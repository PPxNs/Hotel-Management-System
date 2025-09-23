package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//อ่านด้วย : กรณีสร้างไฟล์แบบไม่มีตัวตั้งต้น ยังไม่จัดการ
/*
 * จะสร้าง dictionary hash map มาช่วยเก็บข้อมูล โกดัง จะได้เข้าถึงข้อมูลได้เร็วมากขึ้น
 * https://marcuscode.com/lang/java/hashmap เว็บที่ช่วยเรียนรู้
 */

 // แก้ไข้เพิ่มเติมตรงส่วนของข้อความแจ้ง exception ทุกอัน

public class RoomRepository {

    private static final RoomRepository instance = new RoomRepository();
    HashMap<String, Room > allRoomsByKey = new HashMap<String, Room>(); // สร้าง hash map มีตัวของ key เป็น Integer และ value เป็น Room (เริ่มแรกจะใช้ String แต่น่าจะเข้าถึงข้อมูลลำบากขึ้น)
    


    //เปิดให้ใช้ RoomRepository เดียวทั้งแอป ไม่ให้ new ด้านนอก
    private RoomRepository(){
        loadRoomFromCSV();
    }
    
    public static RoomRepository getInstance(){
        return instance;
    }

    // ดึงห้องตามหมายเลข
    public Room getRoom(String roomNumber) {
        return allRoomsByKey.get(roomNumber); 
    }

    // เพิ่มห้อง 
    public boolean addRoom(Room room){
        if (!allRoomsByKey.containsKey(room.getNumberRoom())) {
            allRoomsByKey.put(room.getNumberRoom(), room);
            return true;
        }
        return false;

    }

    //ดึงข้อมูล Room ทั้งหมดที่อยู่ใน HashMap ออกมา แล้วส่งกลับไปให้ Controller เพื่อนำไปแสดงผลในตาราง
    public java.util.Collection<Room> getAllRooms() {
        return allRoomsByKey.values();
    }

    // ลบห้อง แต่ต้องมีห้องที่จะลบด้วย ถ้าไม่มีจะ false แล้วให้ gui แจ้งอีกที , เราเลือกแก้ไขที่ hash เข้าถึงผ่านคีย์ อาจจะต้องมีตัว gui ให้เข้าถึงได้โดยตรงไม่ต้องใช้งานผ่านตัวแอด
    public boolean removeRoom(String numberRoom){
        if (allRoomsByKey.containsKey(numberRoom)) {
            allRoomsByKey.remove(numberRoom);
            return true;
        }
        return false;
    }

    /*อาจจะเพิ่มให้แก้เฉพาะจุดก็ได้*/
    // แก้ไขรายละเอียดของห้อง เราเลือกแก้ไขที่ hash เข้าถึงผ่านคีย์ อาจจะต้องมีตัว gui ให้เข้าถึงได้โดยตรงไม่ต้องใช้งานผ่านตัวแอด
    public boolean replaceRoom(String numberRoom, String type, double price,String imagePath,int people, List<String> properties, RoomStatus status){

        if (!allRoomsByKey.containsKey(numberRoom)) {
            return false;
        } else 
            allRoomsByKey.put(numberRoom, new Room(numberRoom, type, price,imagePath,people,properties,status));
        return true;
    }

    //เคลียร์ hash รีเซ็ตคลังข้อมูล
    public void removeAllRoom(){
        allRoomsByKey.clear();
    }

    //มีระบบเหมือนปิดโปรแกรมแล้วเรา hash ไปเป็น csv ตอนปรับหรือ update database น่าจะง่ายขึ้น
    public void saveRoomToCSV() {
        
        File fi = new File("File/Room.csv");
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

        for (String key : allRoomsByKey.keySet()) {
            Room room = allRoomsByKey.get(key);
            bw.write(room.getNumberRoom() + "," +
                     room.getType() + "," +
                     room.getPrice() + "," +
                     room.getStatus().name() + "," +
                     room.getImagePath()+ "," +
                     room.getPeople() + "," +
                     String.join(";", room.getProperties()));
            bw.newLine();
        }

        System.out.println("บันทึกห้องจำนวน: " + allRoomsByKey.size());

        bw.close();
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    //ดึงข้อมูลมาดึงข้อมูลของ csv เข้ามาเก็บใน hash
   public boolean loadRoomFromCSV() {
    File fi = new File("File/Room.csv");
    
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
            if (parts.length == 7) {
                String numberRoom = parts[0];
                String type = parts[1];
                double price = Double.parseDouble(parts[2]);
                RoomStatus status = RoomStatus.valueOf(parts[3]);
                String imagePath = parts[4];
                int people = Integer.parseInt(parts[5]);
                List<String> properties; 
                
                //.trim() เอาไว้กันกรณีที่ไฟล์มี space
                if (parts[6].trim().isEmpty()) {
                    properties = List.of(); //ไม่มี properties
                }else {
                    properties = Arrays.asList(parts[6].split(";"));
                }
                    

                if (!allRoomsByKey.containsKey(numberRoom)) {
                    allRoomsByKey.put(numberRoom, new Room(numberRoom, type, price, imagePath,people,properties, status));
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