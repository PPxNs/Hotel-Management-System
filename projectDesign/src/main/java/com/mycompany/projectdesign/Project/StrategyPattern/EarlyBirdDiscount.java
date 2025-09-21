package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * กลยุทธ์ส่วนลดค่าห้องพัก 15% เมื่อจองล่วงหน้า 30 วัน และ 25% ค่าห้องพัก เมื่อจองล่วงหน้า 60 วัน
 */


public class EarlyBirdDiscount implements DiscountStrategy{


    @Override
    public double applyDiscount(Room room, Customer customer, double totalPrice) {
        if (customer.getDaysBeforeCheckin()>= 60) {
            return totalPrice * 0.75; // ลด 25 %

        }else if (customer.getDaysBeforeCheckin()>= 30) {
            return totalPrice * 0.85; // ลด 15 %

        } else return totalPrice;
    }
    
}
