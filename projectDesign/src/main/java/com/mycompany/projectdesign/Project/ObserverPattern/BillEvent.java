package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.Room;

// ข้อมูลสำหรับการพิมพ์
public class BillEvent implements HotelEvent {
    private final Room room;
    private final Customer customer;
    private final DepositRoom depositRoom;
    private final LocalDateTime timetamp; 
    
    public BillEvent(Room room, Customer customer, DepositRoom depositRoom, LocalDateTime timetamp){
        this.room = room;
        this.customer = customer;
        this.depositRoom = depositRoom;
        this.timetamp = timetamp;
    }
    public Room getRoom(){ return room; }
    public Customer getCustomer(){return customer;}
    public DepositRoom getDepositRoom(){ return depositRoom;}

    @Override
    public LocalDateTime getTimetamp() { return timetamp; }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.timetamp.format(formatter);
    }


}
   
    
