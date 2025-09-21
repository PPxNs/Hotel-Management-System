package com.mycompany.projectdesign.Project.ObserverPattern;


import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * Interface: (Observer) สำหรับผู้สังเกตการณ์
 */

 //คิดว่าจะมีการแจ้งเตือนเป็นบิล และชี้แจงรายละเอียดของผู้เข้าพักและหลายละเอียดวันจอง เช็คอิน เช็คเอาส์ เซอร์วิสต่าง ๆ ที่ได้สร้างมาก มีการแสดงรายการชื่อของคนที่พักในห้องเดียวกันครบถ้วนด้วย
 //คิดว่าจะใช้ ฐานข้อมูลของ Customer และ Room มาใช้ 
 
public interface RoomObserver {
    public void update(Room room, Customer customer, DepositRoom depositRoom);
}
