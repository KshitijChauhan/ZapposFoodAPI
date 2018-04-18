/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Me
 */
@WebServlet(name = "MenuWebService", urlPatterns = {"/Menu/*"})
public class MenuWebService extends HttpServlet {

    DataExtractSQL deSQL = null;
    static String errorRestaurantID = "Yikes !! Need to provide atleast the restaurant ID !!";

    @Override
    public void init() {
        deSQL = new DataExtractSQL();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get method to get the user the menu as per the request
        response.setContentType("application/json");
        JSONObject result = new JSONObject();
        PrintWriter out = response.getWriter();
        if (request.getPathInfo() != null && !request.getPathInfo().equals("")) {
            String restID = (request.getPathInfo()).substring(1);
            if (!Utils.isEmpty(restID)) {
                result = deSQL.fetchMenu(restID);
                if ((int) result.get("statusCode") == 200) {
                    response.setStatus(200);
                    out.print(result);
                } else {
                    response.setStatus((int) result.get("statusCode"));
                    out.print(Utils.sendError(400, "Menu not available"));
                }
            } else {
                out.print(Utils.sendError(400, MenuWebService.errorRestaurantID));
            }
        } else {
            out.print(Utils.sendError(400, MenuWebService.errorRestaurantID));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //doPost to add menu to the database as per the user's request
        DataAdd dataAdd = new DataAdd();
        response.setContentType("application/json");
        JSONObject params = new JSONObject();
        PrintWriter out = response.getWriter();
        String data = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        data = builder.toString();
        JSONParser parser = new JSONParser();
        try {
            params = (JSONObject) parser.parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(MenuWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean isRep = Utils.repDishCheck(params);
        if (!isRep) {
            out.print(dataAdd.addToMenu(params));
        } else {
            out.print(Utils.sendError(400, "The dish entered already exist"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //doPut used to update the data tables as per user's requirements
        DataPut dataPut = new DataPut();
        response.setContentType("application/json");
        JSONObject params = new JSONObject();
        PrintWriter out = response.getWriter();
        String data = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        data = builder.toString();
        JSONParser parser = new JSONParser();
        try {
            params = (JSONObject) parser.parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(MenuWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Utils.existDishCheck(params)) {
            out.print(dataPut.updateDish(params));
        } else {
            out.print(Utils.sendError(400, "Dish in this restaurant does not exist in table"));
        }
    }
}
