/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataExtractSQL {

    static Connection connection;

    DataExtractSQL() {//Used to form connection with AWS hosted MySQL service
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://cmu-test.cuawoyjeqihb.us-east-2.rds.amazonaws.com:3306/";
            String userName = "cmu";
            String password = "Cmutest123";
            String dbName = "api";
            DataExtractSQL.connection = DriverManager.getConnection(url + dbName + "?useSSL=false", userName, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveKeyToTable(String name, String email) {//Saving the API key for the user to database
        try {
            String comboString = name + email;
            MessageDigest md;
            String B64 = "";
            md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(comboString.getBytes());
            B64 = DatatypeConverter.printBase64Binary(digest);
            B64 = B64.replace("=", "a").replace("/", "b").replace("+", "c").substring(0, 10);
            String sql = "INSERT INTO api.user_table (user_name, user_email, user_key)"
                    + "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = DataExtractSQL.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, B64);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject fetchRestaurants(String input) {//Fetching the restaurants as per the request
        String[] params = input.split("&");
        JSONObject jsonRestaurants = null;
        JSONArray jsonRestArray = null;
        try {
            String query = "select * from api.restaurants where ";
            for (int i = 0; i < params.length; i++) {
                String[] keyValue = params[i].split("=");
                if (keyValue.length == 2 && !Utils.isEmpty(keyValue[0]) && !Utils.isEmpty(keyValue[1])) {
                    query += keyValue[0] + "=\"" + keyValue[1] + "\"";
                } else {
                    return Utils.sendError(400, "Error in the parameters list.");
                }
                if (i < params.length - 1) {
                    query += " AND ";
                }
            }
            Statement statement = DataExtractSQL.connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery(query);
                jsonRestArray = new JSONArray();
                while (resultSet.next()) {
                    jsonRestaurants = new JSONObject();
                    jsonRestaurants.put("restaurant_id", resultSet.getString("restaurant_id"));
                    jsonRestaurants.put("restaurant_name", resultSet.getString("restaurant_name"));
                    jsonRestaurants.put("city", resultSet.getString("city"));
                    jsonRestaurants.put("price_range", resultSet.getString("price_range"));
                    jsonRestaurants.put("rating", resultSet.getString("rating"));
                    jsonRestaurants.put("contact_number", resultSet.getString("contact_number"));
                    jsonRestArray.add(jsonRestaurants);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Utils.sendResponse(200, jsonRestArray);
        }

    public JSONObject fetchMenu(String input) {//Fetching menu as per the request
        String[] params = input.split("&");
        JSONObject jsonMenu, jsonParse = null, jsonFinal = null;
        JSONArray jsonMenuArray, jsonMenuArray1 = null, jsonMenuArray2 = null, jsonMenuArray3 = null, jsonMenuArrayFinal = null;
        try {
            String query = "select * from api.menu_table where ";
            for (int i = 0; i < params.length; i++) {
                String[] keyValue = params[i].split("=");
                if (keyValue.length == 2 && !Utils.isEmpty(keyValue[0]) && !Utils.isEmpty(keyValue[1])) {
                    query += keyValue[0] + "=\"" + keyValue[1] + "\"";
                } else {
                    return Utils.sendError(400, "Error in the parameters list.");
                }
                if (i < params.length - 1) {
                    query += " AND ";
                }
            }
            Statement statement = DataExtractSQL.connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery(query);
            jsonMenuArray = new JSONArray();
            while (resultSet.next()) {
                jsonMenu = new JSONObject();
                jsonMenu.put("restaurant_id", resultSet.getString("restaurant_id"));
                jsonMenu.put("dish_id", resultSet.getString("dish_id"));
                jsonMenu.put("dish_name", resultSet.getString("dish_name"));
                jsonMenu.put("dish_category", resultSet.getString("dish_category"));
                jsonMenu.put("dish_price", resultSet.getString("dish_price"));
                jsonMenu.put("dish_popularity", resultSet.getString("dish_popularity"));
                jsonMenu.put("menu_category", resultSet.getString("menu_category"));
                jsonMenuArray.add(jsonMenu);
            }
            jsonMenuArray1 = new JSONArray();
            jsonMenuArray2 = new JSONArray();
            jsonMenuArray3 = new JSONArray();
            for (int i = 0; i < jsonMenuArray.size(); i++) {
                jsonParse = (JSONObject) jsonMenuArray.get(i);
                if (jsonParse.get("menu_category").toString().equalsIgnoreCase("lunch")) {
                    jsonMenuArray1.add(jsonParse);
                } else if (jsonParse.get("menu_category").toString().equalsIgnoreCase("dinner")) {
                    jsonMenuArray2.add(jsonParse);
                } else {
                    jsonMenuArray3.add(jsonParse);
                }
            }
            jsonFinal = new JSONObject();
            jsonFinal.put("lunch", jsonMenuArray1);
            jsonFinal.put("dinner", jsonMenuArray2);
            jsonFinal.put("breakfast", jsonMenuArray3);
            jsonMenuArrayFinal = new JSONArray();
            jsonMenuArrayFinal.add(jsonFinal);
        } catch (SQLException ex) {
            Logger.getLogger(DataExtractSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Utils.sendResponse(200, jsonMenuArrayFinal);
    }
}
