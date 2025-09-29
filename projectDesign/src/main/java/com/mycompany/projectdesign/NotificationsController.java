/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;


import java.util.List;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckinEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckoutEvent;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class NotificationsController  {

    @FXML private VBox notificationVBox;
    private Runnable closeAction;

    public void setNotifications(List<HotelEvent> events, Runnable closeAction){
        this.closeAction = closeAction;
        notificationVBox.getChildren().clear();

        if (events.isEmpty()) {
            Label noNotifi = new Label("ไม่มีการแจ้งเตือน");
            noNotifi.setFont(new Font(16));
            notificationVBox.getChildren().add(noNotifi);
        }else {
            for(HotelEvent event : events){
                VBox notificationEntry = new VBox(5);
                notificationEntry.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 10; -fx-border-radius: 5;");

                String titleText = "";
                String detailsText = "";

                if (event instanceof MissedCheckinEvent) {
                    MissedCheckinEvent mcEvent = (MissedCheckinEvent) event;
                    Bookings booking = mcEvent.getBookings();
                    titleText = "แจ้งเตือนก่อนเช็คอิน";
                    detailsText = "ห้อง: " + booking.getRoom().getNumberRoom() + "\n"
                                + "ลูกค้า: " + booking.getCustomer().getFullName() + "\n"
                                + "เวลา: " + booking.getDateCheckin() + " " + booking.getTimeCheckin();
                } else if (event instanceof MissedCheckoutEvent) {
                    MissedCheckoutEvent mcEvent = (MissedCheckoutEvent) event;
                    Bookings booking = mcEvent.getBookings();
                    titleText = "แจ้งเตือนก่อนเช็คเอาต์";
                     detailsText = "ห้อง: " + booking.getRoom().getNumberRoom() + "\n"
                                + "ลูกค้า: " + booking.getCustomer().getFullName() + "\n"
                                + "เวลา: " + booking.getDateCheckout() + " " + booking.getTimeCheckout();
                }

                Label titleLabel = new Label(titleText);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label detailsLabel = new Label(detailsText);
                detailsLabel.setWrapText(true);

                notificationEntry.getChildren().addAll(titleLabel, detailsLabel);
                notificationVBox.getChildren().add(notificationEntry);

            }
        }
    }

    @FXML private void closeWindow(ActionEvent event){
        if (closeAction != null) {
            closeAction.run();
        }
    }
}
