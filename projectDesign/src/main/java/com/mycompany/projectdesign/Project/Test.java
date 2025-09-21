package com.mycompany.projectdesign.Project;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.*;

import java.util.Arrays;

import com.mycompany.projectdesign.Project.DecoratorPattern.*;
public class Test {

    // ยังมีความไม่สมเหตุสมผลการเชื่อมของห้องและลูกต้า //ขอคิดวิธี

    public static void main(String[] args) {
        System.out.println("--- Hotel System Simulation ---");
        
        RoomRepository roomRepo = new RoomRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        DepositFactory depositFactory = new DepositFactory();

        // สร้างห้อง
        Room K1 = new Room("B078", "Double room", 3000, "empty","img1.png",4,Arrays.asList("WiFi","TV","AirConditioner"));
        roomRepo.addRoom(K1);
        Room K2 = new Room("B099", "Suite", 5000, "booking","img1.png",2,Arrays.asList("TV", "WiFi"));
        roomRepo.addRoom(K2);

        //เซฟข้อมูลของห้องลง csv 
        roomRepo.saveRoomToCSV(); 
        System.out.println("Rooms loaded: " + roomRepo.getAllRooms().size());

    
        // สร้างลูกค้า
        Customer customer1 = new Customer(
        "2234567891234", "Alice", "Smith", "alice.smith@gmail.com", "0911111111", "Female", "Thailand","Naconpatom","123/7",
        "2025-09-13", "15:00", "2025-09-14", "11:00", "2025-08-11", "09:15", "Confirmed","LUNA00000024");
        
        Customer customer2 = new Customer(
             "987654321156", "Jane", "Smith","JJ.Doe@gmail.com", "0987654321", "Female","Thailand","Naconpatom","123/7",
            "2025-09-11","14:00", "2025-09-12","14:00", "2025-08-10", "18:32",
            "Confirmed","LUNA00000086");
        
        //สร้างโรงงานก่อนห่อ เป็นตัวของประเภทห้อง
        DepositRoom depositType = depositFactory.createSimpDepositRoom(K1);
            
        // "ห่อ" ทับด้วยบริการประกันของสูญหาย
        depositType = new CleaningRoomDecorator(depositType);

        // "ห่อ" ด้วยบริการมื้ออาหาร
        depositType = new MealDecorator(depositType, 1);

        // "ห่อ" ด้วยบริการรับส่ง
        depositType = new PickupServiceDecorator(depositType);


        // เก็บข้อมูลลูกค้า
        customerRepo.addCustomer(K1.getNumberRoom(),customer1);
        customerRepo.addCustomer(K1.getNumberRoom(),customer2);
        customerRepo.saveCustomerToCSV();
        customerRepo.UpdateHistory();

        //ลองอัพเดทข้อมูล
        Customer updatedCustomer = new Customer(
        "987654321156", "Mina", "Smith","JJ.Doe@gmail.com", "0987654321", "Female","Thailand","Naconpatom","123/7",
    "2025-09-11","14:00", "2025-09-12","14:00", "2025-08-10", "18:32","Check out", "LUNA00000123");
        customerRepo.replaceCustomer(K1.getNumberRoom(), "987654321156", updatedCustomer);
        customerRepo.UpdateHistory();
        customerRepo.saveCustomerToCSV();

        // เรียกผ่าน repository
        customerRepo.replaceCustomer(K1.getNumberRoom(), "987654321156", updatedCustomer);

        // Observer
        BillObserver billObserver = new BillObserver(customerRepo);

        // การแจ้งเตือนออกบิล
        billObserver.update(K1, customer1, depositType);
    }
}
