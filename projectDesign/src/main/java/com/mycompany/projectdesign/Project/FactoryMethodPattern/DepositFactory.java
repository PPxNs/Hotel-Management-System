package com.mycompany.projectdesign.Project.FactoryMethodPattern;

import com.mycompany.projectdesign.Project.Model.*;

/**
 * ทำหน้าที่เป็นโรงงาน (Factory) สำหรับสร้างออบเจกต์ที่จัดการเรื่องเงินมัดจำ (Deposit)
 */
public class DepositFactory {

    /**
     * สร้างและคืนค่าออบเจกต์ DepositRoom ที่เหมาะสมกับประเภทของห้องพักที่ระบุ
     * @param room ออบเจกต์ของห้องพักที่ต้องการสร้างนโยบายเงินมัดจำ
     * @return อินสแตนซ์ของคลาส DepositRoom เช่น SingleroomType, DoubleroomType
     * @throws IllegalArgumentException หากประเภทของห้อง (room.getType()) ไม่ตรงกับประเภทใดๆ ที่โรงงานรู้จัก
     */
    public DepositRoom createSimpDepositRoom(Room room){
        if ("Double room".equalsIgnoreCase(room.getType())) {
            return new DoubleroomType();

        }else if ("Single room" .equalsIgnoreCase(room.getType())) {
            return new SingleroomType();

        }else if ("Twin room".equalsIgnoreCase(room.getType())) {
            return new TwinroomType();

        }else if ("Suite" .equalsIgnoreCase(room.getType())) {
            return new SuiteType();

        }
        // หากไม่ตรงกับเงื่อนไขใดๆ เลย ให้โยน Exception เพื่อแจ้งว่าประเภทห้องไม่ถูกต้อง
        throw new IllegalArgumentException("Unknown room type: " + room.getType());
    }
}
