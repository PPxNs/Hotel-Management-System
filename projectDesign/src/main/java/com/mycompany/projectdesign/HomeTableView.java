package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.Bookings;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HomeTableView {
    private final StringProperty bookingID;
    private final StringProperty fullnameCustomer;
    private final StringProperty numberRoom;
    private final StringProperty checkin;
    private final StringProperty checkout;
    //private final StringProperty amountPaid;
    private final StringProperty status;

    // เก็บ object ต้นฉบับไว้ด้วย

    private final Bookings bookings;

    public HomeTableView(Bookings bookings) {

        this.bookings = bookings;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(bookings.getRoom().getNumberRoom());
        this.fullnameCustomer = new SimpleStringProperty(bookings.getCustomer().getFullName());
        this.checkin = new SimpleStringProperty(bookings.getCheckin());
        this.checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return checkin.get(); }
    public String getCheckout(){ return checkout.get();}
    public String getStatus() { return status.get(); }
     

    // getter object จริง
    public Bookings getBookings() { return bookings;}
}
