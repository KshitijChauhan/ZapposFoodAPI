/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Me
 */
@WebServlet(name = "RestaurantWebService", urlPatterns = {"/restaurants/*"})
public class RestaurantWebService extends HttpServlet {

    DataExtractSQL deSQL = null;

    /**
     *
     */
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
        //Fetch the restaurants from the database as per the user's request

        response.setContentType("application/json");
        JSONObject result = new JSONObject();
        PrintWriter out = response.getWriter();

        if (request.getPathInfo() != null && !request.getPathInfo().equals("")) {
            String restData = (request.getPathInfo()).substring(1);
            if (!Utils.isEmpty(restData)) {
                String[] splitString = restData.split("/");
                boolean keyValid = Utils.validateKey(splitString[0]);
                if (keyValid) {
                    if (splitString.length != 1) {
                        result = deSQL.fetchRestaurants(splitString[1]);
                        if ((int) result.get("statusCode") == 200) {
                            response.setStatus(200);
                            out.print(result);
                        } else {
                            response.setStatus((int) result.get("statusCode"));
                            out.print(Utils.sendError(400, "Restaurant not available"));
                        }
                    } else {
                        out.print(Utils.sendError(400, "Please provide either the city of the name of restaurant"));
                    }
                } else {
                    out.print(Utils.sendError(400, "Incorrect Key Entered"));
                }
            } else {
                out.print(Utils.sendError(400, "API key required for using the API"));
            }
        } else {
            out.print(Utils.sendError(400, "API key required for using the API"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Used to add restaurants to the database as per the user's request
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
        
        if (params.get("api_key") != null) {
            if (Utils.checkKey(params.get("api_key").toString())) {
                boolean isRep = Utils.repRestCheck(params);
                if (!isRep) {
                    out.print(dataAdd.addToRest(params));
                } else {
                    out.print(Utils.sendError(400, "The restaurant " + params.get("restaurant_name") + " is already present in the database"));
                }
            } else {
                out.print(Utils.sendError(400, "Invalid key entered"));
            }
        } else {
            out.print(Utils.sendError(400, "Invalid key entered"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Used to allow users to update data tables
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
        if (Utils.checkKey(params.get("api_key").toString())) {
            if (Utils.existRestCheck(params)) {
                out.print(dataPut.updateRest(params));
            } else {
                out.print(Utils.sendError(400, "Restaurant does not exist in table"));
            }
        } else {
            out.print(Utils.sendError(400, "Invalid key entered"));
        }
    }
}
