/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import java.io.PrintWriter;
import org.json.simple.JSONObject;

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
        if (request.getPathInfo() != null && !request.getPathInfo().equals("")) {
            String restData = (request.getPathInfo()).substring(1);
            if (restData.equals("")) {
                PrintWriter out = response.getWriter();
                out.print("API key required for using the API");
            } else if (!restData.contains("&")) {
                String[] splitString = restData.split("/");
                int lengthString = splitString.length;
                boolean keyValid = deSQL.validateKey(splitString[0]);
                if (keyValid && (lengthString > 1)) {
                    JSONArray jsonRestaurants = deSQL.fetchRestaurantsCity(splitString[1]);
                    JSONObject jsonFinal = new JSONObject();
                    if (!jsonRestaurants.isEmpty()) {
                        jsonFinal.put("status code", 200);
                        jsonFinal.put("restaurants", jsonRestaurants);
                        PrintWriter out = response.getWriter();
                        out.print(jsonFinal);
                    } else {
                        PrintWriter out = response.getWriter();
                        out.print("status code: 400 (Error in fetching data)");
                    }
                } else if (keyValid) {
                    JSONArray jsonRestaurants = deSQL.fetchRestaurants();
                    JSONObject jsonFinal = new JSONObject();
                    if (!jsonRestaurants.isEmpty()) {
                        jsonFinal.put("status code", 200);
                        jsonFinal.put("restaurants", jsonRestaurants);
                        PrintWriter out = response.getWriter();
                        out.print(jsonFinal);
                    } else {
                        PrintWriter out = response.getWriter();
                        out.print("status code: 400 (Error in fetching data)");
                    }
                } else {
                    PrintWriter out = response.getWriter();
                    out.print("status code: 400 (INVALID KEY ENTERED)");
                }
            } else {
                String[] splitString = restData.split("/");
                boolean keyValid = deSQL.validateKey(splitString[0]);
                if (keyValid) {
                    JSONArray jsonRestaurants = deSQL.fetchRestaurantsName(splitString[1]);
                    JSONObject jsonFinal = new JSONObject();
                    if (!jsonRestaurants.isEmpty()) {
                        jsonFinal.put("status code", 200);
                        jsonFinal.put("restaurants", jsonRestaurants);
                        PrintWriter out = response.getWriter();
                        out.print(jsonFinal);
                    } else {
                        PrintWriter out = response.getWriter();
                        out.print("status code: 400 (Error in fetching data)");
                    }
                } else {
                    PrintWriter out = response.getWriter();
                    out.print("status code: 400 (INVALID KEY ENTERED)");
                }
            }
        } else {
            PrintWriter out = response.getWriter();
            out.print("API key required for using the API");
        }
    }
}
