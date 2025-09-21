package com.mycompany.projectdesign;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable{
    @FXML private BorderPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPage("Home.fxml");
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


}
