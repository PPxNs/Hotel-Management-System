package com.mycompany.projectdesign.Project.Model;

/*
 * เป็นคลาสที่ใช้ในการเก็บข้อมูลของลูกค้า // กำหนดให้คลาสนี้เป็นเก็บ properties เป็น โมเดลเอาไว้ ให้ระบบอื่นเข้าถึงได้
 */


public class Room {

    //เราจะทำการเก็บตัวของข้อมูลของห้องพักทั้งหมด โดยอาศัยการรับข้อมูลจาก ฝั่งของ RoomRepository
    private final String numberRoom ; // เก็บข้อมูลหมายเลยห้อง
    private final String type;    //เก็บข้อมูลประเภทของห้อง เช่น ห้องเดี่ยว ห้องคู่ หรือห้องชุด
    private final double price; // เก็บข้อมูลราคาห้องพัก
    private final String status; // เก็บข้อมูลสถานะห้องว่าง ไม่ว่าง มีคนจองแล้ว หรือกำลังทำความสะอาด
    private final String imagePath; // เพิ่มเก็บรูปห้อง

    
    //อันนี้ค่าจะมาจากส่วนของ FactoryMethodPattern
    //private DepositRoom depositRoom; // เก็บข้อมูลเกี่ยวกับการมัดจำ โดยอันนี้จะรับค่าแยกจากส่วนแรก

    //constuctor รับค่าข้อมูลมาเก็บในตัวแปร เหมือนเข้ามาแล้วไม่สามารถแก้ไขเปลี่ยนแปลงเองได้ เนื่องจากเราเก็บเป็น final
    public Room(String numberRoom, String type, double price , String status, String imagePath){
        this.numberRoom = numberRoom;
        this.type = type;
        this.price = price;
        this.status = status;
        this.imagePath = imagePath; 
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

    public String getStatus(){
        return status;
    }

    //เสียดายค่อยลบ
    /*public void setDepositRoom(DepositRoom depositRoom) {
        this.depositRoom = depositRoom;
    }

    public DepositRoom getDepositRoom() {
        return depositRoom;
    }*/

    public String getImagePath() {
        return imagePath;
    }

    

}
