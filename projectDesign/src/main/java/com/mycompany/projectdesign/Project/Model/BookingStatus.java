package com.mycompany.projectdesign.Project.Model;

/**
 * สถานะทั้งหมดของการจอง (Booking)
 * แสดงถึงสถานะของการจองแต่ละรายการในระบบ
 */

public enum BookingStatus {
    CONFIRMED,      // ยืนยันการจองแล้ว
    CHECKED_IN,     // แขกเข้าพักแล้ว
    CHECKED_OUT,    // แขกเช็คเอาท์แล้ว
    CANCELLED       // การจองถูกยกเลิก
}
