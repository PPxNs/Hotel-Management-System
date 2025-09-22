package com.mycompany.projectdesign.Project;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.*;
import com.mycompany.projectdesign.Project.DecoratorPattern.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        System.out.println("--- Hotel System Simulation ---");

        // SETUP ฐานข้อมูลพื้นฐาน
        RoomRepository roomRepo = new RoomRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        DepositFactory depositFactory = new DepositFactory();
        
        HotelNotifier notifier = new HotelNotifier();
        
        // สร้าง Observer
        HotelObserver billPrinter = new BillObserver();
        HotelObserver staffNotifier = new MissedCheckoutObserver();
        
        notifier.register(billPrinter);
        notifier.register(staffNotifier);

        // สร้างห้องพัก
        Room roomB078 = new Room("B078", "Double room", 3000, "empty", "img1.png", 4, Arrays.asList("WiFi", "TV", "AirConditioner"));
        Room roomB099 = new Room("B099", "Suite", 5000, "empty", "img2.png", 2, Arrays.asList("TV", "WiFi"));
        roomRepo.addRoom(roomB078);
        roomRepo.addRoom(roomB099);
        roomRepo.saveRoomToCSV();

        // เพิ่มข้อมูลลูกค้า

        // สร้างข้อมูลลูกค้า ที่จองห้องล่วงหน้า (ใช้ตรวจโปรโมชัน)
        Customer customerAlice = new Customer(
            "2234567891234", "Alice", "Smith", "alice.smith@gmail.com", "0911111111", "Female", "Thailand", "Nakhon Pathom", "123/7",
            "2025-09-21", "15:00", "2025-09-22", "11:00", "2025-08-11", "09:15", "Checked-out", "LUNA00000024");
        
        // สร้างข้อมูลลูกค้า Bob ที่มีเวลาเช็คเอาท์ "อีก 3 นาทีข้างหน้า"
        //String checkoutDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //String checkoutTime = LocalTime.now().plusMinutes(3).format(DateTimeFormatter.ofPattern("HH:mm"));
        Customer customerBob = new Customer(
            "987654321156", "Bob", "Johnson", "bob.j@gmail.com", "0987654321", "Male", "Thailand", "Bangkok", "456/8",
            "2025-09-21", "14:00", "2025-09-20", "15:50", "2025-09-20", "18:32",
            "Confirmed", "LUNA00000086");


        // บันทึกลูกค้าลง Repository ในฐานข้อมูล
        customerRepo.addCustomer(roomB078.getNumberRoom(), customerAlice);
        customerRepo.addCustomer(roomB099.getNumberRoom(), customerBob);
        customerRepo.UpdateHistory();
        customerRepo.saveCustomerToCSV();

        // ตรวจสอบการพิมพ์บิล 
        System.out.println("\n--- SCENARIO 1: Bill Check ---");
    
        DepositRoom depositForAlice = depositFactory.createSimpDepositRoom(roomB078);
        depositForAlice = new MealDecorator(depositForAlice, 2);
        depositForAlice = new PickupServiceDecorator(depositForAlice);

        // สร้าง Event ของบิล
        BillEvent billEvent = new BillEvent(roomB078, customerAlice, depositForAlice, LocalDateTime.now());
        
        // สั่งให้ Notifier ส่งข่าว
        notifier.notifyObserver(billEvent);

        
        // ตรวจสอบการเช็คเอาท์อัตโนมัติ แจ้งเตือนพนักงาน
        System.out.println("\n--- SCENARIO 2: System's Automated Checkout Check ---");
        simulateSystemCheckoutCheck(notifier, customerRepo, roomRepo);
    }

    /**
     * วนตรวจสอบลูกค้าทั้งหมดในระบบว่ามีใครใกล้ถึงเวลาเช็คเอาท์แล้วหรือยัง
     */
    public static void simulateSystemCheckoutCheck(HotelNotifier notifier, CustomerRepository customerRepo, RoomRepository roomRepo) {
        System.out.println("System is now checking all rooms for imminent checkouts...");
        
        // ดึงข้อมูลลูกค้าทั้งหมดในระบบออกมา
        Map<String, List<Customer>> allCustomersMap = customerRepo.getMapCustomer();
        
        // วนลูปทุกห้องที่มีลูกค้า
        for (Map.Entry<String, List<Customer>> entry : allCustomersMap.entrySet()) {
            String roomNumber = entry.getKey();
            List<Customer> customersInRoom = entry.getValue();
            
            // วนลูปเช็คลูกค้าทุกคนในห้องนั้นๆ
            for (Customer customer : customersInRoom) {
                // สนใจเฉพาะลูกค้าที่ยังไม่ได้เช็คเอาท์
                if (customer.getStatusCustomer().equalsIgnoreCase("Confirmed")) {
                    
                    // ดึงข้อมูลลูกค้าและห้องจาก Repository เพื่อสร้าง Event 
                    Room room = roomRepo.getRoom(roomNumber); 
                    if (room != null) {
                        // สร้าง Event สำหรับการแจ้งเตือน
                        MissedCheckoutEvent alertEvent = new MissedCheckoutEvent(room, customer, LocalDateTime.now());
                        // ส่ง Event ให้ Notifier ไปจัดการต่อ
                        notifier.notifyObserver(alertEvent);
                    }
                }
            }
        }
    }
}