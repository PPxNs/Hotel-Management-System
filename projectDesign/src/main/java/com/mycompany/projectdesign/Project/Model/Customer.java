package com.mycompany.projectdesign.Project.Model;

/**
 * เป็นคลาส (Model class) ที่ใช้ในการเก็บข้อมูลส่วนตัวของลูกค้า 
 * ทำหน้าที่เป็นโครงสร้างข้อมูลเพื่อให้คลาสอื่น ๆ ของระบบสามารถเข้าถึงและจัดการข้อมูลของห้องพักได้
 */

public class Customer {
    private final String idCard ;       // ตัวแปรเก็บข้อมูลเลขบัตรประชาชนที่ไม่สามารถเปลี่ยนแปลงได้ (Immutable) จีงใช้ final
    private String firstnameCustomer ;  // ตัวแปรเก็บข้อมูลชื่อจริงลูกค้า
    private String lastnameCustomer ;   // ตัวแปรเก็บข้อมูลนามสกุลลูกค้า
    
    //เพิ่มข้อมูลส่วนตัวของลูกค้า
    private String email;   //ตัวแปรเก็บข้อมูลอีเมลสำหรับติดต่อลูกค้า
    private String phone;   //ตัวแปรเก็บข้อมูลหมายเลขโทรศัพท์สำหรับติดต่อลูกค้า
    private String gender;  //ตัวแปรเก็บข้อมูลเพศของลูกค้า
    private String country; //ตัวแปรเก็บข้อมูลประเทศของลูกค้า 
    private String city;    //ตัวแปรเก็บข้อมูลเมืองหรือจังหวัดของลูกค้า
    private String address; //ตัวแปรเก็บข้อมูลที่อยู่ของลูกค้า


    /**
     * Constructor สำหรับสร้าง Object Customer พร้อมกำหนดข้อมูลที่จำเป็นทั้งหมด
     *
     * @param idCard             หมายเลขบัตรประชาชน 
     * @param firstnameCustomer  ชื่อจริง
     * @param lastnameCustomer   นามสกุล
     * @param email              อีเมล
     * @param phone              หมายเลขโทรศัพท์
     * @param gender             เพศ
     * @param country            ประเทศ
     * @param city               เมือง/จังหวัด
     * @param address            ที่อยู่
     */
    public Customer(String idCard, String firstnameCustomer,String lastnameCustomer, String email,
                    String phone, String gender, String country, String city , String address){

        //เก็บตัวภายนอกที่รับเข้ามา ใน this.___ ซึ่งก็คือตัวแปรจริงของคลาสนี้
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


    // getter// พวกด้านล่างนี้มีไว้ดึงค่าที่เก็บในตัวแปรดังกล่าวในระบบอื่นสามารถเรียกใช้ได้
    
    /**
     * ดึงข้อมูลหมายเลขบัตรประชาชน
     * @return หมายเลขบัตรประชาชน (String)
     */
    public String getidCard(){
        return idCard;
    }

    /**
     * ดึงข้อมูลชื่อจริงของลูกค้า
     * @return ชื่อจริง (String)
     */
    public String getFirstnameCustomer(){
        return firstnameCustomer;
    }

    /**
     * ดึงข้อมูลนามสกุลของลูกค้า
     * @return นามสกุล (String)
     */
    public String getLastnameCustomer(){
        return lastnameCustomer;
    }

    /**
     * ดึงข้อมูลชื่อ-นามสกุลเต็มของลูกค้า
     * @return ชื่อจริง + นามสกุล
     */
    public String getFullName(){
        return firstnameCustomer + " " + lastnameCustomer ;
    }

    /**
     * ดึงข้อมูลอีเมลของลูกค้า
     * @return อีเมล (String)
     */
    public String getEmail(){
        return email;
    }

    /**
     * ดึงข้อมูลหมายเลขโทรศัพท์ของลูกค้า
     * @return หมายเลขโทรศัพท์ (String)
     */
    public String getPhone(){
        return phone;
    }

    /**
     * ดึงข้อมูลเพศของลูกค้า
     * @return เพศ (String)
     */
    public String getGender(){
        return gender;
    }

    /**
     * ดึงข้อมูลประเทศของลูกค้า
     * @return ประเทศ (String)
     */
    public String getCountry(){
        return country;
    }

    /**
     * ดึงข้อมูลเมือง/จังหวัดของลูกค้า
     * @return เมือง/จังหวัด (String)
     */
    public String getCity(){
        return city;
    }

     /**
     * ดึงข้อมูลที่อยู่ของลูกค้า
     * @return ที่อยู่ (String)
     */
    public String getAddress(){
        return address;
    }

    //setter
    /**
     * ตั้งค่าหรือเปลี่ยนแปลงชื่อจริงของลูกค้า
     * @param firstnameCustomer ชื่อจริงใหม่
     */
    public void setFirstnameCustomer(String firstnameCustomer){
        this.firstnameCustomer = firstnameCustomer;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงนามสกุลของลูกค้า
     * @param lastnameCustomer นามสกุลใหม่
     */
    public void setLastnameCustomer(String lastnameCustomer){
        this.lastnameCustomer = lastnameCustomer;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงอีเมลของลูกค้า
     * @param email อีเมลใหม่
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงหมายเลขโทรศัพท์ของลูกค้า
     * @param phone หมายเลขโทรศัพท์ใหม่
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงเพศของลูกค้า
     * @param gender เพศใหม่
     */
    public void setGender(String gender){
        this.gender = gender;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงประเทศของลูกค้า
     * @param country ประเทศใหม่
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงเมือง/จังหวัดของลูกค้า
     * @param city เมือง/จังหวัดใหม่
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลงที่อยู่ของลูกค้า
     * @param address ที่อยู่ใหม่
     */
    public void setAddress(String address){
        this.address = address;
    }
}

    