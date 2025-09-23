package com.mycompany.projectdesign.Project.Model;

/*
 * เป็นคลาสที่ใช้ในการเก็บข้อมูลของลูกค้า // กำหนดให้คลาสนี้เป็นเก็บ properties เป็น โมเดลเอาไว้ ให้ระบบอื่นเข้าถึงได้
 */

public class Customer {
    private final String idCard ; // เก็บข้อมูลเลขบัตรประชาชน
    private final String firstnameCustomer ; // เก็บข้อมูลชื่อจริงลูกค้า
    private final String lastnameCustomer ; // เก็บข้อมูลนามสกุลลูกค้า
    
    //เพิ่มข้อมูลส่วนตัวของลูกค้า
    private final String email;
    private final String phone;
    private final String gender;
    private final String country;
    private final String city;
    private final String address;

    //constuctor รับค่าข้อมูลมาเก็บในตัวแปร เหมือนเข้ามาแล้วไม่สามารถแก้ไขเปลี่ยนแปลงเองได้ เนื่องจากเราเก็บเป็น final
    public Customer(String idCard, String firstnameCustomer,String lastnameCustomer, String email,
                    String phone, String gender, String country, String city , String address){

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