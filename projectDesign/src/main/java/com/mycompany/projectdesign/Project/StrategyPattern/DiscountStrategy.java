package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * Interface สำหรับกลยุทธ์ส่วนลด
 */

 public interface DiscountStrategy{
    public double applyDiscount(Room room, Bookings booking, double totalPrice);
}
