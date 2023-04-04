package com.example.netmart.Controllers;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ListDB<T> {
    private  int size;
    private String cat;
    private Class<T> clazz;

    public ListDB(String cat, Class<T> clazz){
        try{
            this.cat = cat;
            this.size = 0;
            this.clazz = clazz;

            // CONNECTING TO DB
            System.out.println("Connecting to DB");
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            System.out.println("Connected Successfully!");

            // CREATING TABLE IF NOT EXISTING
            String createTableQuery = "";
            if(clazz.equals(Goods.class)){
                createTableQuery = "CREATE TABLE IF NOT EXISTS " + cat + " (" +
                        "id INT UNIQUE, " +
                        "good_name VARCHAR(255), " +
                        "quantity INT, " +
                        "buying_price DECIMAL(10, 2), " +
                        "selling_price DECIMAL(10, 2), " +
                        "gross_price DECIMAL(10, 2), " +
                        "date VARCHAR(255)" +
                        ")";
            }
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute(createTableQuery);

            // Get the current list size
            String countQuery = "SELECT COUNT(*) FROM " + cat;
            Statement countStatement = connection.createStatement();
            ResultSet countResult = countStatement.executeQuery(countQuery);
            if (countResult.next()) {
                size = countResult.getInt(1);
            }

        }catch (Exception ex){
            System.out.println("Error connecting to Database " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void add(T x) {
        addItem(x, size);
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            JOptionPane.showMessageDialog(null, "Index out of bounds" + index + " [" + size + "]");
//            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " [" + size + "]");
        }
        addItem(x, index);
    }

    private  void addItem(T x, int index){
        try{
            for(int i = size - 1; i >= index; i--){
                T currentItem = getItem(i);
                setItem(i + 1, currentItem);
            }
            String insertQuery = "";
            if(clazz.equals(Goods.class)){
                Goods goods = (Goods) x;
                if(index == size){
                    insertQuery = "INSERT INTO " + cat + "(id, good_name, quantity, buying_price, selling_price, gross_price, date) VALUES ("
                            + index + ", '" + goods.getGood_name() + "', " + goods.getQuantity() + ", " + goods.getBuying_price() + ", "
                            + goods.getSelling_price() + ", " + goods.getGross_price() + ", '" + goods.getDate()
                            + "')";
                }
                else{
                    insertQuery = "UPDATE " + cat + " SET " + goods.sqlStr() + " WHERE id = " + index;
                }
            }

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.executeUpdate();
            size++;

        } catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public T get(int index){
        if(index < 0 || index >= size){
            JOptionPane.showMessageDialog(null, "Index out of bounds: " + index);
//            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return null;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            JOptionPane.showMessageDialog(null, "Index out of bounds: " + index);
//            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        T removedItem = getItem(index);
        try {
//             Shift all elements to the left of the index
            for (int i = index; i < size - 1; i++) {
                T currentItem = getItem(i + 1);
                setItem(i, currentItem);
            }

            // Remove the last item
            String deleteQuery = "DELETE FROM " + cat + " WHERE id = " + (size - 1);
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();
            size--;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return removedItem;
    }

    public boolean remove(T element) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (getItem(i).equals(element)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            JOptionPane.showMessageDialog(null, "Index out of bounds: " + index);
//            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        setItem(index, element);
    }
    private void setItem(int index, T goods){
        try{
            String updateQuery = "";
            if(clazz.equals(Goods.class)){
                Goods x = (Goods) goods;
                if(index == size){
                    updateQuery = "INSERT INTO " + cat + "(id, good_name, quantity, buying_price, selling_price, gross_price, date) VALUES (" +
                            + index + ", '" + x.getGood_name() + "', " + x.getQuantity() + ", '"
                            + x.getBuying_price() + "', " + x.getSelling_price() + ", " + x.getGross_price() + ", '" + x.getDate()
                            + "')";
                }else {
                    updateQuery = "UPDATE " + cat + " SET " + x.sqlStr() + " WHERE id = " + index;
                }
            }
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public int size(){
        return size;
    }
    @SuppressWarnings("unchecked")
    private T getItem(int index) {
        T item = null;
        try {
            String selectQuery = "SELECT * FROM " + cat + " WHERE id = " + index;
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                try {
                    if (clazz.equals(Goods.class)) {

                        item = (T) new Goods(
                                resultSet.getInt("id"),
                                resultSet.getString("good_name"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("selling_price"),
                                resultSet.getDouble("buying_price"),
                                resultSet.getInt("gross_price"),
                                resultSet.getString("date"));

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public Class<T> getGenericType() {
        return clazz;
    }
}