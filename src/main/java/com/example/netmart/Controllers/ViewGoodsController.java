package com.example.netmart.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ViewGoodsController implements Initializable {
    public ChoiceBox<String> category;
    public Button remove_btn;
    public Button add_good_btn;
    public DatePicker date;
    public VBox indexVBox;
    public TextField index;
//    public TextField item_1;
    public Button save_btn;
//    public TextField item_1_qty;
//    public TextField item_1_buying_price;
//    public TextField item_1_selling_price;
//    public TextField item_1_gross_price;


    @FXML
    private TableView<Goods> view_goods_table;
    @FXML

    public TableColumn<Goods, Integer> good_id;

    @FXML
    private TableColumn<Goods, String> good_date;

    @FXML
    private TableColumn<Goods, Integer> good_buying_price;

    @FXML
    private TableColumn<Goods, Integer> good_gross_price;

    @FXML
    private TableColumn<Goods, String> good_name;

    @FXML
    private TableColumn<Goods, Integer> good_qty;

    @FXML
    private TableColumn<Goods, Integer> good_selling_price;

    private static final String[] categories = {"Beverages", "Bakery", "Canned", "Dairy", "Dry",
            "Frozen", "Meat", "Produce", "Cleaner", "Paper", "Personal"};

    public static Goods selectedItem = Goods.nullItem();
    ObservableList<Goods> listGoods;

//    int index = -1;
//    Connection conn = null;
//    ResultSet rs = null;
//    PreparedStatement ps = null;

    // STACKS INSTANCE
    private final StackDB beverages = new StackDB("beverages");
    private final StackDB bakery = new StackDB("bakery");
    private final StackDB canned = new StackDB("canned");
    private final StackDB dairy = new StackDB("dairy");
    // QUEUE INSTANCE
    private final QueueDB dry = new QueueDB(10, "dry");
    private final QueueDB frozen = new QueueDB(10, "frozen");
    private final QueueDB meat = new QueueDB(10, "meat");

    // LIST INSTANCE
    private ListDB<Goods> produce = new ListDB<Goods>("produce", Goods.class);
    private static ObservableList<Goods> produceItems;
    private final ListDB cleaner = new ListDB("cleaner", Goods.class);
    private final ListDB paper = new ListDB("paper", Goods.class);
    private final ListDB personal = new ListDB("personal", Goods.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        indexVBox.setVisible(false);
//        category.getItems().addAll(categories);
//        save_btn.setOnAction(event -> onSave());
//        category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//            if (newValue == "Produce" || newValue == "Cleaner" || newValue ==  "Paper" || newValue ==  "Personal"){
//                indexVBox.setVisible(true);
//            }else {
//                indexVBox.setVisible(false);
//            }
//        });

        Integer lastId = null;
        int size = 0;

        good_id.setCellValueFactory(new PropertyValueFactory<>("good_id"));
        good_name.setCellValueFactory(new PropertyValueFactory<>("good_name"));
        good_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        good_buying_price.setCellValueFactory(new PropertyValueFactory<>("buying_price"));
        good_selling_price.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        good_gross_price.setCellValueFactory(new PropertyValueFactory<>("gross_price"));
        good_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        category.getItems().addAll(categories);
        listGoods = getData("beverages");
        view_goods_table.setItems(listGoods);


        // set the default category and add a listener
        category.setValue("All");
        category.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listGoods = getData(newValue.toLowerCase());
                view_goods_table.setItems(listGoods);
            }
        });

        // listen to TableView's selection changes
        view_goods_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedItem = newValue;
            } else {
                // No item is selected
                selectedItem = Goods.nullItem();
            }
        });

        // ADD GOOD BTN
        add_good_btn.setOnAction(event -> addGoodModal());



        // REMOVE BTN
        remove_btn.setOnAction(event -> {
            String cat = category.getValue();
            String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
            int _toRemove = selectedItem.getGood_id();
            switch (_cat) {
                case "beverages":
                    beverages.pop(_cat);
                    break;
                case "bakery":
                    bakery.pop(_cat);
                    break;
                case "canned":
                    canned.pop(_cat);
                    break;
                case "dairy":
                    dairy.pop(_cat);
                    break;
                case "dry":
                    dry.dequeue(_cat);
                    break;
                case "frozen":
                    frozen.dequeue(_cat);
                    break;
                case "meat":
                    meat.dequeue(_cat);
                    break;
                case "produce":
                    if (_toRemove < 0) {
                        produce.remove();
                    } else {
                        produce.remove(_toRemove);
                    }
                case "cleaner":
                    if (_toRemove < 0) {
                        cleaner.remove();
                    } else {
                        cleaner.remove(_toRemove);
                    }
                    break;
                case "paper":
                    if (_toRemove < 0) {
                        paper.remove();
                    } else {
                        paper.remove(_toRemove);
                    }
                    break;
                case "personal":
                    if (_toRemove < 0) {
                        personal.remove();
                    } else {
                        personal.remove(_toRemove);
                    }
                    break;
                default:
                    System.out.println("Category not supported");
                    JOptionPane.showMessageDialog(null, "Category not supported!");
                    break;
            }
        });
    }

    // ON SAVE BTN
