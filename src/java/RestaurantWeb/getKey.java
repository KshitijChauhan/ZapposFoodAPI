/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestaurantWeb;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Me
 */
@WebServlet(name = "getKey", urlPatterns = {"/getKey"})
public class getKey extends HttpServlet {

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
        String nextView;
        String B64 = "";
        String string = request.getParameter("Input1");
        String string2 = request.getParameter("Input2");
        if (string != null && string2 != null) {
            DataExtractSQL deSQL2 = new DataExtractSQL();
            deSQL2.saveKeyToTable(string, string2);
            String comboString = string + string2;
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(comboString.getBytes());
                B64 = DatatypeConverter.printBase64Binary(digest);
                B64 = B64.replace("=", "a").replace("/", "b").replace("+", "c");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(getKey.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.setAttribute("B64", B64);
            nextView = "result.jsp";
        } else {
            nextView = "index.jsp";
        }
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
}
