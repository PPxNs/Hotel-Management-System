module com.mycompany.projectdesign {
    requires javafx.controls;
    requires javafx.fxml;
    // VVV --- 1. เปลี่ยนเป็น transitive เพื่อแก้ปัญหา Stage --- VVV
    requires transitive javafx.graphics; 
    requires java.base;
    requires java.desktop;
    requires kernel;
    requires forms;

    // VVV --- 2. เปิด Package หลักและ Package ย่อยเพื่อให้ FXML เข้าถึงได้ --- VVV
    opens com.mycompany.projectdesign to javafx.fxml;
    opens com.mycompany.projectdesign.Project.Model to javafx.fxml;

    // VVV --- 3. exports Package หลักและ Package ย่อยเพื่อให้โค้ดอื่นมองเห็น --- VVV
    exports com.mycompany.projectdesign;
    exports com.mycompany.projectdesign.Project.Model;
}
