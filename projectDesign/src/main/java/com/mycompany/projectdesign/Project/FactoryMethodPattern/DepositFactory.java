package com.mycompany.projectdesign.Project.FactoryMethodPattern;

import com.mycompany.projectdesign.Project.Model.*;

/**
 * Factory สำหรับสร้างอ็อบเจกต์ Deposit
 */

public class DepositFactory {
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
        throw new IllegalArgumentException("Unknown room type: " + room.getType());
    }
}
