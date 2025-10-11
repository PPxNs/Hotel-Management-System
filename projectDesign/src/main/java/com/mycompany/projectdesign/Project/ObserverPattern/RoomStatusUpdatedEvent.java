package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import com.mycompany.projectdesign.Project.Model.Room;


/**
 * เป็น Event Object ที่ใช้ใน Observer Pattern สำหรับเหตุการณ์ "การเปลี่ยนแปลงสถานะจาก CLEANING เป็น AVAILABLE อัตโนมัติ หลังเช็คเอาส์ได้ 30 นาที" 
 * คลาสนี้ทำหน้าที่แจ้งเปลี่ยนแปลงสถานะของห้องพัก
 * ออบเจกต์นี้จะถูกส่งไปยัง ระบบหน้าต่างของ Rooms 
 */

public class RoomStatusUpdatedEvent implements HotelEvent{
    private final Room room;                //ห้องพักนั้น ๆ
    private final LocalDateTime timetamp;   //เวลาที่เมื่อเปลี่ยนสถานะ

    /**
     * Constructor สำหรับสร้าง RoomStatusUpdatedEvent
     *
     * @param room      ออบเจกต์ Room ที่เกี่ยวข้อง
     * @param timetamp  เวลาที่เกิดเหตุการณ์
    */
    public RoomStatusUpdatedEvent(Room room, LocalDateTime timetamp){
        this.room = room;
        this.timetamp = timetamp;
    }

    /**
     * ดึงข้อมูลห้องพัก
     * @return ออบเจกต์ Room
     */
    public Room getRoom(){ return room; }

    /**
     * ดึงเวลาที่เกิดเหตุการณ์ (Timestamp)
     * @return เวลาที่เกิดเหตุการณ์ (LocalDateTime)
     */
    @Override
    public LocalDateTime getTimetamp() { return timetamp; }
}
