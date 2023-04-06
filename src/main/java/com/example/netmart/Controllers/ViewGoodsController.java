package com.example.netmart.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ViewGoodsController {
    public ChoiceBox<String> category;
    public Button remove_btn;
    public Button add_good_btn;


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
    private static ObservableList<Goods> allGoods = FXCollections.observableArrayList();
    public static Goods selectedItem = Goods.nullItem();
    public static ObservableList<Goods> listGoods = FXCollections.observableArrayList();



    // STACKS INSTANCE
    public static StackDB beverages = new StackDB( "beverages");
    public static ObservableList<Goods> beverageItems;
    public static StackDB bakery = new StackDB( "bakery");
    public static ObservableList<Goods> bakeryItems;

    public static StackDB canned = new StackDB( "canned");
    public static ObservableList<Goods> cannedItems;

    public static StackDB dairy = new StackDB( "dairy");
    public static ObservableList<Goods> dairyItems;


    // QUEUE INSTANCE
    public static QueueDB dry = new QueueDB(10, "dry");
    public static ObservableList<Goods> dryItems;

    public static QueueDB frozen = new QueueDB(10, "frozen");
    public static ObservableList<Goods> frozenItems;

    public static QueueDB meat = new QueueDB(10, "meat");
    public static ObservableList<Goods> meatItems;


    // LIST INSTANCE
    public static ListDB<Goods> produce = new ListDB<Goods>("produce", Goods.class);
    public static ObservableList<Goods> produceItems;

    public static ListDB cleaner = new ListDB("cleaner", Goods.class);
    public static ObservableList<Goods> cleanerItems;

    public static ListDB paper = new ListDB("paper", Goods.class);
    public static ObservableList<Goods> paperItems;

    public static ListDB personal = new ListDB("personal", Goods.class);
    public static ObservableList<Goods> personalItems;


    public void initialize() {

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
        listGoods = getCategoryData("beverages");
        view_goods_table.setItems(listGoods);


        // set the default category and add a listener
        category.setValue("All");
        category.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listGoods = getCategoryData(newValue.toLowerCase());
                view_goods_table.setItems(listGoods);
            }
        });

        // Show all goods in the table
        view_goods_table.setItems(allGoods);

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
        add_good_btn.setOnAction(event -> addGood());


        // REMOVE ITEMS
        remove_btn.setOnAction(event -> {
            String cat = category.getValue();
            String _cat = cat.toLowerCase().split("/")[0].split(" ")[0];
            int _toRemove = selectedItem.getGood_id();
            switch (_cat) {
                case "beverages":
                    beverages.pop(_cat);
                    beverageItems.remove(0);
                    break;
                case "bakery":
                    bakery.pop(_cat);
                    bakeryItems.remove(0);
                    break;
                case "canned":
                    canned.pop(_cat);
                    cannedItems.remove(0);
                    break;
                case "dairy":
                    dairy.pop(_cat);
                    dairyItems.remove(0);
                    break;
                case "dry":
                    dry.dequeue(_cat);
                    dryItems.remove(0);
                    break;
                case "frozen":
                    frozen.dequeue(_cat);
                    frozenItems.remove(0);
                    break;
                case "meat":
                    meat.dequeue(_cat);
                    meatItems.remove(0);
                    break;
                case "produce":
                    if (_toRemove < 0) {
                        produce.remove();
                        produceItems.remove(0);
                    } else {
                        produce.remove(_toRemove);
                        produceItems.remove(0);
                    }
                case "cleaner":
                    if (_toRemove < 0) {
                        cleaner.remove();
                        cleanerItems.remove(0);
                    } else {
                        cleaner.remove(_toRemove);
                        cleanerItems.remove(0);
                    }
                    break;
                case "paper":
                    if (_toRemove < 0) {
                        paper.remove();
                        paperItems.remove(0);
                    } else {
                        paper.remove(_toRemove);
                        paperItems.remove(0);
                    }
                    break;
                case "personal":
                    if (_toRemove < 0) {
                        personal.remove();
                        personalItems.remove(0);
                    } else {
                        personal.remove(_toRemove);
                        personalItems.remove(0);
                    }
                    break;
                default:
                    System.out.println("No Category Selected");
                    JOptionPane.showMessageDialog(null, "Category not supported!");
                    break;
            }
        });
    }


    public ObservableList<Goods> getCategoryData(String category){
        switch (category){
            case "beverages":
                if(beverageItems == null){
                    beverageItems = getData(category);
                }
                return beverageItems;
            case "bakery":
                if(bakeryItems == null){
                    bakeryItems = getData(category);
                }
                return bakeryItems;
            case "dry":
                if(dryItems == null){
                    dryItems = getData(category);
                }
                return dryItems;
            case "frozen":
                if(frozenItems == null){
                    frozenItems = getData(category);
                }
                return frozenItems;
            case "meat":
                if(meatItems == null){
                    meatItems = getData(category);
                }
                return meatItems;
            case "produce":
                if(produceItems == null) {
                    produceItems = getData(category);
                return produceItems;
                }
            case "canned":
                if(cannedItems == null){
                    cannedItems = getData(category);
                }
                return cannedItems;
            case "dairy":
                if(dairyItems == null){
                    dairyItems = getData(category);
                }
                return dairyItems;
            case "cleaner":
                if(cleanerItems == null){
                    cleanerItems = getData(category);
                }
                return cleanerItems;
            case "paper":
                if(paperItems == null){
                    paperItems = getData(category);
                }
                return paperItems;
            case "personal":
                if(personalItems == null){
                    personalItems = getData(category);
                }
                return personalItems;
            default:
                JOptionPane.showMessageDialog(null, "No such category!");
        }
        return null;
    }
    private void addGood(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/addGoodModal.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Good");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


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


}

