package com.mycompany.projectdesign.Project.Service;

import java.time.LocalDateTime;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * Service Class ที่เกี่ยวกับห้องพัก
 * ทำหน้าที่คำนวณสถานะของห้องพักตามเวลาจริง (Real-time) โดยพิจารณาข้อมูล จากหลายแหล่งประกอบกัน เช่น สถานะที่บันทึกไว้ในห้องและข้อมูลการจองปัจจุบัน
 */
public class RoomService {
    private final BookingRepository bookingRepository = BookingRepository.getInstance();

    /**
     * คำนวณและคืนค่าสถานะของห้องพักตามเวลาจริง โดยมีลำดับความสำคัญในการตรวจสอบดังนี้:
     * ถ้าสถานะของห้องเป็น MAINTENANCE จะคืนค่านี้ทันที
     * ตรวจสอบข้อมูลการจองทั้งหมด หากพบว่ามีผู้เข้าพักอยู่ในห้อง ณ เวลานั้น จะคืนค่าว่าห้องนั้น OCCUPIED
     * หากสถานะของห้องเป็น CLEANING และยังอยู่ในช่วงเวลา 30 นาทีหลังเช็คเอาท์
     * หากไม่เข้าเงื่อนไขใด ๆ ข้างต้น แสดงว่าห้อง AVAILABLE
     * @param room ออบเจกต์ Room ที่ต้องการตรวจสอบสถานะ
     * @return RoomStatus สถานะของห้องตามเวลาจริง
     */
    public RoomStatus getRealTimeStatus(Room room){
        // ตรวจสอบสถานะห้อง ถ้าห้อง เป็น MAINTENANCE จะคืนค่านี้ทันที //จองไม่ได้
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            return RoomStatus.MAINTENANCE;
        }

        LocalDateTime now = LocalDateTime.now();

        // ตรวจสอบว่ามีผู้เข้าพักอยู่ห้องนั้น ๆ หรือไม่ จากข้อมูลการจองทั้งหมด
        boolean isOccupied = bookingRepository.getAllBookings().stream().allMatch(
            b -> b.getRoom().getNumberRoom().equals(room.getNumberRoom())
            && b.getStatus() == BookingStatus.CHECKED_IN
            && !now.isBefore(b.getDateCheckin().atTime(b.getTimeCheckin()))
            && now.isBefore(b.getDateCheckout().atTime(b.getTimeCheckout()))
        );

        if (isOccupied) {
            return RoomStatus.OCCUPIED;
        }

        // ตรวจสอบว่าห้องกำลังอยู่ในช่วง CLEANING หรือไม่
        if (room.getStatus() == RoomStatus.CLEANING && room.getLastCheckoutTime() != null){
            // ถ้ายังไม่เกิน 30 นาทีหลังจากเวลาเช็คเอาท์ล่าสุด
            if (now.isBefore(room.getLastCheckoutTime().plusMinutes(30))) {
                return RoomStatus.CLEANING;
            }
        } 

        // ถ้าไม่เข้าเงื่อนไขใดๆ เลย แสดงว่าห้องว่าง
        return RoomStatus.AVAILABLE;
    }
}
