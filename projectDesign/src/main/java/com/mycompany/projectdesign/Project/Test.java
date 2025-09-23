package com.mycompany.projectdesign.Project;

import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.*;
import com.mycompany.projectdesign.Project.DecoratorPattern.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        System.out.println("--- Hotel System Simulation ---");

        // SETUP: ข้อมูลฐานข้อมูล
        // สร้าง Repository ทั้งหมด
        RoomRepository roomRepo = RoomRepository.getInstance();
        CustomerRepository customerRepo = new CustomerRepository();
        BookingRepository bookingRepo = new BookingRepository(); 
        DepositFactory depositFactory = new DepositFactory();
        
        // สร้าง Notifier และลง Observers
        HotelNotifier notifier = new HotelNotifier();
        HotelObserver billPrinter = new BillObserver(); 
        HotelObserver missedCheckoutObserver = new MissedCheckoutObserver();
        notifier.register(billPrinter);
        notifier.register(missedCheckoutObserver);

        // สร้างและเพิ่มห้องพัก
        Room roomB078 = new Room("B078", "Double room", 3000, "img1.png", 4, Arrays.asList("WiFi", "TV"), RoomStatus.AVAILABLE);
        Room roomB099 = new Room("B099", "Suite", 5000, "img2.png", 2, Arrays.asList("TV", "WiFi"), RoomStatus.AVAILABLE);
        roomRepo.addRoom(roomB078);
        roomRepo.addRoom(roomB099);
        roomRepo.saveRoomToCSV();

        // สร้างและเพิ่มข้อมูลลูกค้า 
        Customer customerAlice = new Customer("2234567891234", "Alice", "Smith", "alice.smith@gmail.com", "0911111111","Female", "Thailand", "Nakhon Pathom", "123/7");
        Customer customerBob = new Customer("987654321156", "Bob", "Johnson", "bob.j@gmail.com", "0987654321", "Male", "Thailand", "Bangkok", "456/8");
        customerRepo.addCustomer(roomB078.getNumberRoom(),customerAlice);
        customerRepo.addCustomer(roomB099.getNumberRoom(),customerBob);
        customerRepo.saveCustomerToCSV();

        // สร้าง Booking
        Bookings bookingForAlice = new Bookings(roomB078, customerAlice, "BK001", 
            LocalDate.of(2025, 9, 21), LocalTime.of(15, 0), 
            LocalDate.of(2025, 9, 22), LocalTime.of(11, 0),
            LocalDate.of(2025, 8, 11), LocalTime.of(9, 15));
        bookingForAlice.setStatus(BookingStatus.CHECKED_OUT); 

        // สร้างการจองของ Bob จะให้เป็นคนที่ใกล้เวลา checkout 
        Bookings bookingForBob = new Bookings(roomB099 ,customerBob,"BK002" , 
            LocalDate.of(2025, 9, 21), LocalTime.of(15, 0), 
            LocalDate.of(2025, 9, 23), LocalTime.of(21, 45),
            LocalDate.of(2025, 8, 11), LocalTime.of(9, 15));
        bookingForBob.setStatus(BookingStatus.CHECKED_IN); 

        // เพิ่มการจองลงใน BookingRepository
        bookingRepo.addBooking(bookingForAlice);
        bookingRepo.addBooking(bookingForBob);
        bookingRepo.saveBookingToCSV();

        // พิมพ์บิลสำหรับการจองของ Alice
        System.out.println("\n--- SCENARIO 1: Bill Check ---");
        DepositRoom depositForAlice = depositFactory.createSimpDepositRoom(roomB078);
        depositForAlice = new MealDecorator(depositForAlice, 2);
        depositForAlice = new PickupServiceDecorator(depositForAlice);
        
        // สร้าง Event จากข้อมูลการจองของ Alice
        BillEvent billEvent = new BillEvent(bookingForAlice.getRoom(), bookingForAlice, depositForAlice, LocalDateTime.now());
        
        // สั่งให้ Notifier ส่งข่าว
        notifier.notifyObserver(billEvent);

        // ระบบตรวจสอบการเช็คเอาท์อัตโนมัติ 
        System.out.println("\n--- SCENARIO 2: System's Automated Checkout Check ---");
        simulateSystemCheckoutCheck(notifier, bookingRepo);
    }

    public static void simulateSystemCheckoutCheck(HotelNotifier notifier, BookingRepository bookingRepo) {
        System.out.println("System is now checking all bookings for imminent checkouts...");
        
        // ดึงข้อมูล "การจอง" ทั้งหมดในระบบออกมา
        List<Bookings> allBookings = bookingRepo.getAllBookings();
        
        // วนลูปทุกการจอง
        for (Bookings booking : allBookings) {
            // เราสนใจเฉพาะการจองที่แขกกำลังเข้าพักอยู่ (CHECKED_IN)
            if (booking.getStatus() == BookingStatus.CHECKED_IN) {
                
                // ดึงข้อมูลลูกค้าและห้องจาก "การจอง" นั้นๆ เพื่อสร้าง Event
                Customer customer = booking.getCustomer();
                Room room = booking.getRoom();
                
                MissedCheckoutEvent alertEvent = new MissedCheckoutEvent(room, customer, booking,LocalDateTime.now());
                
                // ส่ง Event ให้ Notifier จัดการต่อ
                notifier.notifyObserver(alertEvent);
            }
        }
    }
}