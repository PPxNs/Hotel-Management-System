package com.mycompany.projectdesign.Project.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * เป็นคลาส (Model class) ที่ใช้ในการเก็บข้อมูลการจองห้องพักต่อ 1 รายการ
 * ทำหน้าที่เชื่อมข้อมูลระหว่างลูกค้า (Customer) กับห้องพัก (Room)
*/
public class Bookings {
    private final Room room;                // ตัวแปรออบเจกต์ของห้องพักที่ถูกจอง
    private final Customer customer;        // ตัวแปรออบเจกต์ของลูกค้าที่ทำการจอง
    private final String bookingID;         // ตัวแปรเก็บหมายเลขจอง (อันนี้จะไม่ซ้ำกัน)
    private final LocalDate dateBooking ;   // ตัวแปรเก็บวันที่จองของลูกค้า
    private final LocalDate dateCheckin;    // ตัวแปรเก็บวันเข้าพักของลูกค้า _/_/_ เก็บเป็น คศ
    private final LocalDate dateCheckout;   // ตัวแปรเก็บวันออกของลูกค้า _/_/_ เก็บเป็น คศ
    private final LocalTime timeCheckin;    // ตัวแปรเก็บเวลาเข้าพักของลูกค้า ชม:นาที
    private final LocalTime timeCheckout;   // ตัวแปรเก็บเวลาออกของลูกค้า ชม:นาที
    private final LocalTime timeBooking;    // ตัวแปรเก็บเวลาจองของลูกค้า ชม:นาที
    private BookingStatus status ;          // ตัวแปรสถานะการมายืนยันการจองหรือการใช้บริการ Data type จะเป็น ENUM จากคลาส BookingStatus ซึ่งเป็นคลาสที่กำหนดสถานะต่าง ๆ ของ สถานะการเข้าพักของลูกค้า
    
    //หมายเหตุ LocalDate คือของวัน LocalTime คือของเวลา

    /**
     * Constructor สำหรับสร้าง Object Booking
     *
     * @param room          ออบเจกต์ห้องพักที่ถูกจอง
     * @param customer      ออบเจกต์ลูกค้าที่ทำการจอง
     * @param bookingID     หมายเลขการจอง
     * @param dateCheckin   วันที่เช็คอิน
     * @param timeCheckin   เวลาเช็คอิน
     * @param dateCheckout  วันที่เช็คเอาท์
     * @param timeCheckout  เวลาเช็คเอาท์
     * @param dateBooking   วันที่ทำการจอง
     * @param timeBooking   เวลาที่ทำการจอง
     */
    public Bookings(Room room, Customer customer, String bookingID, LocalDate datecheckin, LocalTime timeCheckin, LocalDate datecheckout, LocalTime timeCheckout,
                    LocalDate datebooking , LocalTime timeBooking){
        
        this.room = room;
        this.customer = customer;
        this.bookingID = bookingID;
        this.dateCheckin = datecheckin; 
        this.timeCheckin = timeCheckin;
        this.dateCheckout = datecheckout;
        this.timeCheckout = timeCheckout;
        this.dateBooking = datebooking;
        this.timeBooking = timeBooking;
        this.status = BookingStatus.CONFIRMED; // สถานะเริ่มต้นของการจองคือ CONFIRMED
    }

    //Getter พวกด้านล่างนี้มีไว้ดึงค่าที่เก็บในตัวแปรดังกล่าวในระบบอื่นสามารถเรียกใช้ได้

    /**
     * ดึงออบเจกต์ห้องพักที่เกี่ยวข้องกับการจองนี้
     * @return ออบเจกต์ของ Room
     */
    public Room getRoom(){
        return room;
    }

    /**
     * ดึงออบเจกต์ลูกค้าที่เกี่ยวข้องกับการจองนี้
     * @return ออบเจกต์ของ Customer
     */
    public Customer getCustomer(){
        return customer;
    }
    
    /**
     * ดึงวันที่เช็คอิน
     * @return วันที่เช็คอิน (LocalDate)
     */
    public LocalDate getDateCheckin(){
        return dateCheckin ;
    }

    /**
     * ดึงวันที่เช็คเอาท์
     * @return วันที่เช็คเอาท์ (LocalDate)
     */
    public LocalDate getDateCheckout(){
        return dateCheckout;
    }

    /**
     * ดึงวันที่ทำการจอง
     * @return วันที่จอง (LocalDate)
     */
    public LocalDate getDateBooking(){
        return dateBooking;
    }

    /**
     * ดึงสถานะปัจจุบันของการจอง
     * @return สถานะการจอง (BookingStatus)
     */
    public BookingStatus getStatusCustomer(){
        return status;
    }

    /**
     * ดึงเวลาที่เช็คอิน
     * @return เวลาที่เช็คอิน (LocalTime)
     */
    public LocalTime getTimeCheckin(){
        return timeCheckin;
    }

    /**
     * ดึงเวลาที่เช็คเอาท์
     * @return เวลาที่เช็คเอาท์ (LocalTime)
     */
    public LocalTime getTimeCheckout(){
        return timeCheckout;
    }

    /**
     * ดึงเวลาที่ทำการจอง
     * @return เวลาที่จอง (LocalTime)
     */
    public LocalTime getTimeBooking(){
        return timeBooking;
    }  

