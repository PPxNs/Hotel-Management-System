package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.Room;

public class MissedCheckoutEvent implements HotelEvent {

    private final Room room;
    private final Customer customer;
    private final Bookings booking;
    private final LocalDateTime timetamp; 
    
    public MissedCheckoutEvent(Room room, Customer customer,Bookings booking ,LocalDateTime timetamp){
        this.room = room;
        this.customer = customer;
        this.timetamp = timetamp;
        this.booking = booking;
    }
    public Room getRoom(){ return room; }
    public Customer getCustomer(){return customer;}
    public Bookings getBookings(){return booking;}

    @Override
    public LocalDateTime getTimetamp() { return timetamp; }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.timetamp.format(formatter);
    }
    
}
