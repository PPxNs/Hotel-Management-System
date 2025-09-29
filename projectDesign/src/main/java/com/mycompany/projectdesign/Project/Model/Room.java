package com.mycompany.projectdesign.Project.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * เป็นคลาสที่ใช้ในการเก็บข้อมูลของลูกค้า // กำหนดให้คลาสนี้เป็นเก็บ properties เป็น โมเดลเอาไว้ ให้ระบบอื่นเข้าถึงได้
 */


public class Room {

    //เราจะทำการเก็บตัวของข้อมูลของห้องพักทั้งหมด โดยอาศัยการรับข้อมูลจาก ฝั่งของ RoomRepository
    private final String numberRoom ; // เก็บข้อมูลหมายเลยห้อง
    private String type;    //เก็บข้อมูลประเภทของห้อง เช่น ห้องเดี่ยว ห้องคู่ หรือห้องชุด
    private double price; // เก็บข้อมูลราคาห้องพัก
    private RoomStatus status; // เก็บข้อมูลสถานะห้องว่าง ไม่ว่าง มีคนจองแล้ว หรือกำลังทำความสะอาด
    private String imagePath; // เพิ่มเก็บรูปห้อง
    private int people; //จำนวนที่สามารถเข้าพักในห้องได้
    private List<String> properties ;
    private LocalDateTime lastCheckoutTime;
    //private final ArrayList<String> property ; //ลิสคุณสมบัติห้องพัก

    
    //อันนี้ค่าจะมาจากส่วนของ FactoryMethodPattern
    //private DepositRoom depositRoom; // เก็บข้อมูลเกี่ยวกับการมัดจำ โดยอันนี้จะรับค่าแยกจากส่วนแรก

    //constuctor รับค่าข้อมูลมาเก็บในตัวแปร เหมือนเข้ามาแล้วไม่สามารถแก้ไขเปลี่ยนแปลงเองได้ เนื่องจากเราเก็บเป็น final
    public Room(String numberRoom, String type, double price , String imagePath,int people, List<String> properties, RoomStatus status){
        this.numberRoom = numberRoom;
        this.type = type;
        this.price = price;
        this.imagePath = imagePath; 
        this.people = people;
        this.properties = new ArrayList<>(properties);
        this.status = status;
        this.lastCheckoutTime = null;
    }

    // พวกด้านล่างนี้มีไว้ดึงค่าที่เก็บในตัวแปรดังกล่าวในระบบอื่นสามารถเรียกใช้ได้
    public String getNumberRoom(){
        return numberRoom;
    }

    public String getType(){
        return type;
    }

    public double getPrice(){
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getPeople(){
        return people;
    }

    public List<String> getProperties(){
        return new ArrayList<>(properties);
    }

    public RoomStatus getStatus(){
        return status;
    }

    public LocalDateTime getLastCheckoutTime(){
        return lastCheckoutTime;
    }

    public void setRoomType(String roomType) { this.type = roomType;}
    public void setPrice(Double price) { this.price = price; }
    public void setImagePath(String imagePath){ this.imagePath = imagePath; }
    public void setPeople(int people) { this.people = people;}
    public void setStatus(RoomStatus status) {this.status = status; }
    public void setProperties (List<String> properties ){ this.properties = properties;} 
    public void setLastCheckoutTime(LocalDateTime lastCheckoutTime){ this.lastCheckoutTime = lastCheckoutTime ;}
}
