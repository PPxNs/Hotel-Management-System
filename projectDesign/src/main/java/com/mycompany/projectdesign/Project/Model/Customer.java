package com.mycompany.projectdesign.Project.Model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/*
 * เป็นคลาสที่ใช้ในการเก็บข้อมูลของลูกค้า // กำหนดให้คลาสนี้เป็นเก็บ properties เป็น โมเดลเอาไว้ ให้ระบบอื่นเข้าถึงได้
 */

public class Customer {
    private final String idCard ; // เก็บข้อมูลเลขบัตรประชาชน
    private final String firstnameCustomer ; // เก็บข้อมูลชื่อจริงลูกค้า
    private final String lastnameCustomer ; // เก็บข้อมูลนามสกุลลูกค้า
    private final LocalDate dateCheckin;  // เก็บวันเข้าพัก _/_/_ เก็บเป็น คศ
    private final LocalDate dateCheckout; // เก็บวันออก _/_/_ เก็บเป็น คศ
    private final LocalDate dateBooking ; // เก็บวันที่จอง
    private final String statusCustomer; // สถานะการมายืนยันการจอง หรือการใช้บริการ
    

    //เพิ่มข้อมูลส่วนตัวของลูกค้า
    private final String email;
    private final String phone;
    private final String gender;
    private final String country;
    private final String city;
    private final String address;

    //เพิ่มเวลาจองของวันที่จอง ที่พัก
    private final LocalTime timeCheckin; // เก็บเวลาเข้าพัก ชม:นาที
    private final LocalTime timeCheckout; // เก็บเวลาออก ชม:นาที
    private final LocalTime timeBooking; // เก็บเวลาจอง ชม:นาที
    private final String bookingID; // เก็บหมายเลขจอง //หมายเหตุยังไม่มีการปรับให้ระบบหยืดหยุ่น!!!

    //กำหนดรูปแบบที่จะแปลงของวันและเวลา เนื่องจากตัวของ csv มันเป็น string เลยต้องปรับเปลี่ยนให้เป็นรูปแบบที่ถูกต้อง
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //รอการปรับชื่อที่หยืดหยุ่นกว่านี้!!!
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm"); //รอการปรับชื่อที่หยืดหยุ่นกว่านี้!!!

    //constuctor รับค่าข้อมูลมาเก็บในตัวแปร เหมือนเข้ามาแล้วไม่สามารถแก้ไขเปลี่ยนแปลงเองได้ เนื่องจากเราเก็บเป็น final
    public Customer(String idCard, String firstnameCustomer,String lastnameCustomer, String email,
                    String phone, String gender, String country, String city , String address,
                    String checkin, String timeCheckin, String checkout, String timeCheckout,
                    String booking , String timeBooking, String statusCustomer , String bookingID ){

        //เก็บตัวภายนอกที่รับเข้ามา ใน this.___ ซึ่งก็คือตัวแปรจริง ของคลาสนี้
        this.idCard = idCard;
        this.firstnameCustomer = firstnameCustomer;
        this.lastnameCustomer = lastnameCustomer;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.address = address;
        
        //อันนี้คือ จะแปลงจาก String เป็น LocalDate เพราะว่า csv มันเก็บเป็น string แต่ถ้าจะใช้ในระบบก็ต้องแปลง โดยเอาค่าฝั่งซ้ายจัดในรูปแบบของฝั่งด้านขวา แล้วแปลงด datatype เป็น LocalDate
        //หมายเหตุ LocalDate คือของวัน LocalTime คือของเวลา
        this.dateCheckin =  LocalDate.parse(checkin, formatter); 
        this.timeCheckin = LocalTime.parse(timeCheckin, formatter2);
        this.dateCheckout =  LocalDate.parse(checkout, formatter);
        this.timeCheckout = LocalTime.parse(timeCheckout, formatter2);
        this.dateBooking = LocalDate.parse(booking, formatter);
        this.timeBooking = LocalTime.parse(timeBooking,formatter2);
        this.statusCustomer = statusCustomer;
        this.bookingID = bookingID;
    }


    // พวกด้านล่างนี้มีไว้ดึงค่าที่เก็บในตัวแปรดังกล่าวในระบบอื่นสามารถเรียกใช้ได้
    public String getidCard(){
        return idCard;
    }

    public String getFirstnameCustomer(){
        return firstnameCustomer;
    }

    public String getLastnameCustomer(){
        return lastnameCustomer;
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

    public String getStatusCustomer(){
        return statusCustomer;
    }

    public String getFullName(){
        return firstnameCustomer + " " + lastnameCustomer ;
    }

    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }

    public String getGender(){
        return gender;
    }

    public String getCountry(){
        return country;
    }

    public String getCity(){
        return city;
    }

    public String getAddress(){
        return address;
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
        return dateCheckin +" "+ timeCheckin ;
    }

    public String getCheckOut(){
        return dateCheckout +" "+ timeCheckout;
    }

    public String getBooking(){
        return dateBooking +" "+ timeBooking ;
    }

    public String getBookingID(){
        return bookingID;
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

    //อันนี้ไม่ใช้ แต่เก็บไว้ก่อน จารย์บอกว่าไม่ต้องกำหนดรูปแบบการจ่าย
    /*// ค่อยปรับวิธีการชำระเงินข้อลูกค้าในฐานของมูล CSV
    // ในส่วนข้อฐานลูกค้า รูปแบบการชำระเงินน่าจะเข้ามาในนี้เพื่อจะได้สะดวกในการดึงฐานข้อมูลของลูกค้า
    private PaymentStrategy paymentStrategy;
    public void setPaymentStrategy(PaymentStrategy paymentStrategy){
        this.paymentStrategy = paymentStrategy;
    }

    //อันนี้ให้ customerRepository บันทึกข้อมูลใน csv //ยังไม่จัดการ
    public String getPayment(){
        return paymentStrategy.getName();
    }

    //อันนี้มีให้ตัวของผู้ประกาศใช้งาน
    public PaymentStrategy getPaymentStrategy(){
        return paymentStrategy ;
    }*/