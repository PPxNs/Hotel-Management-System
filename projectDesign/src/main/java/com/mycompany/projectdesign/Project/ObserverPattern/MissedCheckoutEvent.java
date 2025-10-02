package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.Room;

/**
 * เป็น Event Object ที่ใช้ใน Observer Pattern สำหรับเหตุการณ์ "ลูกค้าไม่เช็คเอาท์ตามกำหนด" (Missed Check-out)
 * คลาสนี้ทำหน้าที่รวบรวมและส่งต่อข้อมูลที่เกี่ยวข้องกับกรณีที่ลูกค้ายังไม่คืนห้องพัก
 */

public class MissedCheckoutEvent implements HotelEvent {

    private final Room room;                //ห้องพักที่ยังไม่มีการเช็คเอาท์
    private final Customer customer;        //ลูกค้าที่ยังไม่เช็คเอาท์
    private final Bookings booking;         //การจองที่เกี่ยวข้อง
    private final LocalDateTime timetamp;   //เวลาที่ตรวจพบ
    
    /**
     * Constructor สำหรับสร้าง MissedCheckoutEvent
     * @param room      ออบเจกต์ Room ที่เกี่ยวข้อง
     * @param customer  ออบเจกต์ Customer ที่เกี่ยวข้อง
     * @param booking   ออบเจกต์ Bookings ที่เกี่ยวข้อง
     * @param timetamp  เวลาที่เกิดเหตุการณ์
     */
    public MissedCheckoutEvent(Room room, Customer customer,Bookings booking ,LocalDateTime timetamp){
        this.room = room;
        this.customer = customer;
        this.timetamp = timetamp;
        this.booking = booking;
    }

    /**
     * ดึงข้อมูลห้องพัก
     * @return ออบเจกต์ Room
     */
    public Room getRoom(){ return room; }

    /**
     * ดึงข้อมูลลูกค้า
     * @return ออบเจกต์ Customer
     */
    public Customer getCustomer(){return customer;}

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

    /**
     * ดึงเวลาที่เกิดเหตุการณ์ในรูปแบบ String ที่จัดรูปแบบแล้ว
     * @return String ของเวลาในรูปแบบ "yyyy-MM-dd HH:mm"
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.timetamp.format(formatter);
    }
    
}
