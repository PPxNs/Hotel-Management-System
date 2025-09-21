package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.util.HashMap;

//อ่านด้วย : กรณีสร้างไฟล์แบบไม่มีตัวตั้งต้น ยังไม่จัดการ
/*
 * จะสร้าง dictionary hash map มาช่วยเก็บข้อมูล โกดัง จะได้เข้าถึงข้อมูลได้เร็วมากขึ้น
 * https://marcuscode.com/lang/java/hashmap เว็บที่ช่วยเรียนรู้
 */

 // แก้ไข้เพิ่มเติมตรงส่วนของข้อความแจ้ง exception ทุกอัน

public class RoomRepository {
    HashMap<String, Room > map = new HashMap<String, Room>(); // สร้าง hash map มีตัวของ key เป็น Integer และ value เป็น Room (เริ่มแรกจะใช้ String แต่น่าจะเข้าถึงข้อมูลลำบากขึ้น)
    //คิดว่าอาจจะต้องเผื่อกรณีที่จะเพิ่ม ลบ แก้ data ด้วย 

    /*  แอบรู้สึกกังวลตรงถ้าเราดึงข้อมูล csv มาแล้ว แล้วลบภายหลังเฉพาะใน hash เวลา add ค่าแล้ว ใด ๆ มาแล้วต้องอ่าน csv ใหม่อีกรอบ ก็จะ add ค่าซ้ำซ้อนได้   
    คิดว่าจะมีการจดจำลำดับไฟล์ล่าสุดก็คงจะดี ข้อดีคือ เราเหมือนมีไฟล์ csv เป็นตัวประวัติของการบันทึกข้อมูลข้อโรงแรมเกี่ยวกับการเข้าใช้ การเปิดบริการ 
    อีกทั้งสามารถแก้ไขเฉพาะข้อมูลห้องนั้น ๆ โดยไม่มี key,value ซ้ำซ้อนด้วย (แนวคิดตั้งต้น)

    ตอนนี้เราเปลี่ยนมาเขียนทับไฟล์แทน (ทับไฟล์เก่าหลังปิดระบบ = ข้อมูลอัทเดทล่าสุดทุกครั้ง)) ป้องกันข้อมูลหายหลังปิดโปรแกรม ส่วนเพิ่ม ลบ แก้ไขห้องจะแก้ผ่านคีย์โดยตรง 
    และมีการเคลียร์คีย์ทุกครั้งเมื่อมีการปิดระบบแล้ว
    */

