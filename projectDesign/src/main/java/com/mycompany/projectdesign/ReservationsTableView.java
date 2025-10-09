package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.beans.property.*;

/**
 * เป็นคลาสะสำหรับใช้กับ TableView ในหน้าจอ Reservations
 * ทำหน้าที่เป็น "Wrapper" โดยการรวบรวมข้อมูลจากหลายๆ Model (Room, Customer, Bookings) มาไว้ในที่เดียว และห่อหุ้มข้อมูลเหล่านั้นด้วย JavaFX Properties ฃ
 * เพื่อให้ TableView สามารถแสดงผล, ติดตามการเปลี่ยนแปลง, และแก้ไขข้อมูลได้
 */
public class ReservationsTableView {

    // JavaFX Properties สำหรับผูกกับคอลัมน์ใน TableView
    // หมายเหตุ  : StringProperty เป็นคลาสใน JavaFX ที่ใช้สำหรับจัดการข้อมูลประเภท String 
    //            ในลักษณะของ Property (คุณสมบัติ) ซึ่งสามารถผูก (bind) หรือสังเกต (observe) การเปลี่ยนแปลงของค่าได้โดยตรง 
    private final StringProperty bookingID;
    private final StringProperty numberRoom;
    private final StringProperty fullnameCustomer;
    private final StringProperty Checkin;
    private final StringProperty Checkout;
    private final StringProperty status ;
    private final StringProperty booking;
    private final StringProperty amount;

    // เก็บออบเจกต์ต้นฉบับไว้ เพื่อให้ Controller สามารถเข้าถึงข้อมูลจริงได้เมื่อต้องการ
    private final Room room;
    private final Customer customer;
    private final Bookings bookings;
    private final AmountPaid amountPaid;
    
    /**
     * Constructor ที่ทำหน้าที่หุ้มข้อมูล จาก Model หลัก (Room, Customer, Bookings) มาเป็น JavaFX Properties
     * @param room     ออบเจกต์ Room ต้นฉบับ
     * @param customer ออบเจกต์ Customer ต้นฉบับ
     * @param bookings ออบเจกต์ Bookings ต้นฉบับ
     * @param amountPaid ออบเจกต์ amountPaid ต้นฉบับ
     */
    public ReservationsTableView(Room room, Customer customer, Bookings bookings, AmountPaid amountPaid) {
        this.room = room;
        this.customer = customer;
        this.bookings = bookings;
        this.amountPaid = amountPaid;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.fullnameCustomer = new SimpleStringProperty(customer.getFullName());
        this.Checkin = new SimpleStringProperty(bookings.getCheckin());
        this.Checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
        this.booking = new SimpleStringProperty(bookings.getBooking());
        this.amount = new SimpleStringProperty(String.format("%.2f", amountPaid.getAmount()));
    }   

    // Getter
    // เมธอดเหล่านี้คืนค่าเป็น String ธรรมดา เพื่อให้ TableView ดึงไปแสดงผล
    // มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return Checkin.get(); }
    public String getCheckout() { return Checkout.get(); }
    public String getStatus(){ return status.get();}
    public String getBooking() { return booking.get(); }
    public String getAmount(){ return amount.get();}
     
    // getter object จริง
    // เมธอดเหล่านี้เพื่อให้ Controller สามารถดึงออบเจกต์ข้อมูลจริงๆ ไปใช้งานต่อได้
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public Bookings getBookings() { return bookings;}
    public AmountPaid getAmountPaid() { return amountPaid;}
}
