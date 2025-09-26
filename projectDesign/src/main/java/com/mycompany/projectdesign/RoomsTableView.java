package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.beans.property.*;

public class RoomsTableView {

    //ตอนแรกให้มันเป็นฐานดึงเข้า TableView ต้องเปลี่ยนเป็นตัวกลางแทน จะได้ขึ้นทันที
    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;


    public RoomsTableView(Room room) {
        this.room = room;
    }

    // getter object จริง
    public Room getRoom() { return room; }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getNumberRoom() { return this.room.getNumberRoom(); }
    public String getImage() { return this.room.getImagePath();}
    public String getRoomType() { return this.room.getType(); }
    public String getPrice() { return String.valueOf(this.room.getPrice()); }
    public String getPeople() { return String.valueOf(this.room.getPeople()); }
    public String getProperty() { return String.join(", ",this.room.getProperties()); }
    public String getStatus() { return this.room.getStatus().toString(); }

    



}