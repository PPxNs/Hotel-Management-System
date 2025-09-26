package com.mycompany.projectdesign;

//import com.mycompany.projectdesign.Project.Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.mycompany.projectdesign.Project.Model.AmountPaidRepository;
import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.CustomerRepository;
import com.mycompany.projectdesign.Project.Model.RoomRepository;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        RoomRepository roomRepo = RoomRepository.getInstance();
        roomRepo.loadRoomFromCSV();

        CustomerRepository customerRepo = CustomerRepository.getInstance();
        customerRepo.loadCustomerFromCSV();

        BookingRepository bookingRepo = BookingRepository.getInstance();
        bookingRepo.initialize(customerRepo, roomRepo); 
        bookingRepo.loadBookingFromCSV();

        AmountPaidRepository amountPaid = AmountPaidRepository.getInstance();
        amountPaid.setBookingRepository(bookingRepo); 
        amountPaid.loadAmountPaidFromCSV();

        scene = new Scene(loadFXML("MainView"), 1410, 780); 
        stage.setScene(scene);
        stage.setTitle("Hotel Management System");
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    
    public static void main(String[] args) {
        launch();
    }
}
