package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;

/**
 * เป็นคลาสสำหรับใช้กับ TableView ในหน้าจอ Rooms
 * ทำหน้าที่เป็น "Wrapper" โดยการรวบรวมข้อมูลจาก Model (Room) และห่อหุ้มข้อมูลเหล่านั้นด้วย JavaFX Properties 
 * เพื่อให้ TableView สามารถแสดงผล, ติดตามการเปลี่ยนแปลงข้อมูลได้
 */
public class RoomsTableView {

    // เก็บออบเจกต์ต้นฉบับไว้ เพื่อให้ Controller สามารถเข้าถึงข้อมูลจริงได้เมื่อต้องการ
    private final Room room;

    /**
     * Constructor สำหรับสร้าง RoomsTableView wrapper
     * @param room ออบเจกต์ Room 
     */
    public RoomsTableView(Room room) {
        this.room = room;
    }

    /**
     * ดึงออบเจกต์ Room ต้นฉบับ
     * @return ออบเจกต์ Room
     */
    public Room getRoom() { return room; }

    // Getter
    // เมธอดเหล่านี้คืนค่าเป็น String ธรรมดา เพื่อให้ TableView ดึงไปแสดงผล
    // มันเป็น StringProperty จะต้องใช้ .get
    public String getNumberRoom() { return this.room.getNumberRoom(); }
    public String getImage() { return this.room.getImagePath();}
    public String getRoomType() { return this.room.getType(); }
    public String getPrice() { return String.valueOf(this.room.getPrice()); }
    public String getPeople() { return String.valueOf(this.room.getPeople()); }
    public String getProperty() { return String.join(", ",this.room.getProperties()); }
    public String getStatus() { return this.room.getStatus().toString(); }

    



}