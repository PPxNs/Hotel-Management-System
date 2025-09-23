package com.mycompany.projectdesign.Project.Model;
public enum BookingStatus {
    PENDING,        // รอการยืนยัน (รอชำระเงิน)
    CONFIRMED,      // ยืนยันการจองแล้ว
    CHECKED_IN,     // แขกเข้าพักแล้ว
    CHECKED_OUT,    // แขกเช็คเอาท์แล้ว
    CANCELLED       // การจองถูกยกเลิก
}
