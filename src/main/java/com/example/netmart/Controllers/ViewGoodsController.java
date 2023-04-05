package com.example.netmart.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ViewGoodsController implements Initializable {
    public ChoiceBox<String> category;
    public Button remove_btn;
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

    String selectedCategory = "All";
    public static Goods selectedItem = Goods.nullItem();
    ObservableList<Goods> listGoods;

//    int index = -1;
//    Connection conn = null;
//    ResultSet rs = null;
//    PreparedStatement ps = null;

    // STACKS INSTANCE
    private final StackDB beverages = new StackDB( "beverages");
    private final StackDB bakery = new StackDB( "bakery");
    private final StackDB canned = new StackDB( "canned");
    private final StackDB dairy = new StackDB( "dairy");
    // QUEUE INSTANCE
    private final QueueDB dry = new QueueDB(5, "dry");
    private final QueueDB frozen = new QueueDB(5, "frozen");
    private final QueueDB meat = new QueueDB(5, "meat");

    // LIST INSTANCE
    private ListDB<Goods> produce = new ListDB<Goods>("produce", Goods.class);
    private static ObservableList<Goods> produceItems;
    private final ListDB cleaner = new ListDB("cleaner", Goods.class);
    private final ListDB paper = new ListDB("paper", Goods.class);
    private final ListDB personal = new ListDB("personal", Goods.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        remove_btn.setOnAction(event -> {
            String cat = category.getValue();
            String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
            int _toRemove = selectedItem.getGood_id();
            switch (_cat){
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
                    if(_toRemove < 0){
                        produce.remove();
                    }else{
                        produce.remove(_toRemove);
                    }
                case "cleaner":
                    if(_toRemove < 0){
                        cleaner.remove();
                    }else{
                        cleaner.remove(_toRemove);
                    }
                    break;
                case "paper":
                    if(_toRemove < 0){
                        paper.remove();
                    }else{
                        paper.remove(_toRemove);
                    }
                    break;
                case "personal":
                    if(_toRemove < 0){
                        personal.remove();
                    }else{
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

    public  ObservableList<Goods> getData(String category){
        ObservableList<Goods> goods = FXCollections.observableArrayList();
        try{
            Connection connection = DatabaseConnection.getConnection();


            String query = String.format("SELECT * FROM %s", category);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs =  ps.executeQuery();

            while (rs.next()){
                if(rs.getString("good_name") != null) {
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
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return goods;
    }

}
