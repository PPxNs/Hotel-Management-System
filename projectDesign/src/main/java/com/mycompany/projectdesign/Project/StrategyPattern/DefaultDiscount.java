package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * กลยุทธ์การคิดราคาแบบปกติ (ไม่มีส่วนลด)
 */

public class DefaultDiscount implements DiscountStrategy{
    
    @Override
    public double applyDiscount(Room room, Bookings booking, double totalPrice) {
       return totalPrice;
    }
    
}
