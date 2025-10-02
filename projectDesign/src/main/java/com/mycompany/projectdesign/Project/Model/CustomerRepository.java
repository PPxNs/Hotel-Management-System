package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.util.*;

/**
 * Repository สำหรับจัดการข้อมูลลูกค้าทั้งหมดในระบบ
 * ทำหน้าที่เป็นตัวกลางระหว่างตัวรับข้อมูล/ตัวแสดงผลข้อมูลกับฐานข้อมูล (ในที่นี้คือ CSV ไฟล์)
 * ข้อมูลลูกค้าจะถูกจัดเก็บใน Map โดยใช้รหัสบัตรประชาชน (idCard) เป็น Key หลัก
 */

public class CustomerRepository {

    private static final CustomerRepository instance = new CustomerRepository();
    private HashMap<String, List<Customer>> customersByRoom = new HashMap<>(); //สร้าง hash map มีตัวของ Key: หมายเลขห้อง (String) Value: รายการของลูกค้า (List<Customer>) ที่ผูกกับห้องนั้น
  

    /**
    * Private Constructor เพื่อป้องกันการสร้างอินสแตนซ์จากภายนอก
    * จะทำการโหลดข้อมูลห้องจาก CSV ไฟล์โดยอัตโนมัติเมื่อถูกสร้างครั้งแรก
    */  
    //เปิดให้ใช้ CustomerRepository เดียวทั้งแอป ไม่ให้ new ด้านนอก
    private CustomerRepository(){
        loadCustomerFromCSV();
    }
    
    /**
     * ดึงอินสแตนซ์เดียวของ CustomerRepository
     * @return อินสแตนซ์ของ CustomerRepository
     */
    public static CustomerRepository getInstance(){
        return instance;
    }



    public List<Customer> getCustomersByRoom(String roomNo) {
        return customersByRoom.getOrDefault(roomNo, new ArrayList<>());
    }

    /**
     * ดึงข้อมูลลูกค้าจากรหัสบัตรประชาชน
     * @param idCard รหัสบัตรประชาชนของลูกค้าที่ต้องการค้นหา
     * @return ออบเจกต์ Customer ถ้าพบ, หรือ null ถ้าไม่พบ
     */
    public Customer getCustomerById(String idCard) {
    for (List<Customer> list : customersByRoom.values()) {
        for (Customer c : list) {
            if (c.getidCard().equals(idCard)) {
                return c;
            }
        }
    }
        return null;
    }

    /**
     * เพิ่มข้อมูลลูกค้าเข้าไปในรายการของห้องที่ระบุ
     * หากยังไม่มีรายการสำหรับห้องนั้น จะสร้างรายการใหม่ขึ้นมาก่อน
     * @param roomNo หมายเลขห้องที่จะเพิ่มลูกค้าเข้าไป
     * @param customer ออบเจกต์ลูกค้าที่ต้องการเพิ่ม
     */
    public void addCustomer(String roomNo, Customer customer){
        customersByRoom.putIfAbsent(roomNo, new ArrayList<>());
        customersByRoom.get(roomNo).add(customer);
    }

    /**
     * ดึงโครงสร้างข้อมูลลูกค้าทั้งหมดที่จัดกลุ่มตามห้อง
     * @return Map ที่มี Key เป็นหมายเลขห้อง และ Value เป็นรายการลูกค้า
     */
    public Map<String, List<Customer>> getAllCustomers(){
        return customersByRoom;
    }

    /**
     * ลบข้อมูลลูกค้าทั้งหมดออกจาก hash
     */
    public void removeAllCustomer(){
        customersByRoom.clear();
    }

