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

    ObservableList<Goods> listGoods;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    // STACKS INSTANCE
    private StackDB beverages = new StackDB( "beverages");
    private StackDB bakery = new StackDB( "bakery");
    private StackDB canned = new StackDB( "canned");
    private StackDB dairy = new StackDB( "dairy");
    private QueueDB dry = new QueueDB(5, "dry");
    private QueueDB frozen = new QueueDB(5, "frozen");
    private QueueDB meat = new QueueDB(5, "meat");

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

        category.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listGoods = getData(newValue.toLowerCase());
                view_goods_table.setItems(listGoods);
            }
        });

        remove_btn.setOnAction(event -> {
            String cat = category.getValue();
            String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
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
            }
            StackDB remove = new StackDB(category.getValue());
            remove.pop(category.getValue());
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
                goods.add(new Goods(rs.getString("id"),
                        rs.getString("good_name"),
                        rs.getString("quantity"),
                        rs.getString("buying_price"),
                        rs.getString("selling_price"),
                        rs.getString("gross_price"),
                        rs.getString("date")
                ));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return goods;
    }
}
