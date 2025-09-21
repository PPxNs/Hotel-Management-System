package com.mycompany.projectdesign.Project.ObserverPattern;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.StrategyPattern.*;

public class BillObserver implements RoomObserver{
    
    private final CustomerRepository customerRepository;

    // รับ CustomerRepository มาตอนสร้าง
    public BillObserver(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
  
    @Override
    public void update(Room room, Customer customer,DepositRoom depositRoom) {
        BillPrint(room , customer,depositRoom);
    }

    public void BillPrint(Room room, Customer customer, DepositRoom depositRoom){
        
        System.out.println("===== HOTEL BILL =====");
        System.out.println("Number Room: " + room.getNumberRoom());
        System.out.println("Type: " + room.getType());
        System.out.println("Booking: " + customer.getDateBooking() + " " + customer.getTimeBooking());


        //อันนี้ไม่ผ่านการเห็นด้วย
        //กรองลูกค้าที่พักห้องนี้จาก Hash (วันเช็คอินตรง+วันจองตรง)
        /*List<Customer> customersInRoom = customerRepository.getCustomersInRoom(room.getNumberRoom());


        
         
        List<Customer> stayingCustomers = new ArrayList<>();
        for(Customer c : customersInRoom){
            String setCheckinBooking =  c.getDateCheckin() + " " + c.getTimeCheckin();
            if (setCheckinBooking.equals(customer.getDateCheckin() + " " + c.getTimeCheckin())) {
                stayingCustomers.add(c); //ถ้าข้อมูลตรงเก็บไว้ในลิสก่อน
            }
        }

        if (!stayingCustomers.isEmpty()) {

            //ลูกค้าที่ 1 ที่อยู่ห้องนี้
            Customer mainGuest = stayingCustomers.get(0);
            System.out.println("Main Guest: " + mainGuest.getFullName());
            
            //กรณีมากกว่า 1 คน
            if (stayingCustomers.size()> 1) {
                System.out.println("Guest: ");
            for(int i = 1;i < stayingCustomers.size() ; i++){
                Customer g = stayingCustomers.get(i);
                System.out.println(g.getFullName()+"\n"); 
            }
            }
        } else {
            System.out.println("No valid guests found for this booking/check-in.");
        }*/

        System.out.println("Check-in: " + customer.getDateCheckin() + " " + customer.getTimeCheckin()) ;
        System.out.println("Check-out: " + customer.getDateCheckout() + " " + customer.getTimeCheckout());
        System.out.println("----------------------");


        System.out.println("Room Price: " + room.getPrice()); // อยากให้แสดงราคาห้องที่ยังไม่มีการบวกมัดจำ //ผ่าน

        //ส่วนลดของห้อง เราจะลดให้แค่ค่าห้องส่วนค่าอื่น ๆ คงเดิม
        // จะดึงอย่างไรดีเมื่อเราไม่ต้องการที่จะให้ส่วนลดยุ่งกับฐานระบบได้
        //ค่อยหาวิธี
        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(room, customer);
        HotelCalculator calculator = new HotelCalculator();
        double finalPriceRoom = calculator.calculateFinalPrice(room, customer, discountStrategy);
        double discount = room.getPrice() - finalPriceRoom ;
        System.out.printf("Discount: %.2f\n" ,discount);


        // เราจะทำยังไงที่จะดึงข้อมูลหลังห่อ แสดงราคาของมัดจำ + service 
        //อยากจะเพิ่มเติมส่วนของ ราคาเฉพาะอย่าง

        System.out.println("Deposit & Services Add-on: " );
        System.out.println(" " + depositRoom.getDescription()); //ไม่ผ่าน
        System.out.printf("Deposit & Services Add-on Price: %.2f\n", depositRoom.getCost());

        //บริการเสริมน่าจะตรวจสอบได้ว่ามีการห่อมั้ย ถ้าไม่มี จะให้เขียนว่า 0.0

        System.out.printf("Total Price: %.2f\n", room.getPrice() + depositRoom.getCost()-discount);  // ไม่ผ่าน
        System.out.println("Payment Method: Cash Payment");
        System.out.println("======================\n");

    }    
}
