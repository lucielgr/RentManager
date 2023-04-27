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
import java.time.LocalDate;

import static java.lang.Long.parseLong;


@WebServlet("/rents/modif")
public class RentsModifServlet extends HttpServlet {
    @Autowired
    private ReservationService reservationService;
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
        long idResa = parseLong(request.getParameter("id"));

        try {
            Reservation reservation = reservationService.findById(idResa);
            request.setAttribute("id",idResa);
            request.setAttribute("client_id_init", reservation.getClient().getId());
            request.setAttribute("vehicle_id_init", reservation.getVehicle().getId());
            request.setAttribute("debut_init", reservation.getDebut());
            request.setAttribute("fin_init", reservation.getFin());

            request.setAttribute("allClients", this.clientService.findAll());
            request.setAttribute("allVehicles", this.vehicleService.findAll());


        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/modif.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long idResa = parseLong(request.getParameter("id"));
        long idClient = parseLong(request.getParameter("client"));
        long idVehicle = parseLong(request.getParameter("car"));
        LocalDate debut = LocalDate.parse(request.getParameter("begin"));
        LocalDate fin = LocalDate.parse(request.getParameter("end"));

        try {
            Client client = clientService.findById(idClient);
            Vehicle vehicle = vehicleService.findById(idVehicle);

            Reservation reservation = new Reservation(idResa,client,vehicle,debut,fin);

            reservationService.update(reservation);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/rents");


    }
}

