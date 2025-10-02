package com.mycompany.projectdesign.Project.Model;

/**
 * เป็นคลาส (Model class) ที่ใช้ในการเก็บข้อมูลการชำระเงิน 1 รายการ
 * ทำหน้าที่บันทึกจำนวนเงินที่ชำระโดยเชื่อมโยงกับการจอง (Booking) รายการนั้น ๆ
 */

public class AmountPaid {

    private final Bookings booking ;    //ตัวแปรเก็บข้อมูลการจองที่เกี่ยวข้องกับการชำระเงินนี้
    private final double amountPaid ;   //ตัวแปรเก็บข้อมูลจำนวนเงินที่ชำระ

    /**
     * Constructor สำหรับสร้าง Object Payment
     * @param booking    การจองที่ต้องการบันทึกการชำระเงิน
     * @param amountPaid จำนวนเงินที่ชำระ
     */

    public AmountPaid(Bookings booking, Double amount ){
        this.booking = booking;
        this.amountPaid = amount;
    }

    //Getter

    /**
     * ดึงข้อมูลการจองที่เกี่ยวข้องกับการชำระเงินนี้
     * @return ออบเจกต์ Booking
     */
    public Bookings getBooking(){
        return booking;
    }

    /**
     * ดึงข้อมูลจำนวนเงินที่ชำระ
     * @return จำนวนเงินที่ชำระ
     */
    public Double getAmount(){
        return amountPaid;
    }

}
