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
	    public void addProductToCart(int productId, int quantity) {
        Product product = getProductById(productId);
        
        if (product != null) {
            if (product.getQuantity() >= quantity) {
                String insertCartSql = "INSERT INTO cart (product_id, quantity) VALUES (?, ?)";
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement insertStatement = connection.prepareStatement(insertCartSql)) {
                    
                    insertStatement.setInt(1, productId);  
                    insertStatement.setInt(2, quantity);    
                    insertStatement.executeUpdate();
                    

                    updateProductStock(productId, product.getQuantity() - quantity);
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

	    
    // This method I did is to obtain a product by ID
	    
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM stock WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                return new Product(productId, name, category, price, quantity);
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching product: "+e.getMessage());
        }
        return null;
    }

    private void updateProductStock(int productId, int newQuantity) {
        String sql = "UPDATE stock SET quantity = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating stock: "+e.getMessage());
        }
    }

    public void removeProductFromCart(int productId, int quantity) {
        String deleteCartSql = "DELETE FROM cart WHERE product_id = ? AND quantity = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteCartSql)) {
        
            deleteStatement.setInt(1, productId);
            deleteStatement.setInt(2, quantity);
            int rowsAffected = deleteStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                Product product = getProductById(productId);
                if (product != null) {
                    int newQuantity = product.getQuantity() + quantity;
                    updateProductStock(productId, newQuantity);
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
	


    public void removeProductFromCart(int productId) {
        String sql = "DELETE FROM cart WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
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
