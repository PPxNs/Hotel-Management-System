package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;

import javafx.beans.property.*;

public class GuestsTableView {
    private final StringProperty numberRoom;
    private final StringProperty idCard;
    private final StringProperty bookingID;
    private final StringProperty fullnameCustomer;
    private final StringProperty checkin;
    private final StringProperty checkout;
    private final StringProperty status;
    

    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;
    private final Customer customer;
    private final Bookings bookings;

    public GuestsTableView(Room room, Customer customer, Bookings bookings) {
        this.room = room;
        this.customer = customer;
        this.bookings = bookings;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.idCard = new SimpleStringProperty(customer.getidCard());
        this.fullnameCustomer = new SimpleStringProperty(customer.getFullName());
        this.checkin = new SimpleStringProperty(bookings.getCheckin());
        this.checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getIdCard() { return idCard.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return checkin.get(); }
    public String getCheckout() { return checkout.get(); }
    public String getStatus() { return status.get(); }

    // getter object จริง
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public Bookings getBookings(){ return bookings;}
}
