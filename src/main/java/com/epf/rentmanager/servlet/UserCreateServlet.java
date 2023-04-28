package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
//import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;


@WebServlet("/users/create")
public class UserCreateServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String last_name = request.getParameter("last_name");
        String first_name = request.getParameter("first_name");
        String email = request.getParameter("email");
        LocalDate birth_date = LocalDate.parse(request.getParameter("birth_date"));
        Client client = new Client(last_name, first_name, birth_date, email);

        if(client.isLegal()){
            try {
                clientService.create(client);
                response.sendRedirect("/rentmanager/users");
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }

        if (!client.isLegal()) {
            String message = "Attention, l'utilisateur doit avoir au moins 18 ans.";
            request.setAttribute("alert_msg", message);
            request.setAttribute("last_name", last_name);
            request.setAttribute("first_name", first_name);
            request.setAttribute("email", email);
            request.setAttribute("naissance", birth_date);
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
        }






    }
}


