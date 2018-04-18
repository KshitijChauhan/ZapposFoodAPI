/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Me
 */
public class Utils {

    public static boolean validateKey(String key) {//Validating the authority to access the API
        boolean valid = false;
        try {
            String[] keys = key.split("=");
            String finalKey = keys[1];
            String sql = "select * from api.user_table where user_key = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, finalKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                valid = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }
    
    public static boolean checkKey(String key) {//Validating the authority to access the API
        boolean valid = false;
        try {
            String sql = "select * from api.user_table where user_key = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                valid = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    //To check for empty string
    public static boolean isEmpty(String input) {
        boolean isEmptyTrue = true;
        if (!input.equals("") && input != null) {
            isEmptyTrue = false;
        }
        return isEmptyTrue;
    }

    //To check for empty object
    public static boolean isEmpty(Object input) {
        boolean isEmptyTrue = true;
        if (input != null) {
            String convertedInput = input.toString();
            if (!convertedInput.equals("")) {
                isEmptyTrue = false;
            }
        }
        return isEmptyTrue;
    }

    //To check for error encountered during the API run
    public static JSONObject sendError(int statusCode, String message) {
        JSONObject response = new JSONObject();
        response.put("statusCode", statusCode);
        response.put("message", message);
        return response;
    }

    //To provide user the status and the json response
    public static JSONObject sendResponse(int statusCode, JSONArray data) {
        JSONObject response = new JSONObject();
        response.put("statusCode", statusCode);
        response.put("data", data);
        return response;
    }

    //To provide user the status and the string response
    public static JSONObject sendSuccessResponse(int statusCode, String data) {
        JSONObject response = new JSONObject();
        response.put("statusCode", statusCode);
        response.put("data", data);
        return response;
    }

    //To check for repetetive dish add to the database using doPost
    public static boolean repDishCheck(JSONObject params) {
        boolean isRep = false;
        String dish_name = params.get("dish_name").toString().toLowerCase();
        int restaurant_id = Integer.parseInt(params.get("restaurant_id").toString());
        try {
            String sql = "select * from api.menu_table where LOWER(dish_name) = ? AND restaurant_id = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, dish_name);
            preparedStatement.setInt(2, restaurant_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isRep = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isRep;
    }

    //To check for repetetive restaurant add to the database using doPost
    public static boolean repRestCheck(JSONObject params) {
        boolean isRepRest = false;
        String rest_name = params.get("restaurant_name").toString().toLowerCase();
        String rest_city = params.get("city").toString().toLowerCase();
        try {
            String sql = "select * from api.restaurants where LOWER(restaurant_name) = ? AND LOWER(city) = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, rest_name);
            preparedStatement.setString(2, rest_city);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isRepRest = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isRepRest;
    }

    //To check for repetitive deletion request
    public static boolean delDishCheck(JSONObject params) {
        boolean isDel = false;
        String dish_name = params.get("dish_name").toString().toLowerCase();
        int restaurant_id = Integer.parseInt(params.get("restaurant_id").toString());
        String menu_category = params.get("menu_category").toString().toLowerCase();
        try {
            String sql = "select is_delete from api.menu_table where LOWER(dish_name) = ? AND restaurant_id = ? AND LOWER(menu_category) = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, dish_name);
            preparedStatement.setInt(2, restaurant_id);
            preparedStatement.setString(1, menu_category);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isDel = resultSet.getBoolean("is_delete");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isDel;
    }
    
    //To check if restaurants exist based on user's requirements
    public static boolean existRestCheck(JSONObject params) {
        boolean isRepRest = false;
        int restaurant_id = Integer.parseInt(params.get("restaurant_id").toString());
        try {
            String sql = "select * from api.restaurants where restaurant_id = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurant_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isRepRest = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isRepRest;
    }
    
    //To check for repetitive deletion request
    public static boolean existDishCheck(JSONObject params) {
        boolean isDel = false;
        int dish_id = Integer.parseInt(params.get("dish_id").toString());
        try {
            String sql = "select * from api.menu_table where dish_id = ?";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setInt(1, dish_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isDel = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isDel;
    }    
}
