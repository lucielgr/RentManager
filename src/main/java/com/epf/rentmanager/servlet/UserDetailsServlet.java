package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/details")
public class UserDetailsServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
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
            long id = Long.parseLong(request.getParameter("id"));
            List<Reservation> reservations = this.reservationService.findResaByClientId(id);
            int reservationsCount = reservations.size();

            List< Vehicle>vehicles = new ArrayList<Vehicle>();
            for (Reservation reservation:reservations) {
                Vehicle vehicle = reservation.getVehicle();
                vehicles.add(vehicle);
            }
            int vehiclesCount = vehicles.size();

            request.setAttribute("client", this.clientService.findById(id));
            request.setAttribute("reservations", reservations);
            request.setAttribute("reservationsCount",reservationsCount);
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("vehiclesCount",vehiclesCount);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);

    }
}