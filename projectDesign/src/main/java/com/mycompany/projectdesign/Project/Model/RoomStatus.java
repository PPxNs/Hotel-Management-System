package com.mycompany.projectdesign.Project.Model;

/**
 * สถานะทั้งหมดของห้องพัก (Room)
 * ใช้เพื่อจัดการและแสดงสถานะปัจจุบันของห้องในระบบ
 */

public enum RoomStatus {
    AVAILABLE,       // ว่าง 
    OCCUPIED,        // ไม่ว่าง 
    CLEANING,        // กำลังทำความสะอาด
    MAINTENANCE      // ปิดปรับปรุง
}
