package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;


@WebServlet("/users/modif")
public class UserModifServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long idClient = Long.parseLong(request.getParameter("id"));
        try {
            Client client = clientService.findById(idClient);
            request.setAttribute("id",client.getId());
            request.setAttribute("last_name_init",client.getNom());
            request.setAttribute("first_name_init",client.getPrenom());
            request.setAttribute("email_init",client.getEmail());
            request.setAttribute("birth_date_init",client.getNaissance());

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/modif.jsp").forward(request, response);

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long idClient = Long.parseLong(request.getParameter("id"));
        String last_name = request.getParameter("last_name");
        String first_name = request.getParameter("first_name");
        String email = request.getParameter("email");
        LocalDate birth_date = LocalDate.parse(request.getParameter("birth_date"));
        Client client = new Client(idClient,last_name,first_name,birth_date,email);
        try {
            clientService.update(client);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        response.sendRedirect("/rentmanager/users");


    }
}
