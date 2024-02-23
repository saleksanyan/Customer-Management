package org.example;
//import com.google.gson.Gson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

//get a customer
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionFactory session_factory =
                HibernateConfig.getSessionFactory();
        Session s = session_factory.openSession();
        CustomerDao customerDao = new CustomerDao(s);
        List<Customer> customers = customerDao.getAll();
        String jsonCustomers = objectMapper.writeValueAsString(customers);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonCustomers);
        }
        s.flush();
        s.close();
        HibernateConfig.shutdown();
    }


//post a customer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionFactory session_factory =
                HibernateConfig.getSessionFactory();
        Session s = session_factory.openSession();
        CustomerDao customerDao = new CustomerDao(s);
        StringBuilder requestData = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestData.append(line);
            }
        }

        // Convert JSON data to Customer object
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Customer newCustomer = objectMapper.readValue(requestData.toString(), Customer.class);

        Customer createdCustomer = customerDao.createCustomer(newCustomer,s);

        // Respond with the created customer object
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String jsonCreatedCustomer = objectMapper.writeValueAsString(createdCustomer);
            out.print(jsonCreatedCustomer);
        }
        s.flush();
        s.close();
        HibernateConfig.shutdown();
    }

//delete a customer
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        SessionFactory session_factory =
                HibernateConfig.getSessionFactory();
        Session s = session_factory.openSession();
        CustomerDao customerDao = new CustomerDao(s);
        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int customerId = Integer.parseInt(idParameter);

            // Delete the customer based on the id
            boolean success = customerDao.deleteCustomer(customerId, s);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }finally {
            s.close();
            HibernateConfig.shutdown();
        }


    }


}