    /**
     * ดึงข้อมูลวัน-เวลาเช็คอินในรูปแบบ String ที่จัดรูปแบบแล้ว
     * @return String ของวัน-เวลาเช็คอิน (รูปแบบ "yyyy-MM-dd HH:mm")
     */
    public String getCheckin(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateCheckin.atTime(timeCheckin).format(df);

    }

    /**
     * ดึงข้อมูลวัน-เวลาเช็คเอาท์ในรูปแบบ String ที่จัดรูปแบบแล้ว
     * @return String ของวัน-เวลาเช็คเอาท์ (รูปแบบ "yyyy-MM-dd HH:mm")
     */
    public String getCheckOut(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateCheckout.atTime(timeCheckout).format(df);

    }

    /**
     * ดึงข้อมูลวัน-เวลาที่จองในรูปแบบ String ที่จัดรูปแบบแล้ว
     * @return String ของวัน-เวลาที่จอง (รูปแบบ "yyyy-MM-dd HH:mm")
     */
    public String getBooking(){
       DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateBooking.atTime(timeBooking).format(df);

    }

    /**
     * ดึงหมายเลขการจอง
     * @return หมายเลขการจอง (String)
     */
    public String getBookingID(){
        return bookingID;
    }

    /**
     * ดึงสถานะปัจจุบันของการจอง
     * @return สถานะการจอง (BookingStatus)
     */
    public BookingStatus getStatus(){
        return status;
    }

    /**
     * คำนวณจำนวนวันที่จองล่วงหน้าก่อนวันเช็คอิน
     * มีประโยชน์ในการคำนวณโปรโมชั่นการจองล่วงหน้า (Early Bird)
     * @return จำนวนเต็มของวันที่จองล่วงหน้า
     */

    //ใช้ดึงข้อมูลว่าจองก่อนเข้าพักจริงกี่วัน เนื่องจากเรามีตัวโปรโมชันดังนั้นจึงจำเป็นต้องมีการดึงค่าต่อไปนี้ ไปใช้ในการคำนวณ
    public int getDaysBeforeCheckin(){
        return (int) ChronoUnit.DAYS.between(dateBooking, dateCheckin);
    }

    /**
     * คำนวณจำนวนคืนที่เข้าพัก
     * @return จำนวนเต็มของคืนที่เข้าพัก
     */

    //ใช้ดึงข้อมูลว่าพักกี่คืน เนื่องจากเรามีตัวโปรโมชันดังนั้นจึงจำเป็นต้องมีการดึงค่าต่อไปนี้ ไปใช้ในการคำนวณ
    public int getDateStay(){
        return (int) ChronoUnit.DAYS.between(dateCheckin, dateCheckout);
    }

    /**
     * ดึงข้อมูลวัน-เวลาเช็คอินในรูปแบบ LocalDateTime
     * @return LocalDateTime ของวัน-เวลาเช็คอิน
     */
    public LocalDateTime getCheckinDateTime(){
        return LocalDateTime.of(dateCheckin,timeCheckin);}

    /**
     * ดึงข้อมูลวัน-เวลาเช็คเอาท์ในรูปแบบ LocalDateTime
     * @return LocalDateTime ของวัน-เวลาเช็คเอาท์ 
     */
    public LocalDateTime getCheckoutDateTime(){
        return LocalDateTime.of(dateCheckout,timeCheckout);}

    
    private boolean checkinNotified = false;    //ใช้สำหรับติดตามว่าได้ส่งการแจ้งเตือนเช็คอินแล้วหรือยัง
    private boolean checkoutNotified = false;   //ใช้สำหรับติดตามว่าได้ส่งการแจ้งเตือนเช็คเอาท์แล้วหรือยัง

    /**
     * ตรวจสอบว่าการแจ้งเตือนเช็คอินถูกส่งไปแล้วหรือไม่
     * @return true ถ้าส่งแล้ว, false ถ้ายังไม่ได้ส่ง
     */
    public boolean isCheckinNotified() {
        return checkinNotified;
    }

    /**
     * ตรวจสอบว่าการแจ้งเตือนเช็คเอาท์ถูกส่งไปแล้วหรือไม่
     * @return true ถ้าส่งแล้ว, false ถ้ายังไม่ได้ส่ง
     */
    public boolean isCheckoutNotified() {
        return checkoutNotified;
    }


    //Setter

    /**
     * ตั้งค่าสถานะของการจอง
     * @param status สถานะใหม่ของการจอง
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * ตั้งค่าสถานะการแจ้งเตือนเช็คอิน
     * @param checkinNotified true เพื่อระบุว่าส่งการแจ้งเตือนแล้ว
     */
    public void setCheckinNotified(boolean checkinNotified) {
        this.checkinNotified = checkinNotified;
    }

    /**
     * ตั้งค่าสถานะการแจ้งเตือนเช็คเอาท์
     * @param checkoutNotified true เพื่อระบุว่าส่งการแจ้งเตือนแล้ว
     */
    public void setCheckoutNotified(boolean checkoutNotified) {
        this.checkoutNotified = checkoutNotified;
    }

}
