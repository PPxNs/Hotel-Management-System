package com.mycompany.projectdesign.Project.ObserverPattern;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.Room;

/**
 * เป็น Event Object ที่ใช้ใน Observer Pattern สำหรับเหตุการณ์ "เกิดการซ่อมแซมห้องพักกะทันหัน" 
 * คลาสนี้ทำหน้าที่รวบรวมและส่งต่อข้อมูลที่เกี่ยวข้องกับกรณีที่มีการเปลี่ยนแปลงสถานะของห้องพักกะทันหัน 
 * แต่มีการจองของห้องพักตั้งแต่ช่วงวันที่เปลี่ยนสถานะ และถัดไป 3 วัน
 * ออบเจกต์นี้จะถูกส่งไปยัง ระบบแจ้งเตือนแอดมิน 
 */

public class MaintenanceRoomEvent implements HotelEvent {
    private final Room room;                //ห้องพักที่ถูกจองไว้
    private final Customer customer;        //ลูกค้าที่ทำการจองไว้
    private final Bookings booking;         //รายการที่ทำการจองไว้
    private final LocalDateTime timetamp;   //เวลาที่เมื่อเปลี่ยนสถานะ
    
    /**
     * Constructor สำหรับสร้าง MissedCheckinEvent
     *
     * @param room      ออบเจกต์ Room ที่เกี่ยวข้อง
     * @param customer  ออบเจกต์ Customer ที่เกี่ยวข้อง
     * @param booking   ออบเจกต์ Bookings ที่เกี่ยวข้อง
     * @param timetamp  เวลาที่เกิดเหตุการณ์
    */
    public MaintenanceRoomEvent(Room room, Customer customer,Bookings booking ,LocalDateTime timetamp){
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
