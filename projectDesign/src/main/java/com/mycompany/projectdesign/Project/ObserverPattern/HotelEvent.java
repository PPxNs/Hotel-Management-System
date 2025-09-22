package com.mycompany.projectdesign.Project.ObserverPattern;
/**
     * interface สำหรับกำหนด Evet ในการแจ้งเตือน
     */

import java.time.LocalDateTime;

public interface HotelEvent {
    LocalDateTime getTimetamp();
}
