package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.util.*;


/**
 * RoomRepository เป็นคลาสสำหรับจัดการข้อมูลห้องพักทั้งหมดในระบบ
 * ทำหน้าที่เป็นตัวกลางระหว่างตัวรับข้อมูล/ตัวแสดงผลข้อมูลกับฐานข้อมูล (ในที่นี้คือ CSV ไฟล์)
 * ข้อมูลลูกค้าจะถูกจัดเก็บใน Map โดยใช้หมายเลขห้องพัก(numberRoom) เป็น Key หลัก
 */

/*
 * จะสร้าง dictionary hash map มาช่วยเก็บข้อมูล โกดัง จะได้เข้าถึงข้อมูลได้เร็วมากขึ้น
 * https://marcuscode.com/lang/java/hashmap เว็บที่ช่วยเรียนรู้
 */

public class RoomRepository {

    private static final RoomRepository instance = new RoomRepository();
    HashMap<String, Room > allRoomsByKey = new HashMap<String, Room>(); // สร้าง hash map มีตัวของ Key: Sting (เป็นตัวหมายเลยห้อง) Value: ห้องพัก (Room) 
    
    /**
    * Private Constructor เพื่อป้องกันการสร้างอินสแตนซ์จากภายนอก
    * จะทำการโหลดข้อมูลห้องจาก CSV ไฟล์โดยอัตโนมัติเมื่อถูกสร้างครั้งแรก
    */
    //เปิดให้ใช้ RoomRepository เดียวทั้งแอป ไม่ให้ new ด้านนอก
    private RoomRepository(){
        loadRoomFromCSV();
    }
    
    /**
     * ดึงอินสแตนซ์เดียวของ RoomRepository
     * @return อินสแตนซ์ของ RoomRepository
     */
    public static RoomRepository getInstance(){
        return instance;
    }

    /**
     * ดึงข้อมูลห้องพักจากหมายเลขห้อง
     * @param roomNumber หมายเลขห้องที่ต้องการค้นหา
     * @return ออบเจกต์ Room ถ้าพบ, หรือ null ถ้าไม่พบ
     */
    public Room getRoom(String roomNumber) {
        return allRoomsByKey.get(roomNumber); 
    }

    /**
     * เพิ่มห้องใหม่เข้าสู่ hash allRoomsByKey 
     * @param room ออบเจกต์ห้องที่ต้องการเพิ่ม
     * @return true ถ้าเพิ่มสำเร็จ, false ถ้ามีห้องหมายเลขนี้อยู่แล้ว
     */
    public boolean addRoom(Room room){
        String key = room.getNumberRoom().toUpperCase();
        if (!allRoomsByKey.containsKey(key)) {
            allRoomsByKey.put(key, room);
            return true;
        }
        return false;

    }

    /**
     * ดึงข้อมูลห้องพักทั้งหมดที่อยู่ใน hash allRoomsByKey 
     * @return Collection ของออบเจกต์ Room ทั้งหมด
     */
    public java.util.Collection<Room> getAllRooms() {
        return allRoomsByKey.values();
    }

    /**
     * ลบห้องพักออกจาก hash allRoomsByKey  ตามหมายเลขห้อง
     * @param numberRoom หมายเลขห้องที่ต้องการลบ
     * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบห้องหมายเลขดังกล่าว
     */
    public boolean removeRoom(String numberRoom){
        if (allRoomsByKey.containsKey(numberRoom)) {
            allRoomsByKey.remove(numberRoom);
            return true;
        }
        return false;
    }

    /**
     * อัปเดตข้อมูลห้องพัก (แก้ไข) โดยการแทนที่ออบเจกต์เดิมด้วยออบเจกต์ใหม่
     * @param numberRoom หมายเลขห้องที่จะอัปเดต
     * @param type       ประเภทห้องใหม่
     * @param price      ราคาใหม่
     * @param imagePath  ที่อยู่รูปภาพใหม่
     * @param people     จำนวนคนใหม่
     * @param properties คุณสมบัติใหม่
     * @param status     สถานะใหม่
     * @return true ถ้าอัปเดตสำเร็จ, false ถ้าไม่พบห้องหมายเลขดังกล่าว
     */
    public boolean replaceRoom(String numberRoom, String type, double price,String imagePath,int people, List<String> properties, RoomStatus status){

        String key = numberRoom.toUpperCase();
        if (!allRoomsByKey.containsKey(key)) {
            return false;
        } else 
            allRoomsByKey.put(key, new Room(key, type, price,imagePath,people,properties,status));
        return true;
    }

    /**
     * ลบข้อมูลห้องพักทั้งหมดออกจาก hash (คลังข้อมูล)
     */
    public void removeAllRoom(){
        allRoomsByKey.clear();
    }

    /**
     * ค้นหาว่ามีห้องพักตามหมายเลขที่ระบุหรือไม่
     * @param roomNo หมายเลขห้องที่ต้องการตรวจสอบ
     * @return true ถ้ามีห้องนี้อยู่, false ถ้าไม่มี
     */
    public boolean findByRoomNo(String roomNo){
        return allRoomsByKey.containsKey(roomNo.toUpperCase());
    }

    /**
     * บันทึกข้อมูลห้องพักทั้งหมดจาก hash ลงในไฟล์ CSV
     * @throws IOException หากเกิดข้อผิดพลาดระหว่างการเขียนไฟล์
     */
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


    /**
    * โหลดข้อมูลห้องพักจากไฟล์ CSV เข้าสู่ hash
    * @throws IOException หากเกิดข้อผิดพลาดระหว่างการอ่านไฟล์
    */
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