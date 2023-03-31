package com.example.netmart.Controllers;

import com.example.netmart.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {
    public TextField name_field;
    public TextField password_field;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(event -> onLogin());
    }

    public void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showMainWindow();

//        if(!name_field.getText().isBlank() && !password_field.getText().isBlank()){
//            String username = name_field.getText();
//            String password = password_field.getText();
//            try{
//                Connection connection = DatabaseConnection.getConnection();
//                // CREATE
//                String createSql = "INSERT INTO users (username, password) VALUES (?, ?)";
//                PreparedStatement createStatement = connection.prepareStatement(createSql);
//                createStatement.setString(1, username);
//                createStatement.setString(2, password);
//                createStatement.executeUpdate();
////                System.out.println("Record created.");
//            } catch (Exception ex){
//                ex.printStackTrace();
//            }
//        } else{
//            error_lbl.setText("Please enter your username and password!");
//        }
    }
}
