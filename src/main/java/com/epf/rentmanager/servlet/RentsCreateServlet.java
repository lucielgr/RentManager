package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;


@WebServlet("/rents/create")
public class RentsCreateServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("allClients", this.clientService.findAll());
            request.setAttribute("allVehicles", this.vehicleService.findAll());

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idClient = Integer.parseInt(request.getParameter("client"));
        int idVehicle = Integer.parseInt(request.getParameter("car"));
        LocalDate begin_date = LocalDate.parse(request.getParameter("begin"));
        LocalDate end_date = LocalDate.parse(request.getParameter("end"));

        try {
            Client client = clientService.findById(idClient);
            Vehicle vehicle = vehicleService.findById(idVehicle);
            Reservation reservation = new Reservation(client, vehicle, begin_date, end_date);
            reservationService.create(reservation);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        response.sendRedirect("/rentmanager/rents");


    }
}
