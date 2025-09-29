package com.mycompany.projectdesign;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



import com.mycompany.projectdesign.Project.ObserverPattern.HotelEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelObserver;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckinEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckoutEvent;
import com.mycompany.projectdesign.Project.Service.BookingScheduler;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable, HotelObserver{

    @FXML private Button notificationButton;
    @FXML private BorderPane mainPane;

    private int notificationCount = 0;
    private final HotelEventManager eventManager = HotelEventManager.getInstance();

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
            mainPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("ไม่สามารถโหลดไฟล์: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @Override
    public void update(HotelEvent event) {
        if (event instanceof MissedCheckinEvent || event instanceof MissedCheckoutEvent) {
            Platform.runLater(() ->{
                showNotification();
            });
        }
    }

    private void showNotification(){
        notificationCount++;
        notificationButton.setText(String.valueOf(notificationCount));

        //เอาให้เจ้เจ๊ทำ css ต่อ
        if (!notificationButton.getStyleClass().contains("has-notification")) {
            notificationButton.getStyleClass().add("has-notification");
        }

        System.out.println("มีการแจ้งเตือนทั้งหมด : " + notificationCount + "รายการ");
    }

    @FXML private void handleBellChick(){
        notificationCount = 0;
        notificationButton.setText("");
        notificationButton.getStyleClass().remove("has-notification"); 
    }


}
