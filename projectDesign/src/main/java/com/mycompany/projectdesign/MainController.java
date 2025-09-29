package com.mycompany.projectdesign;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



import com.mycompany.projectdesign.Project.ObserverPattern.HotelEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelObserver;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckinEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckoutEvent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable, HotelObserver{

    @FXML private AnchorPane rootPane; 
    @FXML private Button notificationButton;
    @FXML private BorderPane mainContentPane;

    private final HotelEventManager eventManager = HotelEventManager.getInstance();
    private List<HotelEvent> notifications = new ArrayList<>();
    private Node notificationPopup = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPage("Home.fxml");
        eventManager.addObserver(this);
    }

    @FXML private void showRoomsPage(ActionEvent event){
        loadPage("Rooms.fxml");
    }


    @FXML private void showHomePage(ActionEvent event) {
        loadPage("Home.fxml");
    }


    @FXML private void showReservationsPage(ActionEvent event) {
        loadPage("Reservations.fxml");
    }


    @FXML private void showGuestsPage(ActionEvent event) {
        loadPage("Guests.fxml");
    }
    

    //loader.load() มันบังคับ try catch
    @FXML private void loadPage(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node page = loader.load();
            mainContentPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("ไม่สามารถโหลดไฟล์: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @Override
    public void update(HotelEvent event) {
        if (event instanceof MissedCheckinEvent || event instanceof MissedCheckoutEvent) {
            Platform.runLater(() ->{
                notifications.add(event);
                showNotification();
            });
        }
    }

    private void showNotification(){
        notificationButton.setText(String.valueOf(notifications.size()));

        //เอาให้เจ้เจ๊ทำ css ต่อ
        if (!notificationButton.getStyleClass().contains("has-notification")) {
            notificationButton.getStyleClass().add("has-notification");
        }

        System.out.println("มีการแจ้งเตือนทั้งหมด : " + notifications.size() + "รายการ");
    }

    @FXML private void handleBellCheck(){
         if (notificationPopup == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Notifications.fxml"));
                notificationPopup = loader.load();
                NotificationsController controller = loader.getController();
                
                controller.setNotifications(notifications, this::hideNotificationPopup);
                
                // ตั้งค่าตำแหน่งของ Pop-up
                AnchorPane.setTopAnchor(notificationPopup, 60.0);
                AnchorPane.setRightAnchor(notificationPopup, 20.0);
                
                rootPane.getChildren().add(notificationPopup);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            hideNotificationPopup();
        }
    }

        private void hideNotificationPopup() {
        if (notificationPopup != null) {
            rootPane.getChildren().remove(notificationPopup);
            notificationPopup = null;
            clearNotifications();
        }
    }

    private void clearNotifications() {
        notifications.clear();
        notificationButton.setText("");
        notificationButton.getStyleClass().remove("has-notification");
    }
}
