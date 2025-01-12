package controller;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.DbException;
import model.entities.DatabaseConnection;
import model.entities.Product;

public class StockControllerDAO {
	
	// This method is just to check the available quantity of a product in stock.
    public int getStockQuantity(int productId) {
        String sql = "SELECT quantity FROM stock WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching stock quantity: " + e.getMessage());
        }
        return -1; 
    }


    public void addProductToStock(int productId, int quantityToAdd) {
        String sql = "SELECT quantity FROM stock WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                int newQuantity = currentQuantity + quantityToAdd;

                String updateStockSql = "UPDATE stock SET quantity = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateStockSql)) {
                    updateStatement.setInt(1, newQuantity);
                    updateStatement.setInt(2, productId);
                    updateStatement.executeUpdate();
                    System.out.println("Stock updated successfully.");
                }
            } else {
                System.out.println("Product not found in stock.");
            }
        } catch (SQLException e) {
            throw new DbException("Error adding product to stock: " + e.getMessage());
        }
    }
    public void removeProductFromStock(int productId, int quantityToRemove) {
        String query = "UPDATE stock SET quantity = quantity - ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, quantityToRemove);
            statement.setInt(2, productId);
            
            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Product quantity updated successfully.");
            } else {
                System.out.println("Product not found or invalid quantity.");
            }
            
        } catch (SQLException e) {
            throw new DbException("Error removing product from stock: " + e.getMessage());
        }
    }

    public void viewStock() {
        String sql = "SELECT * FROM stock";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                
                System.out.printf("ID: %d, Name: %s, Category: %s, Price: %.2f, Quantity: %d%n",
                        id, name, category, price, quantity);
            }
        } catch (SQLException e) {
            throw new DbException("Error viewing stock: " + e.getMessage());
        }
    }


    public void addProduct(Product product) {
    	
        String sql = "INSERT INTO stock (name, category, price, quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.executeUpdate();

            System.out.println("Product added to stock successfully!");
        } catch (SQLException e) {
            throw new DbException("Error adding product to stock: " + e.getMessage());
        }
    }

  
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM stock";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setCategory(resultSet.getString("category"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("quantity"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching products from stock: " + e.getMessage());
        }

        return products;
    }


    public void updateProductQuantity(int productId, int newQuantity) {
    	
        String sql = "UPDATE stock SET quantity = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product quantity updated successfully!");
            } else {
                System.out.println("Product not found in stock.");
            }
        } catch (SQLException e) {
            throw new DbException("Error updating product quantity: " + e.getMessage());
        }
    }
}

