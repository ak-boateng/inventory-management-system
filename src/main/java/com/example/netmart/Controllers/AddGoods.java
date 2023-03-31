package com.example.netmart.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddGoods implements Initializable {
    public ChoiceBox<String> category;
    public DatePicker date;

    public TextField item_1_qty;
    public TextField item_1_buying_price;
    public TextField item_1_selling_price;
    public TextField item_1_gross_price;
    public TextField item_1;
    public Button save_btn;
    public TextField total_cost;
    public TextField item_2_qty;
    public TextField item_2_buying_price;
    public TextField item_2_selling_price;
    public TextField item_2_gross_price;
    public TextField item_2;
    public TextField item_3_qty;
    public TextField item_3_buying_price;
    public TextField item_3_selling_price;
    public TextField item_3_gross_price;
    public TextField item_3;
    public TextField item_4_qty;
    public TextField item_4_buying_price;
    public TextField item_4_selling_price;
    public TextField item_4_gross_price;
    public TextField item_4;
    public Label error_lbl;

    private final String[] categories = {"Beverages", "Bread/Bakery", "Canned/Jarred", "Diary", "Dry/Baking Goods",
            "Frozen Foods", "Meat", "Produce", "Cleaner", "Paper Goods", "Personal Care"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.getItems().addAll(categories);
        save_btn.setOnAction(event -> onSave());
    }
    public void onSave(){
         if(!item_1.getText().isBlank() && !category.getValue().isBlank() && !item_1_qty.getText().isBlank() && !item_1_buying_price.getText().isBlank() && !item_1_selling_price.getText().isBlank()){
            // GETTING ITEM 1 VALUES
            String item1 = item_1.getText();
            String quantity = item_1_qty.getText();
            String buying_price = item_1_buying_price.getText();
            String selling_price = item_1_selling_price.getText();
            String date_stamp = String.valueOf(date.getValue());
            int gross_price = Integer.parseInt(quantity) * Integer.parseInt(buying_price);
            item_1_gross_price.setText(String.valueOf(gross_price));

            try{
                // ESTABLISHING CONNECTION TO DATABASE
                Connection connection = DatabaseConnection.getConnection();
                // CHANGING CASE OF CATEGORY
                String cat = category.getValue().toLowerCase();
                // SAVING ITEM TO DATABASE
                String createSql = String.format("INSERT INTO %s (good_name, quantity, buying_price, selling_price, gross_price, date) VALUES (?, ?, ?, ?, ?, ?)", cat);
                PreparedStatement createStatement = connection.prepareStatement(createSql);
                createStatement.setString(1, item1);
                createStatement.setString(2, quantity);
                createStatement.setString(3, buying_price);
                createStatement.setString(4, selling_price);
                createStatement.setString(5, String.valueOf(gross_price));
                createStatement.setString(6, date_stamp);
                createStatement.executeUpdate();
                JOptionPane.showMessageDialog(null,"Good / Item inserted successfully!");
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Please all fields are required!");
        }
    }
}
