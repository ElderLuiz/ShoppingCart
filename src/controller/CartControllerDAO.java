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
	
	
	//Here i created a method to add a product to the shopping cart
	    public void addProductToCart(String ProductName, int quantity) {
        Product product = getProductByName(ProductName);
        
        if (product != null) {
            if (product.getQuantity() >= quantity) {
                String insertCartSql = "INSERT INTO cart (product_name, quantity) VALUES (?, ?)";
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement insertStatement = connection.prepareStatement(insertCartSql)) {
                    
                	insertStatement.setString(1, ProductName);  
                    insertStatement.setInt(2, quantity);    
                    insertStatement.executeUpdate();
                    

                    updateProductStock(ProductName, product.getQuantity() - quantity);
                    System.out.println("Product added to cart successfully.");
                } catch (SQLException e) {
                    throw new DbException("Error adding product to cart: "+e.getMessage());
                }
            } else {
                System.out.println("Not enough stock available.");
            }
        } else {
            System.out.println("Product not found.");
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
    
/*
    private boolean isProductInCart(int productId) {
        String sql = "SELECT * FROM cart WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error checking product in cart: " + e.getMessage());
        }

        return false;
    }


    private void updateProductQuantityInCart(int productId, int newQuantity) {
        String sql = "UPDATE cart SET quantity = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product quantity updated in cart.");
            } else {
                System.out.println("Error updating product quantity in cart.");
            }
        } catch (SQLException e) {
            throw new DbException("Error updating product quantity in cart: "+e.getMessage());
        }
    }

  
    
    // I used this method to obtain the product from stock by ID.
    private Product getProductFromStock(int productId) {
        String sql = "SELECT * FROM stock WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching product from stock: "+e.getMessage());
        }

        return null;
    }*/
}
