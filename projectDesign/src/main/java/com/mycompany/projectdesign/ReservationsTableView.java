package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.beans.property.*;
public class ReservationsTableView {
    private final StringProperty bookingID;
    private final StringProperty numberRoom;
    private final StringProperty fullnameCustomer;
    private final StringProperty Checkin;
    private final StringProperty Checkout;
    private final StringProperty status ;
    private final StringProperty booking;




    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;
    private final Customer customer;
    private final Bookings bookings;

    public ReservationsTableView(Room room, Customer customer, Bookings bookings) {
        this.room = room;
        this.customer = customer;
        this.bookings = bookings;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.fullnameCustomer = new SimpleStringProperty(customer.getFullName());
        this.Checkin = new SimpleStringProperty(bookings.getCheckin());
        this.Checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
        this.booking = new SimpleStringProperty(bookings.getBooking());
    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return Checkin.get(); }
    public String getCheckout() { return Checkout.get(); }
    public String getStatus(){ return status.get();}
    public String getBooking() { return booking.get(); }

     
    // getter object จริง
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public Bookings getBookings() { return bookings;}
}
