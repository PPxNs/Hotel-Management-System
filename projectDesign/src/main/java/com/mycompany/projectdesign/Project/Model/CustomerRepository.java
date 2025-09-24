package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.util.*;

/*
 * เป็นคลังเก็บข้อมูลลูกค้า จะเก็บข้อมูลคล้ายห้องแต่จะใช้ key เป็นตัวหมายเลขห้อง
 */

public class CustomerRepository {

    private static final CustomerRepository instance = new CustomerRepository();
    HashMap<String, Room > allRoomsByKey = new HashMap<String, Room>(); // สร้าง hash map มีตัวของ key เป็น Integer และ value เป็น Room (เริ่มแรกจะใช้ String แต่น่าจะเข้าถึงข้อมูลลำบากขึ้น)
    
    //เปิดให้ใช้ CustomerRepository เดียวทั้งแอป ไม่ให้ new ด้านนอก
    private CustomerRepository(){
        loadCustomerFromCSV();
    }
    
    public static CustomerRepository getInstance(){
        return instance;
    }

    private HashMap<String, List<Customer>> customersByRoom = new HashMap<>();

    public List<Customer> getCustomersByRoom(String roomNo) {
        return customersByRoom.getOrDefault(roomNo, new ArrayList<>());
    }


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

    public void addCustomer(String roomNo, Customer customer){
        customersByRoom.putIfAbsent(roomNo, new ArrayList<>());
        customersByRoom.get(roomNo).add(customer);
    }

    public Map<String, List<Customer>> getAllCustomers(){
        return customersByRoom;
    }

    public void removeAllCustomer(){
        customersByRoom.clear();
    }

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