    /**
     * บันทึกข้อมูลลูกค้าทั้งหมดจาก hash ลงในไฟล์ CSV (เขียนทับไฟล์เดิม)
     * @throws IOException หากเกิดข้อผิดพลาดระหว่างการเขียนไฟล์
     */
    public void saveCustomerToCSV(){
        //กดปิดโปรแกรม จะเรียก method นี้ โหลดข้อมูลกลายเป็น CSV (ไว้ restore ตอนเปิดระบบใหม่)
        File fi = null;
        FileWriter fw = null; 
        BufferedWriter bw = null;
        try {
            fi = new File("File/Customer.csv");
            fw = new FileWriter(fi,false); //flase บันทึกใหม่เป็นไฟล์อัพเดท
            bw = new BufferedWriter(fw);
            //จะวน for เข้าถึงตัว key ทั้งหมด
            for (Map.Entry<String, List<Customer>> entry : customersByRoom.entrySet()) {
                String roomNo = entry.getKey();
                for (Customer customer : entry.getValue()) {
                bw.write(roomNo + ","
                        + customer.getidCard() + ","
                        + customer.getFirstnameCustomer() + "," 
                        + customer.getLastnameCustomer() + "," 
                        + customer.getEmail() + "," 
                        + customer.getPhone() + "," 
                        + customer.getGender() + "," 
                        + customer.getCountry() + "," 
                        + customer.getCity() + "," 
                        + customer.getAddress());
                bw.newLine(); 
                }
            }
        } catch (Exception e) {
            // อาจมีการขึ้นแจ้งเตือนถ้ามีความผิดพลาดแบบ pop up
            System.out.println(e);
        }finally{  //รอปรับ
            try {
                bw.close(); 
                fw.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * บันทึกข้อมูลลูกค้าปัจจุบันลงในไฟล์ประวัติ (ต่อท้ายไฟล์เดิม)
     * @throws IOException หากเกิดข้อผิดพลาดระหว่างการเขียนไฟล์
     */
    public void UpdateHistory(){
        //กดปิดโปรแกรม จะเรียก method นี้ โหลดข้อมูลกลายเป็น CSV (ไว้ restore ตอนเปิดระบบใหม่)
        File fi = new File("File/Customer_History.csv");
        FileWriter fw = null; 
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fi,true); //true เพราะจะบันทึกประวัติการเข้าพักต่อกันเรื่อย ๆ 
            bw = new BufferedWriter(fw);
            //จะวน for เข้าถึงตัว key ทั้งหมด
            
            for (Map.Entry<String, List<Customer>> entry : customersByRoom.entrySet()) {
                String roomNo = entry.getKey();
                for (Customer customer : entry.getValue()) {
                bw.write(roomNo + ","
                        + customer.getidCard() + ","
                        + customer.getFirstnameCustomer() + "," 
                        + customer.getLastnameCustomer() + "," 
                        + customer.getEmail() + "," 
                        + customer.getPhone() + "," 
                        + customer.getGender() + "," 
                        + customer.getCountry() + "," 
                        + customer.getCity() + "," 
                        + customer.getAddress());
                bw.newLine(); 
                }
            
            }
        } catch (Exception e) {
            // อาจมีการขึ้นแจ้งเตือนถ้ามีความผิดพลาดแบบ pop up
            System.out.println(e);
        }finally{  //รอปรับ
            try {
                bw.close(); 
                fw.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
    * โหลดข้อมูลลูกค้าจากไฟล์ CSV เข้าสู่ hash
    * @throws IOException หากเกิดข้อผิดพลาดระหว่างการอ่านไฟล์
    */
    public boolean loadCustomerFromCSV(){
        File fi = new File("File/Customer.csv");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            
            fr = new FileReader(fi);
            br = new BufferedReader(fr);
            String s ;
            while ((s = br.readLine()) != null) {
                String[] parts = s.split(","); //จะทำการสร้างสตริง เพื่อแบ่ง ข้อมูลตาม (,)
                if (parts.length == 10) {
                   String roomNo = parts[0];
                   String idCard = parts[1];
                   String firstnameCustomer = parts[2] ; 
                   String lastnameCustomer = parts[3];
                   String email = parts[4];
                   String phone = parts[5];
                   String gender = parts[6];
                   String country = parts[7];
                   String city = parts[8];
                   String address = parts[9];
            Customer customer = new Customer(idCard, firstnameCustomer,lastnameCustomer, email,
                                             phone, gender, country, city , address);
            this.addCustomer(roomNo, customer);
                } 
            } 
        }
        catch (Exception e) {
            System.out.println();
        }finally { // รอปรับ
            try {
                br.close(); fr.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
            return true;
        }
}



