package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.beans.property.*;

public class RoomsTableView {

    private final StringProperty numberRoom;
    private final StringProperty image;
    private final StringProperty roomType;
    private final StringProperty price;
    private final StringProperty people;
    private final StringProperty property;
    private final StringProperty status;
    

    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;

    public RoomsTableView(Room room) {
        this.room = room;
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.image = new SimpleStringProperty(room.getImagePath()); 
        this.roomType = new SimpleStringProperty(room.getType());
        this.price = new SimpleStringProperty(String.valueOf(room.getPrice()));
        this.people = new SimpleStringProperty(String.valueOf(room.getPeople()));
        this.property = new SimpleStringProperty(String.join(", ", room.getProperties()));
        this.status = new SimpleStringProperty(room.getStatus().name());
    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getNumberRoom() { return numberRoom.get(); }
    public String getImage() { return image.get();}
    public String getRoomType() { return roomType.get(); }
    public String getPrice() { return price.get(); }
    public String getPeople() { return people.get(); }
    public String getProperty() { return property.get(); }
    public String getStatus() { return status.get(); }

    // getter object จริง
    public Room getRoom() { return room; }



}