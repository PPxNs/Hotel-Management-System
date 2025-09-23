package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/**
 * Interface สำหรับกลยุทธ์ส่วนลด
 */
// น่าจะมีความหยืดหยุ่นกว่านี้ที่ แอดมินสามารถยกเลิกส่วนลดหรือโปรได้
public interface DiscountStrategy{
    public double applyDiscount(Room room, Bookings booking, double totalPrice);
}
