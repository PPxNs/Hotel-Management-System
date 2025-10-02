package com.mycompany.projectdesign.Project.StrategyPattern;

import com.mycompany.projectdesign.Project.Model.*;

/**
 * ทำหน้าที่เป็น Factory หรือ Selector สำหรับเลือกกลยุทธ์ส่วนลด (DiscountStrategy) ที่เหมาะสม
 */
public class DiscountSelector {

    /**
     * เมธอดแบบ static สำหรับเลือกและสร้างอินสแตนซ์ของ DiscountStrategy ที่เหมาะสม
     * @param room    ออบเจกต์ของห้องพักที่เกี่ยวข้องกับการจอง (เพื่อให้ข้อมูลประกอบในอนาคต เช่น โปรโมชั่นสำหรับห้องสวีท)
     * @param booking ออบเจกต์ของการจอง ซึ่งใช้ข้อมูลเช่น จำนวนคืนที่เข้าพัก หรือ จำนวนวันที่จองล่วงหน้า
     * @return อินสแตนซ์ของ DiscountStrategy ที่ถูกเลือกตามเงื่อนไข ดังนี้:
     *          - StayMoreDiscount: หากจำนวนคืนที่เข้าพัก (getDateStay) ตั้งแต่ 2 คืนขึ้นไป
     *          - EarlyBirdDiscount: หากเงื่อนไขแรกไม่ตรง และจองล่วงหน้า (getDaysBeforeCheckin) ตั้งแต่ 30 วันขึ้นไป
     *          - DefaultDiscount: หากไม่เข้าเงื่อนไขใด ๆ เลย (เป็นค่าเริ่มต้น)
     */
    public static DiscountStrategy getStrategy(Room room, Bookings booking) {
        
        if (booking.getDateStay() >= 2) {
            return new StayMoreDiscount();
        } else if (booking.getDaysBeforeCheckin() >= 30) {
            return new EarlyBirdDiscount();
        }
        return new DefaultDiscount();
    }
}
