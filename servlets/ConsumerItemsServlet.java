/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;

/**
 *
 * @author lijoy
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DAO.ConsumerItemDAO;
import Model.ConsumerItem;

/**
 * Servlet implementation class ConsumerItemsServlet
 */
public class ConsumerItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ConsumerItemDAO consumerItemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        consumerItemDAO = new ConsumerItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "delete":
                    deleteConsumerItem(request, response);
                    break;
                case "transferToConsumer":
                    transferToConsumer(request, response);
                    break;
                case "transferToCharity":
                    transferToCharity(request, response);
                    break;
                case "purchase":
                    deleteConsumerItem(request, response);
                    break;
                case "list":
                default:
                    listConsumerItems(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listConsumerItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<ConsumerItem> listConsumerItems = consumerItemDAO.selectAllConsumerItems();
        request.setAttribute("listConsumerItems", listConsumerItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("consumer_items.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteConsumerItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        consumerItemDAO.deleteConsumerItem(id);
        response.sendRedirect("ConsumerItemsServlet?action=list");
    }

    private void transferToConsumer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
    }
    private void transferToCharity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            throw new ServletException("Missing id parameter for transfer to charity");
        }

        int id = Integer.parseInt(idParam);
        consumerItemDAO.transferToCharity(id);
        response.sendRedirect("ConsumerItemsServlet?action=list");
    }

}