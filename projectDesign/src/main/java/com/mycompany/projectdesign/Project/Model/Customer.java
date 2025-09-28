package com.mycompany.projectdesign.Project.Model;

/*
 * เป็นคลาสที่ใช้ในการเก็บข้อมูลของลูกค้า // กำหนดให้คลาสนี้เป็นเก็บ properties เป็น โมเดลเอาไว้ ให้ระบบอื่นเข้าถึงได้
 */

public class Customer {
    private final String idCard ; // เก็บข้อมูลเลขบัตรประชาชน
    private String firstnameCustomer ; // เก็บข้อมูลชื่อจริงลูกค้า
    private String lastnameCustomer ; // เก็บข้อมูลนามสกุลลูกค้า
    
    //เพิ่มข้อมูลส่วนตัวของลูกค้า
    private String email;
    private String phone;
    private String gender;
    private String country;
    private String city;
    private String address;

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

    public void setFirstnameCustomer(String firstnameCustomer){
        this.firstnameCustomer = firstnameCustomer;
    }

    public void setLastnameCustomer(String lastnameCustomer){
        this.lastnameCustomer = lastnameCustomer;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setAddress(String address){
        this.address = address;
    }
}

    