//    public void onSave(){
//        if(!item_1.getText().isBlank() && !category.getValue().isBlank() && !item_1_qty.getText().isBlank() && !item_1_buying_price.getText().isBlank() && !item_1_selling_price.getText().isBlank()){
//            // GETTING ITEM 1 VALUES
//            String cat = category.getValue();
//            String item1 = item_1.getText();
//            int quantity = Integer.parseInt(item_1_qty.getText());
//            double buying_price = Double.parseDouble(item_1_buying_price.getText());
//            double selling_price = Double.parseDouble(item_1_selling_price.getText());
//            String date_stamp = String.valueOf(date.getValue());
//            double gross_price = quantity * buying_price;
//            item_1_gross_price.setText(String.valueOf(gross_price));
//            String _indexStr = index.getText();
//            int _index = -1;
//            if(!_indexStr.isEmpty()){ _index = Integer.parseInt(_indexStr);}
//            System.out.println(_index);
//
//            String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
//            switch (_cat){
//                case "beverages":
//                    beverages.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "bakery":
//                    bakery.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "canned":
//                    canned.push(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "dairy":
//                    dairy.push(cat.toLowerCase(), item1, quantity , buying_price, selling_price , gross_price, date_stamp);
//                    break;
//                case "dry":
//                    dry.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "frozen":
//                    frozen.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "meat":
//                    meat.enqueue(cat.toLowerCase(), item1, quantity , buying_price , selling_price , gross_price, date_stamp);
//                    break;
//                case "produce":
//                    if(_index == -1){
//                        produce.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }else{
//                        produce.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }
//                    break;
//                case "cleaner":
//                    if(_index == -1){
//                        cleaner.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }else{
//                        cleaner.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }
//                    break;
//                case "paper":
//                    if(_index == -1){
//                        paper.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }else{
//                        paper.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }
//                    break;
//                case "personal":
//                    if(_index == -1){
//                        personal.add(new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }else{
//                        personal.add(_index, new Goods(-1,  item1, quantity , buying_price , selling_price , gross_price, date_stamp));
//                    }
//                    break;
//            }
//            JOptionPane.showMessageDialog(null, "Added successfully!");
//        } else{
//            JOptionPane.showMessageDialog(null, "Please all fields are required!");
//        }
//    };

    public ObservableList<Goods> getData(String category) {
        ObservableList<Goods> goods = FXCollections.observableArrayList();
        try {
            Connection connection = DatabaseConnection.getConnection();


            String query = String.format("SELECT * FROM %s", category);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("good_name") != null) {
                    goods.add(new Goods(rs.getInt("id"),
                            rs.getString("good_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("buying_price"),
                            rs.getDouble("selling_price"),
                            rs.getDouble("gross_price"),
                            rs.getString("date")
                    ));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return goods;
    }

    public void addGoodModal(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/addGoodModal.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Vendor");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

