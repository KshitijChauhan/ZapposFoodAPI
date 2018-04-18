/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Me
 */
public class DataAdd {

    public JSONObject addToMenu(JSONObject params) {//Adding menu to the database

        //Creating a array of keys to check for missing parameters
        String[] keys = {"restaurant_id", "dish_name", "dish_category", "dish_price", "dish_popularity", "menu_category"};
        for (String key : keys) {
            if (!params.containsKey(key) || Utils.isEmpty(params.get(key))) {
                return Utils.sendError(400, "Please provide the full set of data");
            }
        }
        
        //Fetching the parameters from the json received
        int restaurant_id = Integer.parseInt(params.get("restaurant_id").toString());
        String dish_name = params.get("dish_name").toString();
        String dish_category = params.get("dish_category").toString();
        double dish_price = Double.parseDouble(params.get("dish_price").toString());
        int dish_popularity = Integer.parseInt(params.get("dish_popularity").toString());
        String menu_category = params.get("menu_category").toString();
        try {
            //Sql query to add data as requested in doPost
            String sql = "INSERT INTO api.menu_table (restaurant_id, dish_name, dish_category, dish_price, dish_popularity, menu_category)"
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurant_id);
            preparedStatement.setString(2, dish_name);
            preparedStatement.setString(3, dish_category);
            preparedStatement.setDouble(4, dish_price);
            preparedStatement.setInt(5, dish_popularity);
            preparedStatement.setString(6, menu_category);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Utils.sendSuccessResponse(200, "The item " + dish_name + " has been added.");
    }

    public JSONObject addToRest(JSONObject params) {//Adding restaurants to the database
        
        //Creating a array of keys to check for missing parameters
        String[] keys = {"restaurant_name", "city", "price_range", "rating", "contact_number"};
        for (String key : keys) {
            if (!params.containsKey(key) || Utils.isEmpty(params.get(key))) {
                return Utils.sendError(400, "Please provide the full set of data");
            }
        }

        //Fetching the parameters from the json received
        String restaurant_name = params.get("restaurant_name").toString();
        String city = params.get("city").toString();
        int price_range = Integer.parseInt(params.get("price_range").toString());
        double rating = Double.parseDouble(params.get("rating").toString());
        String contact_number = params.get("contact_number").toString();
        try {
            //Sql query to add data as requested in doPost
            String sql = "INSERT INTO api.restaurants (restaurant_name, city, price_range, rating, contact_number)"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, restaurant_name);
            preparedStatement.setString(2, city);
            preparedStatement.setInt(3, price_range);
            preparedStatement.setDouble(4, rating);
            preparedStatement.setString(5, contact_number);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Utils.sendSuccessResponse(200, "The restaurant " + restaurant_name + " has been added.");
    }
}