    //ดึงข้อมูลมาดึงข้อมูลของ csv เข้ามาเก็บใน hash
    // ค่อยเพิ่มเติมตรงจุด แจ้ง exception กรณีไม่เจอไฟล์
   public boolean loadRoomFromCSV() {
    File fi = new File("src/main/resources/Room.csv");
    
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
            if (parts.length == 5) {
                String numberRoom = parts[0];
                String type = parts[1];
                double price = Double.parseDouble(parts[2]);
                String status = parts[3];
                String imagePath = parts[4];

                if (!map.containsKey(numberRoom)) {
                    map.put(numberRoom, new Room(numberRoom, type, price, status, imagePath));
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

    // ดึงห้องตามหมายเลข
    public Room getRoom(String roomNumber) {
        return map.get(roomNumber); 
    }

    // เพิ่มห้อง อาจจะต้องมีตัวเช็คว่ามีห้องนั้นยัง ถ้ามีแล้วจะแอดห้องเพิ่มต้องแจ้งเตือน
    public boolean addRoom(Room room){
        //แก้ไขความไม่หยืดหยุ่นด้วย รับค่าที่โยนมา และ return เป็นค่าจริงเท็จแจ้งผลการทำงาน
        //คิดว่าน่าจะต้องมีอะไรเพิ่มความหยืดหยุ่นเจค แบบให้ไม่ยึดข้อมูลที่คีย์ผ่าน gui เผื่อกรณีต้องการปรับแต่งหน้า gui น่าจะลำบาก้ามามั่วแก้โค้ดในนี้
        //มีการดึงข้อมูลจาก gui มา //รอ gui
        //map.put(numberRoom, new Room(numberRoom, type, price, status))
        if (!map.containsKey(room.getNumberRoom())) {
            map.put(room.getNumberRoom(), new Room(room.getNumberRoom(), room.getType(), room.getPrice(), room.getStatus(),room.getImagePath()));
            return true;
        }
        return false;

    }

    //ดึงข้อมูล Room ทั้งหมดที่อยู่ใน HashMap ออกมา แล้วส่งกลับไปให้ Controller เพื่อนำไปแสดงผลในตาราง
    public java.util.Collection<Room> getAllRooms() {
        return map.values();
    }

    // ลบห้อง แต่ต้องมีห้องที่จะลบด้วย ถ้าไม่มีจะ false แล้วให้ gui แจ้งอีกที , เราเลือกแก้ไขที่ hash เข้าถึงผ่านคีย์ อาจจะต้องมีตัว gui ให้เข้าถึงได้โดยตรงไม่ต้องใช้งานผ่านตัวแอด
    public boolean removeRoom(String numberRoom){
        //แก้ไขความไม่หยืดหยุ่นด้วย รับค่าที่โยนมา และ return เป็นค่าจริงเท็จแจ้งผลการทำงาน
        //คิดว่าน่าจะต้องมีอะไรเพิ่มความหยืดหยุ่นเจค แบบให้ไม่ยึดข้อมูลที่คีย์ผ่าน gui เผื่อกรณีต้องการปรับแต่งหน้า gui น่าจะลำบาก้ามามั่วแก้โค้ดในนี้
        //มีการดึงข้อมูลจาก gui มา //รอ gui
        //map.remove(numberRoom);
        if (map.containsKey(numberRoom)) {
            map.remove(numberRoom);
            return true;
        }
        return false;
    }

    /*อาจจะเพิ่มให้แก้เฉพาะจุดก็ได้*/
    // แก้ไขรายละเอียดของห้อง เราเลือกแก้ไขที่ hash เข้าถึงผ่านคีย์ อาจจะต้องมีตัว gui ให้เข้าถึงได้โดยตรงไม่ต้องใช้งานผ่านตัวแอด
    public boolean replaceRoom(String numberRoom, String type, double price,String status,String imagePath){
        //คิดว่าควรสามารถแก้ได้เฉพาะจุดนะ ถ้าคีย์ใหม่หมดทั้ง ๆ ที่แก้้เฉพาะจุดได้ อาจจะแก้ตรงส่วนของ ระบบ gui ให้โยนค่าที่ผู้ใช้ไม่เปลี่ยนมาด้วย
        //แก้ไขความไม่หยืดหยุ่นด้วย รับค่าที่โยนมา และ return เป็นค่าจริงเท็จแจ้งผลการทำงาน
        //คิดว่าน่าจะต้องมีอะไรเพิ่มความหยืดหยุ่นเจค แบบให้ไม่ยึดข้อมูลที่คีย์ผ่าน gui เผื่อกรณีต้องการปรับแต่งหน้า gui น่าจะลำบาก้ามามั่วแก้โค้ดในนี้
        //มีการดึงข้อมูลจาก gui มา //รอ gui
        //map.replace(numberRoom, type, price, status)

        if (!map.containsKey(numberRoom)) {
            return false;
        } else 
            map.put(numberRoom, new Room(numberRoom, type, price, status,imagePath));
        return true;
    }

    //เคลียร์ hash รีเซ็ตคลังข้อมูล
    public void removeAllRoom(){
        map.clear();
    }

    //มีระบบเหมือนปิดโปรแกรมแล้วเรา hash ไปเป็น csv ตอนปรับหรือ update database น่าจะง่ายขึ้น
    //ตอนแรกจะเข้าด้วย Enumerating the elements of the dictionary แต่มัน error เลยเปลี่ยนใหม่
public void saveRoomToCSV() {
    // ระบุ path ไฟล์
    File fi = new File("src/main/resources/Room.csv");
    

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

        for (String key : map.keySet()) {
            Room room = map.get(key);
            bw.write(room.getNumberRoom() + "," +
                     room.getType() + "," +
                     room.getPrice() + "," +
                     room.getStatus() + "," +
                     room.getImagePath());
            bw.newLine();
        }

        bw.close();
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
