package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.AmountPaid;
import com.mycompany.projectdesign.Project.Model.Bookings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * เป็นคลาสะสำหรับใช้กับ TableView ในหน้าจอ Home
 * ทำหน้าที่เป็น "Wrapper" โดยการรวบรวมข้อมูลจาก Model (Bookings, AmountPaid) มาไว้ในที่เดียว และห่อหุ้มข้อมูลเหล่านั้นด้วย JavaFX Properties 
 * เพื่อให้ TableView สามารถแสดงผล, ติดตามการเปลี่ยนแปลงได้
 */

public class HomeTableView {

    // JavaFX Properties สำหรับผูกกับคอลัมน์ใน TableView
    // หมายเหตุ  : StringProperty เป็นคลาสใน JavaFX ที่ใช้สำหรับจัดการข้อมูลประเภท String 
    //            ในลักษณะของ Property (คุณสมบัติ) ซึ่งสามารถผูก (bind) หรือสังเกต (observe) การเปลี่ยนแปลงของค่าได้โดยตรง 
    private final StringProperty bookingID;
    private final StringProperty fullnameCustomer;
    private final StringProperty numberRoom;
    private final StringProperty checkin;
    private final StringProperty checkout;
    private final StringProperty status;
    private final StringProperty amount;

    // เก็บออบเจกต์ต้นฉบับไว้ เพื่อให้ Controller สามารถเข้าถึงข้อมูลจริงได้เมื่อต้องการ
    private final Bookings bookings;
    private final AmountPaid amountPaid;

    /*
     * Constructor สำหรับสร้าง HomeTableView โดยการรวมข้อมูลจาก Bookings และ AmountPaid
     * @param bookings   ออบเจกต์การจองที่เกี่ยวข้อง
     * @param amountPaid ออบเจกต์การชำระเงินที่เกี่ยวข้อง
     */
    public HomeTableView(Bookings bookings, AmountPaid amountPaid) {

        this.bookings = bookings;
        this.amountPaid = amountPaid;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(bookings.getRoom().getNumberRoom());
        this.fullnameCustomer = new SimpleStringProperty(bookings.getCustomer().getFullName());
        this.checkin = new SimpleStringProperty(bookings.getDisplayCheckin());
        this.checkout = new SimpleStringProperty(bookings.getDisplayCheckout());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
        this.amount = new SimpleStringProperty(String.format("%.2f", amountPaid.getAmount()));
    }

    // Getter
    // เมธอดเหล่านี้คืนค่าเป็น String ธรรมดา เพื่อให้ TableView ดึงไปแสดงผล
    // มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getFullnameCustomer() { return fullnameCustomer.get(); }
    public String getCheckin() { return checkin.get(); }
    public String getCheckout(){ return checkout.get();}
    public String getStatus() { return status.get(); }
    public String getAmount(){ return amount.get();}
     

    // getter object จริง
    // เมธอดเหล่านี้เพื่อให้ Controller สามารถดึงออบเจกต์ข้อมูลจริงๆ ไปใช้งานต่อได้
    public Bookings getBookings() { return bookings;}
    public AmountPaid getAmountPaid() { return amountPaid;}
}
