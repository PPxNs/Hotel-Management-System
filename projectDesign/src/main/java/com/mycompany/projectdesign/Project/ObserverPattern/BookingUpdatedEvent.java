package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;

import com.mycompany.projectdesign.Project.Model.Bookings;



/**
 * เป็น Event Object ที่ใช้ใน Observer Pattern สำหรับเหตุการณ์ "การเปลี่ยนแปลงของการจอง" 
 * คลาสนี้ทำหน้าที่แจ้งเปลี่ยนแปลงของการจอง
 * ออบเจกต์นี้จะถูกส่งไปยัง ระบบหน้าต่างที่เกี่ยวข้องเพื่อเปลี่ยนแปลง
 */

public class BookingUpdatedEvent implements HotelEvent{
    private final Bookings booking;         //รายการที่ทำการจองไว้
    private final LocalDateTime timetamp;   //เวลาที่เมื่อเปลี่ยน

    /**
     * Constructor สำหรับสร้าง BookingStatusUpdatedEvent
     * @param booking   ออบเจกต์ Bookings ที่เกี่ยวข้อง
     * @param timetamp  เวลาที่เกิดเหตุการณ์
    */
    public BookingUpdatedEvent(Bookings booking, LocalDateTime timetamp){
        this.booking = booking;
        this.timetamp = timetamp;
    }

    /**
     * ดึงข้อมูลการจอง
     * @return ออบเจกต์ Bookings
     */
    public Bookings getBookings(){return booking;}

    /**
     * ดึงเวลาที่เกิดเหตุการณ์ (Timestamp)
     * @return เวลาที่เกิดเหตุการณ์ (LocalDateTime)
     */
    @Override
    public LocalDateTime getTimetamp() { return timetamp; }
}
