module com.erikshea.nestiuseraccount {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;
    requires sqlite.jdbc;
    

    opens com.erikshea.nestiuseraccount.application to javafx.fxml;
    opens com.erikshea.nestiuseraccount.controller to javafx.fxml;
    opens com.erikshea.nestiuseraccount.form to javafx.fxml;
    opens com.erikshea.nestiuseraccount.model to javafx.fxml;
    exports com.erikshea.nestiuseraccount.application;
    

}
