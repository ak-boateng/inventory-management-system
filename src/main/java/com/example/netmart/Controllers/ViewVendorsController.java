package com.example.netmart.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewVendorsController implements Initializable {

    public Button vendor_add_btn;
    public TextField vendor_name;
    public TextField vendor_location;
    public TextField vendor_email;
    public TextField vendor_phone;
    public TextField vendor_address;
    public Button vendor_save_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vendor_add_btn.setOnAction(event -> addVendor());
    }

    public void addVendor(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/addVendor.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Add New Vendor");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
