package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.DbException;
import model.entities.Cart;
import model.entities.DatabaseConnection;
import model.entities.Product;

public class CartControllerDAO {
	
	
	
	//test
	
    public void addProductToCartOfc(String productName, int quantityToAdd) {
        String sql = "SELECT quantity FROM cart WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                int newQuantity = currentQuantity + quantityToAdd;

                String updateStockSql = "UPDATE stock SET quantity = ? WHERE name = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateStockSql)) {
                    updateStatement.setInt(1, newQuantity);
                    updateStatement.setString(2, productName);
                    updateStatement.executeUpdate();
                    System.out.println("Stock updated successfully.");
                }
            } else {
                System.out.println("Product not found in stock.");
            }
        } catch (SQLException e) {
            throw new DbException("Error adding product to cart: " + e.getMessage());
        }
    }
    
       
	
	 
    // I fixed this, now this method gets the product by name
	    
    public Product getProductByName(String productName) {
        String sql = "SELECT * FROM stock WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                return new Product(productName,category, price, quantity);
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching product: "+e.getMessage());
        }
        return null;
    }

    private void updateProductStock(String productName, int newQuantity) {
        String sql = "UPDATE stock SET quantity = ? WHERE name = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, newQuantity);
          
            statement.setString(2, productName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating stock: "+e.getMessage());
        }
    }

    public void removeProductFromCart(String productName, int quantity) {
        String deleteCartSql = "DELETE FROM cart WHERE product_name = ? AND quantity = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteCartSql)) {
        
            deleteStatement.setString(1, productName);
            deleteStatement.setInt(2, quantity);
            int rowsAffected = deleteStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                Product product = getProductByName(productName);
                if (product != null) {
                    int newQuantity = product.getQuantity() + quantity;
                    updateProductStock(productName, newQuantity);
                    System.out.println("Product removed from cart and stock updated.");
                }
            } else {
                System.out.println("Product not found in cart.");
            }
        } catch (SQLException e) {
            throw new DbException("Error removing product from cart: "+e.getMessage());
        }
    }


    public List<Product> getAllProducts() {
    	
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM stock";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                ));
            }         
            
        } catch (SQLException e) {
            throw new DbException("Error fetching products from stock: " + e.getMessage());
        }

        return products;
    }
    
    public void addProductToCart(Cart cartItem) {
        // Here is just to add the product to the Shopping cart in the database
        String sql = "INSERT INTO cart (name, category, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cartItem.getName());
            statement.setString(2, cartItem.getCategory());
            statement.setDouble(3, cartItem.getPrice());
            statement.setInt(4, cartItem.getQuantity());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException("Error adding product to cart: "+e.getMessage());
        }
    }

    
    public List<Cart> getAllCartItems() {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                cartItems.add(new Cart(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching cart items: "+e.getMessage());
        }

        return cartItems;
    }


    public double calculateCartTotal() {
        double total = 0.0;
        String sql = "SELECT price, quantity FROM cart";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                total += resultSet.getDouble("price") * resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            throw new DbException("Error calculating cart total: " +e.getMessage());
        }

        return total;
    }
	


    public void removeProductFromCart(String cartName) {
        String sql = "DELETE FROM cart WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cartName);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product removed from cart successfully!");
            } else {
                System.out.println("Product not found in cart.");
            }
        } catch (SQLException e) {
            throw new DbException("Error removing product from cart: "+e.getMessage());
        }
    }
}
