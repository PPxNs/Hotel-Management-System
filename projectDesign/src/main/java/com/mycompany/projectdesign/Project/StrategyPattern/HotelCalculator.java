package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/*
 * คำนวณราคาและส่วนลดห้องพัก ไม่รวมมัดจำและบริการเสริม
 */
public class HotelCalculator {
    public double calculateFinalPrice(Room room, Customer customer, DiscountStrategy strategy){
        double roomPrice = room.getPrice();
    
        return strategy.applyDiscount(room, customer, roomPrice);
    }
}
