/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Me
 */
@WebServlet(name = "MenuWebService", urlPatterns = {"/Menu/*"})
public class MenuWebService extends HttpServlet {

    DataExtractSQL deSQL = null;

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
            String restID = (request.getPathInfo()).substring(1);
            if (!restID.equals("")) {
                if (!restID.contains("&")) {
                    JSONArray jsonMenu = deSQL.fetchMenu(restID);
                    JSONObject jsonFinal1 = new JSONObject();
                    if (!jsonMenu.isEmpty()) {
                        jsonFinal1.put("status code", 200);
                        jsonFinal1.put("menu", jsonMenu);
                        PrintWriter out = response.getWriter();
                        out.print(jsonFinal1);
                    } else {
                        PrintWriter out = response.getWriter();
                        out.print("status code: 400 (Error in fetching data)");
                    }
                } else {
                    JSONArray jsonMenu = deSQL.fetchCategoryMenu(restID);
                    JSONObject jsonFinal1 = new JSONObject();
                    if (!jsonMenu.isEmpty()) {
                        jsonFinal1.put("status code", 200);
                        jsonFinal1.put("menu", jsonMenu);
                        PrintWriter out = response.getWriter();
                        out.print(jsonFinal1);
                    } else {
                        PrintWriter out = response.getWriter();
                        out.print("status code: 400 (Error in fetching data)");
                    }
                }
            } else {
                PrintWriter out = response.getWriter();
                out.println("Yikes !! Need to provide atleast the restaurant ID !!");
            }
        } else {
            PrintWriter out = response.getWriter();
            out.println("Yikes !! Need to provide atleast the restaurant ID !!");
        }
    }
}
