package com.mycompany.projectdesign.Project.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Service สำหรับจัดการงานอัตโนมัติตามเวลา
 * คลาสนี้ทำหน้าที่เป็น "นาฬิกาปลุก" ของระบบ โดยจะตรวจสอบสถานะของห้องพักและการจองทุกๆ นาที
 * เพื่อบังคับใช้กฎที่เกี่ยวกับเวลา เช่น การแจ้งเตือน, การยกเลิกอัตโนมัติ
 */

public class BookingScheduler {
    
    private final RoomRepository roomRepository = RoomRepository.getInstance();
    private final BookingRepository bookingRepository = BookingRepository.getInstance();
    private final HotelEventManager eventManager = HotelEventManager.getInstance();
    
    /** Executor Service สำหรับการรัน Task ตามเวลาที่กำหนดบน Background Thread */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BookingScheduler(){}

    public void start(){
        //ให้เช็คว่ามีอะไรต้องแจ้งเตือนมั้ยทุก ๆ 1 นาที
        scheduler.scheduleAtFixedRate(this :: runChecks, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * หยุดการทำงานของ Scheduler (ควรเรียกใช้เมื่อปิดโปรแกรม)
     */
    public void stop(){
        scheduler.shutdown();
    }

    /**
     * เมธอดหลักที่รวบรวม Task ทั้งหมดที่จะถูกรันทุกๆ นาที
     */
    private void runChecks(){

        LocalDateTime now = LocalDateTime.now();
        boolean bookingsModified = false;
        //ตรวจสถานะห้องก่อน
        for (Room room : roomRepository.getAllRooms()){
            // ถ้าห้องอยู่ในสถานะ "ทำความสะอาด" และมีเวลาเช็คเอาท์ล่าสุดบันทึกอยู่
            if (room.getStatus() == RoomStatus.CLEANING && room.getLastCheckoutTime() != null ) {
                //ถ้าเวลาปัจจุบันเลยเวลาเช็คเอาท์ไปแล้ว 30 นาที
                if (now.isAfter(room.getLastCheckoutTime().plusMinutes(30))) {
                    room.setStatus(RoomStatus.AVAILABLE); // เปลี่ยนสถานะเป็น "ว่าง"
                    room.setLastCheckoutTime(null);
                    roomRepository.saveRoomToCSV(); // บันทึกการเปลี่ยนแปลง
                }
            }
        }
        
        // ตรวจสอบสถานะการจองทั้งหมด
        for(Bookings booking : bookingRepository.getAllBookings()){
            LocalDateTime checkinTime = booking.getDateCheckin().atTime(booking.getTimeCheckin());
            LocalDateTime checkoutTime = booking.getDateCheckout().atTime(booking.getTimeCheckout());
            
            //จัดการการจองที่ยังไม่ถึงเวลาเช็คอิน (สถานะ CONFIRMED)
            // วางแผนว่าให้โทรเช็คคนจองก่อน 10 นาที
            if (booking.getStatus() == BookingStatus.CONFIRMED) {

                // ถ้าอยู่ในช่วง 10 นาทีก่อนเช็คอิน และยังไม่เคยแจ้งเตือน
                if (!booking.isCheckinNotified() && now.isAfter(checkinTime.minusMinutes(10))&& now.isBefore(checkinTime)) {
                    // สร้าง Event และส่งไปให้ Observer (MissedCheckinObserver)
                    MissedCheckinEvent event = new MissedCheckinEvent(booking.getRoom(), booking.getCustomer(), booking , now);
                    eventManager.notifyObserver(event);
                    booking.setCheckinNotified(true); //ตั้งค่าเพื่อบอกว่าแจ้งแล้ว
                    bookingsModified = true;
                }   

                //ถ้าเลยเวลาเช็คอินแล้วแต่ยังไม่มา (Missed Check-in)
                //ถ้าเลยเวลาเช็คอินก็ยกเลิกออโต้
                if (now.isAfter(checkinTime)) {
                    booking.setStatus(BookingStatus.CANCELLED); // ยกเลิกการจองอัตโนมัติ
                    bookingRepository.saveBookingToCSV();
                    showAlert("Booking Auto-Cancelled", "ห้องหมายเลข : " + booking.getRoom().getNumberRoom() + " ยกเลิกอัตโนมัติ");
                }
            }

            // จัดการการจองที่เช็คอินแล้ว (สถานะ CHECKED_IN)
            // วางแผนว่าให้โทรเช็คคน checkout ก่อน 5 นาที
            if (booking.getStatus() == BookingStatus.CHECKED_IN) {

                // ถ้าอยู่ในช่วง 5 นาทีก่อนเช็คเอาท์ และยังไม่เคยแจ้งเตือน
                if (!booking.isCheckoutNotified() && now.isBefore(checkoutTime.minusMinutes(5))&& now.isBefore(checkoutTime)) {
                    
                    // สร้าง Event และส่งไปให้ Observer (MissedCheckoutObserver)
                    MissedCheckoutEvent event = new MissedCheckoutEvent(booking.getRoom(), booking.getCustomer(), booking, now);
                    eventManager.notifyObserver(event);
                    booking.setCheckoutNotified(true); // ตั้งค่าว่าแจ้งเตือนแล้ว
                    bookingsModified = true;
                }
            }
        }

            // ถ้ามีการเปลี่ยนแปลงข้อมูลการจอง ให้บันทึกลง CSV 
            if (bookingsModified) {
                bookingRepository.saveBookingToCSV();
            }
        }
    
        /**
        * เมธอดช่วยสำหรับแสดง Alert บน UI Thread 
        * @param title   หัวข้อของ Alert
        * @param message ข้อความใน Alert
        */
        private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
