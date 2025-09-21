package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;

import javafx.beans.property.*;

public class GuestsTableView {
    private final StringProperty numberRoom;
    private final StringProperty idCard;
    private final StringProperty fullnameCustomer;
    private final StringProperty Checkin;
    private final StringProperty Checkout;
    private final StringProperty statusCustomer;

    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;
    private final Customer customer;

    public GuestsTableView(Room room, Customer customer) {
        this.room = room;
        this.customer = customer;

        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.idCard = new SimpleStringProperty(customer.getidCard());
        this.fullnameCustomer = new SimpleStringProperty(customer.getFullName());
        this.Checkin = new SimpleStringProperty(customer.getCheckin());
        this.Checkout = new SimpleStringProperty(customer.getCheckOut());
        this.statusCustomer = new SimpleStringProperty(customer.getStatusCustomer());
    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getNumberRoom() { return numberRoom.get(); }
    public String getIdCard() { return idCard.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return Checkin.get(); }
    public String getCheckout() { return Checkout.get(); }
    public String getStatusCustomer() { return statusCustomer.get(); }

    // getter object จริง
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
}
