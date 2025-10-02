package com.mycompany.projectdesign.Project.Model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * เป็นคลาส (Model class) ที่ใช้ในการเก็บข้อมูลและคุณสมบัติของห้องพักในโรงแรม
 * ทำหน้าที่เป็นโครงสร้างข้อมูลเพื่อให้คลาสอื่น ๆ ของระบบสามารถเข้าถึงและจัดการข้อมูลของห้องพักได้
 */

public class Room {

    //เราจะทำการเก็บตัวของข้อมูลของห้องพักทั้งหมด โดยอาศัยการรับข้อมูลจากคลาสของ RoomRepository มาเก็บในคลาสนี้

    private final String numberRoom ;   // ตัวแปรเก็บข้อมูลหมายเลยห้องที่ไม่สามารถเปลี่ยนแปลงได้ (Immutable) จีงใช้ final
    private String type;                // ตัวแปรเก็บข้อมูลประเภทของห้องซึ่งระบบจัดการโรงแรมของเรามีทั้งหมด 4 ประเภท ได้แก่ Single room, Double room, Twin room, Suite 
    private double price;               // ตัวแปรเก็บข้อมูลราคาห้องพักต่อคืน
    private RoomStatus status;          // ตัวแปรเก็บข้อมูลสถานะห้องว่าง Data type จะเป็น ENUM จากคลาส RoomStatus ซึ่งเป็นคลาสที่กำหนดสถานะต่าง ๆ ของ Room
    private String imagePath;           // ตัวแปรเก็บที่อยู่ (Path) ของรูปภาพห้องพัก
    private int people;                 // ตัวแปรเก็บข้อมูลจำนวนผู้เข้าพักสูงสุดที่ห้องสามารถรองรับได้
    private List<String> properties ;   // ตัวแปรเก็บรายการคุณสมบัติ/สิ่งอำนวยความสะดวกในห้อง
    private LocalDateTime lastCheckoutTime; // ตัวแปรเก็บข้อมูลวันที่และเวลาที่ลูกค้าคนล่าสุดทำการเช็คเอาท์จากห้องพัก

    
    /**
     * เป็น Constructor หลักสำหรับสร้าง Object Room พร้อมกำหนดค่าเริ่มต้น
     * @param numberRoom   หมายเลขห้อง  
     * @param type         ประเภทห้อง 
     * @param price        ราคาห้องต่อคืน
     * @param imagePath    ที่อยู่ (Path) ของรูปภาพ
     * @param people       จำนวนผู้เข้าพักสูงสุด
     * @param properties   รายการสิ่งอำนวยความสะดวก
     * @param status       สถานะเริ่มต้นของห้อง
     */

    public Room(String numberRoom, String type, double price , String imagePath,int people, List<String> properties, RoomStatus status){
        this.numberRoom = numberRoom;
        this.type = type;
        this.price = price;
        this.imagePath = imagePath; 
        this.people = people;
        this.properties = new ArrayList<>(properties);
        this.status = status;
        this.lastCheckoutTime = null; // กำหนดค่าเริ่มต้นเป็น null 
    }

    // Getter พวกด้านล่างนี้มีไว้ดึงค่าที่เก็บในตัวแปรดังกล่าวในระบบอื่นสามารถเรียกใช้ได้

    /**
     * ดึงข้อมูลหมายเลขห้องพัก
     * @return หมายเลขห้องพัก (String)
     */
    public String getNumberRoom(){
        return numberRoom;
    }

     /**
     * ดึงข้อมูลประเภทของห้องพัก
     * @return ประเภทของห้องพัก (String)
     */
    public String getType(){
        return type;
    }

    /**
     * ดึงข้อมูลราคาห้องพัก
     * @return ราคาห้องพัก (double)
     */
    public double getPrice(){
        return price;
    }

    /**
     * ดึงข้อมูลที่อยู่รูปภาพของห้องพัก
     * @return ที่อยู่รูปภาพ (String)
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * ดึงข้อมูลจำนวนผู้เข้าพักสูงสุด
     * @return จำนวนผู้เข้าพักสูงสุด (int)
     */
    public int getPeople(){
        return people;
    }

    /**
     * ดึงรายการคุณสมบัติทั้งหมดของห้อง
     * @return รายการคุณสมบัติ (List<String>)
     */
    public List<String> getProperties(){
        return new ArrayList<>(properties);
    }

    /**
     * ดึงข้อมูลสถานะปัจจุบันของห้อง
     * @return สถานะของห้อง (RoomStatus)
     */
    public RoomStatus getStatus(){
        return status;
    }

    /**
     * ดึงข้อมูลเวลาเช็คเอาท์ล่าสุดของห้อง
     * @return เวลาเช็คเอาท์ล่าสุด (LocalDateTime) หรือ null หากยังไม่เคยมีการเช็คเอาท์
     */
    public LocalDateTime getLastCheckoutTime(){
        return lastCheckoutTime;
    }

    //setter

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงประเภทของห้องพัก
     * @param roomType ประเภทห้องใหม่
     */
    public void setRoomType(String roomType) { this.type = roomType;}

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงราคาห้องพัก
     * @param price ราคาใหม่
     */
    public void setPrice(Double price) { this.price = price; }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงที่อยู่รูปภาพของห้องพัก
     * @param imagePath ที่อยู่รูปภาพใหม่
     */
    public void setImagePath(String imagePath){ this.imagePath = imagePath; }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงจำนวนผู้เข้าพักสูงสุด
     * @param people จำนวนผู้เข้าพักใหม่
     */
    public void setPeople(int people) { this.people = people;}

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงสถานะของห้องพัก
     * @param status สถานะใหม่
     */
    public void setStatus(RoomStatus status) {this.status = status; }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงรายการคุณสมบัติทั้งหมดของห้อง
     * @param properties รายการคุณสมบัติใหม่
     */
    public void setProperties (List<String> properties ){ this.properties = properties;} 

    /**
     * ตั้งค่าเวลาเช็คเอาท์ล่าสุด
     * @param lastCheckoutTime เวลาที่ทำการเช็คเอาท์
     */
    public void setLastCheckoutTime(LocalDateTime lastCheckoutTime){ this.lastCheckoutTime = lastCheckoutTime ;}
}
