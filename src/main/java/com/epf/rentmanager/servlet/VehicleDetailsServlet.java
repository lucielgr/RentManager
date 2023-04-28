package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@WebServlet("/cars/details")
public class VehicleDetailsServlet extends HttpServlet {
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
            List<Reservation> reservations = this.reservationService.findResaByVehicleId(id);
            int reservationsCount = reservations.size();

            List<Client> clientsDoublons = new ArrayList<Client>();
            for (Reservation reservation : reservations) {
                Client client = reservation.getClient();
                clientsDoublons.add(client);
            }

            List<Client> clients = new ArrayList<>(new LinkedHashSet<>(clientsDoublons));

            int usersCount = clients.size();

            request.setAttribute("client", this.clientService.findById(id));
            request.setAttribute("reservations", reservations);
            request.setAttribute("reservationsCount", reservationsCount);
            request.setAttribute("users", clients);
            request.setAttribute("usersCount", usersCount);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp").forward(request, response);

    }
}
