package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;

/**
* interface สำหรับกำหนด Evet ในการแจ้งเตือน
*/

public interface HotelEvent {
    LocalDateTime getTimetamp();
}
