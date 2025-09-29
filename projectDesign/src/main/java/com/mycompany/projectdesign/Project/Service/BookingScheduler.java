package com.mycompany.projectdesign.Project.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import com.mycompany.projectdesign.Project.Model.RoomStatus;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckinEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckoutEvent;

import javafx.application.Platform;
import javafx.scene.control.Alert;
public class BookingScheduler {
    
    private final RoomRepository roomRepository = RoomRepository.getInstance();
    private final BookingRepository bookingRepository = BookingRepository.getInstance();
    private final HotelEventManager eventManager = HotelEventManager.getInstance();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BookingScheduler(){}

    public void start(){
        //ให้เช็คว่ามีอะไรต้องแจ้งเตือนมั้ยทุก ๆ 1 นาที
        scheduler.scheduleAtFixedRate(this :: runChecks, 0, 1, TimeUnit.MINUTES);
    }

    public void stop(){
        scheduler.shutdown();
    }

    private void runChecks(){

        LocalDateTime now = LocalDateTime.now();
        boolean bookingsModified = false;
        //ตรวจสถานะห้องก่อน
        for (Room room : roomRepository.getAllRooms()){
            if (room.getStatus() == RoomStatus.CLEANING && room.getLastCheckoutTime() != null ) {
                if (now.isAfter(room.getLastCheckoutTime().plusMinutes(30))) {
                    room.setStatus(RoomStatus.AVAILABLE);
                    room.setLastCheckoutTime(null);
                    roomRepository.saveRoomToCSV();
                }
            }
        }
        
        for(Bookings booking : bookingRepository.getAllBookings()){
            LocalDateTime checkinTime = booking.getDateCheckin().atTime(booking.getTimeCheckin());
            LocalDateTime checkoutTime = booking.getDateCheckout().atTime(booking.getTimeCheckout());
            
            // วางแผนว่าให้โทรเช็คคนจองก่อน 10 นาที
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                if (!booking.isCheckinNotified() && now.isAfter(checkinTime.minusMinutes(10))&& now.isBefore(checkinTime)) {
                    MissedCheckinEvent event = new MissedCheckinEvent(booking.getRoom(), booking.getCustomer(), booking , now);
                    eventManager.notifyObserver(event);
                    booking.setCheckinNotified(true); //ตั้งค่าเพื่อบอกว่าแจ้งแล้ว
                    bookingsModified = true;
                }   
                
                //ถ้าเลยเวลาเช็คอินก็ยกเลิกออโต้
                if (now.isAfter(checkinTime)) {
                    booking.setStatus(BookingStatus.CANCELLED);
                    bookingRepository.saveBookingToCSV();
                    showAlert("Booking Auto-Cancelled", "ห้องหมายเลข : " + booking.getRoom().getNumberRoom() + " ยกเลิกอัตโนมัติ");
                }
            }

            // วางแผนว่าให้โทรเช็คคน checkout ก่อน 5 นาที
            if (booking.getStatus() == BookingStatus.CHECKED_IN) {
                if (!booking.isCheckinNotified() && now.isBefore(checkoutTime.minusMinutes(5))&& now.isBefore(checkoutTime)) {
                    MissedCheckoutEvent event = new MissedCheckoutEvent(booking.getRoom(), booking.getCustomer(), booking, now);
                    eventManager.notifyObserver(event);
                    booking.setCheckoutNotified(true);
                    bookingsModified = true;
                }
            }
        }

        if (bookingsModified) {
            bookingRepository.saveBookingToCSV();
        }
    }
    
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
