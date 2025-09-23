package com.mycompany.projectdesign.Project.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Bookings {
    private final Room room;
    private final Customer customer;
    private final String bookingID; // เก็บหมายเลขจอง
    private final LocalDate dateBooking ; // เก็บวันที่จอง
    private final LocalDate dateCheckin;  // เก็บวันเข้าพัก _/_/_ เก็บเป็น คศ
    private final LocalDate dateCheckout; // เก็บวันออก _/_/_ เก็บเป็น คศ
    private final LocalTime timeCheckin; // เก็บเวลาเข้าพัก ชม:นาที
    private final LocalTime timeCheckout; // เก็บเวลาออก ชม:นาที
    private final LocalTime timeBooking; // เก็บเวลาจอง ชม:นาที
    private BookingStatus status ;// สถานะการมายืนยันการจอง หรือการใช้บริการ
   
    public Bookings(Room room, Customer customer, String bookingID, LocalDate datecheckin, LocalTime timeCheckin, LocalDate datecheckout, LocalTime timeCheckout,
                    LocalDate datebooking , LocalTime timeBooking){

        //อันนี้คือ จะแปลงจาก String เป็น LocalDate เพราะว่า csv มันเก็บเป็น string แต่ถ้าจะใช้ในระบบก็ต้องแปลง โดยเอาค่าฝั่งซ้ายจัดในรูปแบบของฝั่งด้านขวา แล้วแปลงด datatype เป็น LocalDate
        //หมายเหตุ LocalDate คือของวัน LocalTime คือของเวลา
        this.room = room;
        this.customer = customer;
        this.bookingID = bookingID;
        this.dateCheckin = datecheckin; 
        this.timeCheckin = timeCheckin;
        this.dateCheckout = datecheckout;
        this.timeCheckout = timeCheckout;
        this.dateBooking = datebooking;
        this.timeBooking = timeBooking;
        this.status = BookingStatus.CONFIRMED;
    }

    public Room getRoom(){
        return room;
    }

    public Customer getCustomer(){
        return customer;
    }
    
    public LocalDate getDateCheckin(){
        return dateCheckin ;
    }

    public LocalDate getDateCheckout(){
        return dateCheckout;
    }

    public LocalDate getDateBooking(){
        return dateBooking;
    }

    public BookingStatus getStatusCustomer(){
        return status;
    }

        public LocalTime getTimeCheckin(){
        return timeCheckin;
    }

    public LocalTime getTimeCheckout(){
        return timeCheckout;
    }

    public LocalTime getTimeBooking(){
        return timeBooking;
    }  

    public String getCheckin(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateCheckin.atTime(timeCheckin).format(df);

    }

    public String getCheckOut(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateCheckout.atTime(timeCheckout).format(df);

    }

    public String getBooking(){
       DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateBooking.atTime(timeBooking).format(df);

    }

    public String getBookingID(){
        return bookingID;
    }

    public BookingStatus getStatus(){
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    //ใช้ดึงข้อมูลว่าจองก่อนเข้าพักจริงกี่วัน เนื่องจากเรามีตัวโปรโมชันดังนั้นจึงจำเป็นต้องมีการดึงค่าต่อไปนี้ ไปใช้ในการคำนวณ
    public int getDaysBeforeCheckin(){
        return (int) ChronoUnit.DAYS.between(dateBooking, dateCheckin);
    }

    //ใช้ดึงข้อมูลว่าพักกี่คืน เนื่องจากเรามีตัวโปรโมชันดังนั้นจึงจำเป็นต้องมีการดึงค่าต่อไปนี้ ไปใช้ในการคำนวณ
    public int getDateStay(){
        return (int) ChronoUnit.DAYS.between(dateCheckin, dateCheckout);
    }
}
