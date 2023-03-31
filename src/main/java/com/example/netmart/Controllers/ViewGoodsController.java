package com.example.netmart.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ViewGoodsController implements Initializable {
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


    ObservableList<Goods> listGoods;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        good_id.setCellValueFactory(new PropertyValueFactory<>("good_id"));
        good_name.setCellValueFactory(new PropertyValueFactory<>("good_name"));
        good_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        good_buying_price.setCellValueFactory(new PropertyValueFactory<>("buying_price"));
        good_selling_price.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        good_gross_price.setCellValueFactory(new PropertyValueFactory<>("gross_price"));
        good_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        listGoods = getData();
        view_goods_table.setItems(listGoods);
    }

    public static ObservableList<Goods> getData(){
        ObservableList<Goods> goods = FXCollections.observableArrayList();
        try{
            Connection connection = DatabaseConnection.getConnection();
            String query = String.format("SELECT * FROM beverages");
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs =  ps.executeQuery();

            while (rs.next()){
                goods.add(new Goods(Integer.parseInt(rs.getString("id")),
                        rs.getString("good_name"),
                        Integer.parseInt(rs.getString("quantity")),
                        Integer.parseInt(rs.getString("buying_price")),
                        Integer.parseInt(rs.getString("selling_price")),
                        Integer.parseInt(rs.getString("gross_price")),
                        rs.getString("date")
                ));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return goods;
    }
}
