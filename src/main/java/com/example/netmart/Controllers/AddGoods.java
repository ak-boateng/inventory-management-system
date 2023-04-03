package com.example.netmart.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.swing.*;
import java.net.URL;
import java.util.Objects;
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

    private final String[] categories = {"Beverages", "Bakery", "Canned", "Dairy", "Dry",
            "Frozen Foods", "Meat", "Produce", "Cleaner", "Paper Goods", "Personal Care"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.getItems().addAll(categories);
        save_btn.setOnAction(event -> onSave());
    }

    Integer lastId = null;
    int size = 0;

    // STACKS INSTANCE
    private StackDB beverages = new StackDB( "beverages");
    private StackDB bakery = new StackDB( "bakery");
    private StackDB canned = new StackDB( "canned");
    private StackDB dairy = new StackDB( "dairy");

    public void onSave(){
            if(!item_1.getText().isBlank() && !category.getValue().isBlank() && !item_1_qty.getText().isBlank() && !item_1_buying_price.getText().isBlank() && !item_1_selling_price.getText().isBlank()){
            // GETTING ITEM 1 VALUES
                String cat = category.getValue();
                String item1 = item_1.getText();
                String quantity = item_1_qty.getText();
                String buying_price = item_1_buying_price.getText();
                String selling_price = item_1_selling_price.getText();
                String date_stamp = String.valueOf(date.getValue());
                int gross_price = Integer.parseInt(quantity) * Integer.parseInt(buying_price);
                item_1_gross_price.setText(String.valueOf(gross_price));

                String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];

                switch (_cat){
                    case "beverages":
                        beverages.push(cat.toLowerCase(), item1, Integer.parseInt(quantity) , Double.parseDouble(buying_price) , Double.parseDouble(selling_price) , gross_price, date_stamp);
                        break;
                    case "bakery":
                        bakery.push(cat.toLowerCase(), item1, Integer.parseInt(quantity) , Double.parseDouble(buying_price) , Double.parseDouble(selling_price) , gross_price, date_stamp);
                        break;
                    case "canned":
                        canned.push(cat.toLowerCase(), item1, Integer.parseInt(quantity) , Double.parseDouble(buying_price) , Double.parseDouble(selling_price) , gross_price, date_stamp);
                        break;
                    case "dairy":
                        dairy.push(cat.toLowerCase(), item1, Integer.parseInt(quantity) , Double.parseDouble(buying_price) , Double.parseDouble(selling_price) , gross_price, date_stamp);
                        break;
                }

                JOptionPane.showMessageDialog(null, "Added successfully!");

        } else{
            JOptionPane.showMessageDialog(null, "Please all fields are required!");
        }
    }
}
