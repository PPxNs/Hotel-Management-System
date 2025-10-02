package com.mycompany.projectdesign.Project.StrategyPattern;
import com.mycompany.projectdesign.Project.Model.*;

/*
 * คำนวณราคาและส่วนลดห้องพัก ไม่รวมมัดจำและบริการเสริม
 */
public class HotelCalculator {

    /**
     * คำนวณราคาสุดท้ายของห้องพักโดยใช้กลยุทธ์ส่วนลดที่ส่งเข้ามา
     * @param room     ออบเจกต์ของห้องพัก เพื่อดึงราคาพื้นฐาน
     * @param booking  ออบเจกต์ของการจอง เพื่อใช้เป็นเงื่อนไขในการคำนวณส่วนลด
     * @param strategy ออบเจกต์ของกลยุทธ์ส่วนลด (เช่น EarlyBirdDiscount, DefaultDiscount) ที่จะถูกนำมาใช้
     * @return ราคาห้องพักสุทธิ (double) หลังจากหักส่วนลดตามกลยุทธ์แล้ว
     */
    public double calculateFinalPrice(Room room, Bookings booking, DiscountStrategy strategy){
        // ดึงราคาพื้นฐานของห้องพัก
        double roomPrice = room.getPrice();
    
        // ส่งต่อการคำนวณทั้งหมดไปให้ strategy object ที่ได้รับมาเป็นคนจัดการ
        return strategy.applyDiscount(room, booking, roomPrice);
    }
}
