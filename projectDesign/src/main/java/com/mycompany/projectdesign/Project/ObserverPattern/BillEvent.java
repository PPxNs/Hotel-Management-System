package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Room;

// ข้อมูลสำหรับการพิมพ์
public class BillEvent implements HotelEvent {
    private final Room room;                //ข้อมูลห้องพักที่เกี่ยวข้องกับบิล
    private final Bookings booking;         //ข้อมูลการจองที่เกี่ยวข้องกับบิล
    private final DepositRoom depositRoom;  //ข้อมูลยอดรวมเงินมัดจำและบริการเสริมจาก Decorator Pattern
    private final LocalDateTime timetamp;   //เวลาที่พิมพ์ใบเสร็จ
    

    /**
     * Constructor สำหรับสร้าง BillEvent
     *
     * @param room        ออบเจกต์ Room
     * @param booking     ออบเจกต์ Bookings
     * @param depositRoom ออบเจกต์ DepositRoom
     * @param timetamp    เวลาที่พิมพ์ใบเสร็จ
     */
    public BillEvent(Room room, Bookings booking, DepositRoom depositRoom, LocalDateTime timetamp){
        this.room = room;
        this.booking = booking;
        this.depositRoom = depositRoom;
        this.timetamp = timetamp;
    }

    /**
     * ดึงข้อมูลห้องพัก
     * @return ออบเจกต์ Room
     */
    public Room getRoom(){ return room; }

    /**
     * ดึงข้อมูลการจอง
     * @return ออบเจกต์ Bookings
     */
    public Bookings getBookings(){return booking;}

    /**
     * ดึงข้อมูลเงินมัดจำและบริการเสริม
     * @return ออบเจกต์ DepositRoom
     */
    public DepositRoom getDepositRoom(){ return depositRoom;}

    /**
     * ดึงเวลาที่พิมพ์ใบเสร็จ (Timestamp)
     * @return เวลาที่พิมพ์ใบเสร็จ (LocalDateTime)
     */
    @Override
    public LocalDateTime getTimetamp() { return timetamp; }

    /**
     * ดึงเวลาที่พิมพ์ใบเสร็จในรูปแบบ String ที่จัดรูปแบบแล้ว
     * @return String ของเวลาในรูปแบบ "yyyy-MM-dd HH:mm"
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.timetamp.format(formatter);
    }


}
   
    
