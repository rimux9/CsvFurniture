package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        dbConnection();
        productData();
    }
    public static void productData() {

        String csvFilePath = "D:\\product_list1.csv.csv";
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            ArrayList<Product> products = new ArrayList<Product>();
            for (CSVRecord record : records) {
                Product product = new Product();
                product.setProductID(Integer.parseInt(record.get(0)));
                product.setProductName(record.get(1));
                product.setProductCategory(record.get(2));
                product.setProductDescription(record.get(3));

                products.add(product);
            }
            PreparedStatement statement = null;
            Connection con = dbConnection();
            String sql = "INSERT INTO product(PRODUCTID, PRODUCTNAME) VALUES (?, ?, ?)";
            statement = con.prepareStatement(sql);
            for (Product record : products) {
                statement.setInt(1, record.getProductID());
                statement.setString(2, record.getProductName());
               statement.setString(3, record.getProductCategory());
                //statement.setString(4, record.getProductDescription());

                statement.addBatch();
            }
            statement.executeBatch();
            con.commit();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    public static Connection dbConnection() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/productdata?", "root", "Debilas26");
            System.out.println("connection is ok");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
