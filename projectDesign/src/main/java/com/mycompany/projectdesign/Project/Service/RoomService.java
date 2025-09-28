package com.mycompany.projectdesign.Project.Service;

import java.time.LocalDateTime;

import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomStatus;

public class RoomService {
    private final BookingRepository bookingRepository = BookingRepository.getInstance();

    public RoomStatus getRealTimeStatus(Room room){
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            return RoomStatus.MAINTENANCE;
        }

        LocalDateTime now = LocalDateTime.now();

        boolean isOccupied = bookingRepository.getAllBookings().stream().allMatch(
            b -> b.getRoom().getNumberRoom().equals(room.getNumberRoom())
            && b.getStatus() == BookingStatus.CHECKED_IN
            && !now.isBefore(b.getDateCheckin().atTime(b.getTimeCheckin()))
            && now.isBefore(b.getDateCheckout().atTime(b.getTimeCheckout()))
        );

        if (isOccupied) {
            return RoomStatus.OCCUPIED;
        }

        if (room.getStatus() == RoomStatus.CLEANING && room.getLastCheckoutTime() != null){
            if (now.isBefore(room.getLastCheckoutTime().plusMinutes(30))) {
                return RoomStatus.CLEANING;
            }
        } 

        return RoomStatus.AVAILABLE;
    }
}
