package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.beans.property.*;

/**
 * เป็นคลาสะสำหรับใช้กับ TableView ในหน้าจอ Guests
 * ทำหน้าที่เป็น "Wrapper" โดยการรวบรวมข้อมูลจากหลายๆ Model (Room, Customer, Bookings) มาไว้ในที่เดียว และห่อหุ้มข้อมูลเหล่านั้นด้วย JavaFX Properties 
 * เพื่อให้ TableView สามารถแสดงผล, ติดตามการเปลี่ยนแปลง, และแก้ไขข้อมูลได้
 */
public class GuestsTableView {
    // JavaFX Properties สำหรับผูกกับคอลัมน์ใน TableView
    // หมายเหตุ  : StringProperty เป็นคลาสใน JavaFX ที่ใช้สำหรับจัดการข้อมูลประเภท String 
    //            ในลักษณะของ Property (คุณสมบัติ) ซึ่งสามารถผูก (bind) หรือสังเกต (observe) การเปลี่ยนแปลงของค่าได้โดยตรง 
    private final StringProperty numberRoom;
    private final StringProperty idCard;
    private final StringProperty bookingID;
    private final StringProperty firstName ;
    private final StringProperty lastName ;
    private final StringProperty checkin;
    private final StringProperty checkout;
    private final StringProperty status;
    private final StringProperty email ;
    private final StringProperty phone ;
    private final StringProperty gender ;
    private final StringProperty address ;
    private final StringProperty city ;
    private final StringProperty country ;

    // เก็บออบเจกต์ต้นฉบับไว้ เพื่อให้ Controller สามารถเข้าถึงข้อมูลจริงได้เมื่อต้องการ
    private final Room room;
    private final Customer customer;
    private final Bookings bookings;

    /**
     * Constructor ที่ทำหน้าที่หุ้มข้อมูล จาก Model หลัก (Room, Customer, Bookings) มาเป็น JavaFX Properties
     * @param room     ออบเจกต์ Room ต้นฉบับ
     * @param customer ออบเจกต์ Customer ต้นฉบับ
     * @param bookings ออบเจกต์ Bookings ต้นฉบับ
     */
    public GuestsTableView(Room room, Customer customer, Bookings bookings) {
        this.room = room;
        this.customer = customer;
        this.bookings = bookings;

        // ดึงข้อมูลจาก Model แต่ละตัวมาสร้างเป็น SimpleStringProperty
        // หมายเหตุ  : SimpleStringProperty เป็นคลาสที่ช่วยให้การจัดการข้อมูลแบบ dynamic 
        //            และการผูกข้อมูลใน JavaFX ง่ายขึ้น โดยเหมาะสำหรับการพัฒนา UI ที่ต้องการตอบสนองต่อการเปลี่ยนแปลงของข้อมูลแบบเรียลไทม์
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.idCard = new SimpleStringProperty(customer.getidCard());
        this.firstName = new SimpleStringProperty(customer.getFirstnameCustomer());
        this.lastName = new SimpleStringProperty(customer.getLastnameCustomer());
        this.checkin = new SimpleStringProperty(bookings.getCheckin());
        this.checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
        this.email = new SimpleStringProperty(customer.getEmail());
        this.phone = new SimpleStringProperty(customer.getPhone());
        this.gender = new SimpleStringProperty(customer.getGender());
        this.address = new SimpleStringProperty(customer.getAddress());
        this.city = new SimpleStringProperty(customer.getCity());
        this.country = new SimpleStringProperty(customer.getCountry());

    }

    // Getter
    // เมธอดเหล่านี้คืนค่าเป็น String ธรรมดา เพื่อให้ TableView ดึงไปแสดงผล
    // มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getIdCard() { return idCard.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }    
    public String getCheckin() { return checkin.get(); }
    public String getCheckout() { return checkout.get(); }
    public String getStatus() { return status.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getGender() { return gender.get(); }    
    public String getAddress() { return address.get(); }
    public String getCity() { return city.get(); }
    public String getCountry() { return country.get(); }

    // Setter
    // เมธอดเหล่านี้รับค่า String ธรรมดา แล้วไป .set() ค่าใน StringProperty ซึ่งจะทำให้ UI อัปเดตอัตโนมัติ
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }    
    public void setEmail(String email) { this.email.set(email); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setGender(String gender) { this.gender.set(gender); }    
    public void setAddress(String address) { this.address.set(address); }
    public void setCity(String city) { this.city.set(city); }
    public void setCountry(String country) { this.country.set(country); }   

    // getter object จริง
    // เมธอดเหล่านี้เพื่อให้ Controller สามารถดึงออบเจกต์ข้อมูลจริงๆ ไปใช้งานต่อได้
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public Bookings getBookings(){ return bookings;}
}
