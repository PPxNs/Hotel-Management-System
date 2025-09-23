package com.mycompany.projectdesign.Project.StrategyPattern;

import com.mycompany.projectdesign.Project.Model.*;

/**
 * กลยุทธ์ส่วนลดค่าห้องพัก 10% เมื่อเข้าพัก 2 คืน และ 20% เมื่อเข้าพัก 3 คืนขึ้นไป
 */
public class StayMoreDiscount implements DiscountStrategy {

    
    @Override
    public double applyDiscount(Room room, Bookings booking, double totalPrice) {
        if (booking.getDateStay() >= 3) {
            return totalPrice * 0.8; // ลด 20 %

        }else if (booking.getDateStay() == 2) {
            return totalPrice * 0.9; // ลด 10 %

        } else return totalPrice;
    }
}
