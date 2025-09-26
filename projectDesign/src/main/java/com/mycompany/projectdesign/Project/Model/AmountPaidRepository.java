package com.mycompany.projectdesign.Project.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AmountPaidRepository {
    private static final AmountPaidRepository instance = new AmountPaidRepository();
    private Map<String, AmountPaid> amountPaid = new HashMap<>();

    private BookingRepository bookingRepository;

    private AmountPaidRepository(){}
    
    public static AmountPaidRepository getInstance(){
        return instance;
    }

    public void initialize(BookingRepository bookingRepo) {
        this.bookingRepository = bookingRepo;
        loadAmountPaidFromCSV();
    }

    public void addAmount(AmountPaid amount) {
        amountPaid.put(amount.getBooking().getBookingID(), amount);
    }

    public AmountPaid getAmountByBookingID(String bookingID) {
    for (AmountPaid amount : amountPaid.values()) {
        if (amount.getBooking().getBookingID().equals(bookingID)) {
            return amount;
        }
    }
        return null; 
    }

    public List<AmountPaid> getAmountsByBookingID(String bookingID) {
        List<AmountPaid> result = new ArrayList<>();
        for (AmountPaid amount : amountPaid.values()) {
            if (amount.getBooking().getBookingID().equals(bookingID)) {
                result.add(amount);
        }
    }
        return result;
    }
    

    public List<AmountPaid> getAllAmountPaid(){
        return new ArrayList<>(amountPaid.values());
    }

    public void setBookingRepository(BookingRepository bookingRepo) {
        this.bookingRepository = bookingRepo;
    }


    public void saveAmountPaidToCSV() {
    File fi = new File("File/AmountPaid.csv");
    try {
        // ถ้าโฟลเดอร์ยังไม่มี → สร้างขึ้นมา
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        // ถ้าไฟล์ยังไม่มี → สร้างไฟล์ว่าง
        if (!fi.exists()) {
            fi.createNewFile();
        }

        // เขียนไฟล์ทับใหม่ทุกครั้ง
        FileWriter fw = new FileWriter(fi, false);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String key : amountPaid.keySet()) {
            AmountPaid amount = amountPaid.get(key);
            bw.write(amount.getBooking().getBookingID() + "," +
                     amount.getAmount());
            bw.newLine();
        }

        bw.close();
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    //ดึงข้อมูลมาดึงข้อมูลของ csv เข้ามาเก็บใน hash
    // ค่อยเพิ่มเติมตรงจุด แจ้ง exception กรณีไม่เจอไฟล์
   public boolean loadAmountPaidFromCSV() {
    File fi = new File("File/AmountPaid.csv");
    try {
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        if (!fi.exists()) {
            fi.createNewFile();
            System.out.println("สร้างไฟล์ Room.csv ใหม่ (ว่าง)");
            return false;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    FileReader fr = null;
    BufferedReader br = null;

    try {
        fr = new FileReader(fi);
        br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            String[] parts = s.split(",");
            if (parts.length == 2) {
                String bookingID = parts[0];
                Double amountpaid = Double.parseDouble(parts[1]);
               

                Bookings bookings = this.bookingRepository.getBookingByID(bookingID);
                if (bookings != null) {
                    AmountPaid amount = new AmountPaid(bookings, amountpaid);
                    this.addAmount(amount);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (br != null) br.close();
            if (fr != null) fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return true;
}


}
