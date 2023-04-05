package com.example.netmart.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.net.URL;
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

    private final String[] categories = {"Beverages", "Bakery", "Canned", "Dairy", "Dry",
            "Frozen", "Meat", "Produce", "Cleaner", "Paper", "Personal"};
    public VBox indexVBox;
    public TextField index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        indexVBox.setVisible(false);
        category.getItems().addAll(categories);
        save_btn.setOnAction(event -> onSave());
        category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == "Produce" || newValue == "Cleaner" || newValue ==  "Paper" || newValue ==  "Personal"){
                indexVBox.setVisible(true);
            }else {
                indexVBox.setVisible(false);
            }
        });
    }


    Integer lastId = null;
    int size = 0;

    // STACKS INSTANCE
    private StackDB beverages = new StackDB( "beverages");
    private StackDB bakery = new StackDB( "bakery");
    private StackDB canned = new StackDB( "canned");
    private StackDB dairy = new StackDB( "dairy");

    // QUEUE INSTANCE
    private QueueDB dry = new QueueDB(10, "dry");
    private QueueDB frozen = new QueueDB(10, "frozen");
    private QueueDB meat = new QueueDB(10, "meat");

    // LIST INSTANCE
    private ListDB<Goods> produce = new ListDB<Goods>("produce", Goods.class);
    private ListDB cleaner = new ListDB("cleaner", Goods.class);
    private ListDB paper = new ListDB("paper", Goods.class);
    private ListDB personal = new ListDB("personal", Goods.class);

    public void onSave(){
            if(!item_1.getText().isBlank() && !category.getValue().isBlank() && !item_1_qty.getText().isBlank() && !item_1_buying_price.getText().isBlank() && !item_1_selling_price.getText().isBlank()){
                // GETTING ITEM 1 VALUES
                String cat = category.getValue();
                String item1 = item_1.getText();
                int quantity = Integer.parseInt(item_1_qty.getText());
                double buying_price = Double.parseDouble(item_1_buying_price.getText());
                double selling_price = Double.parseDouble(item_1_selling_price.getText());
                String date_stamp = String.valueOf(date.getValue());
                double gross_price = quantity * buying_price;
                item_1_gross_price.setText(String.valueOf(gross_price));
                String _indexStr = index.getText();
                int _index = -1;
                if(!_indexStr.isEmpty()){ _index = Integer.parseInt(_indexStr);}
                System.out.println(_index);

                String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
                switch (_cat){
                    case "beverages":
                        beverages.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "bakery":
                        bakery.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "canned":
                        canned.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "dairy":
                        dairy.push(cat.toLowerCase(), item1, quantity , buying_price, selling_price , gross_price, date_stamp);
                        break;
                    case "dry":
                        dry.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "frozen":
                        frozen.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "meat":
                        meat.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
                        break;
                    case "produce":
                        if(_index == -1){
                            produce.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }else{
                            produce.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }
                        break;
                    case "cleaner":
                        if(_index == -1){
                            cleaner.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }else{
                            cleaner.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }
                        break;
                    case "paper":
                        if(_index == -1){
                            paper.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }else{
                            paper.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }
                        break;
                    case "personal":
                        if(_index == -1){
                            personal.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }else{
                            personal.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
                        }
                        break;
                }
                JOptionPane.showMessageDialog(null, "Added successfully!");
            } else{
            JOptionPane.showMessageDialog(null, "Please all fields are required!");
        }
    }
}
