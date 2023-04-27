package com.epf.rentmanager.servlet;


import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/cars/modif")
public class VehicleModifServlet extends HttpServlet {
    @Autowired
    private VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long idVehicle = Long.parseLong(request.getParameter("id"));
        try {
            Vehicle vehicle = vehicleService.findById(idVehicle);
            request.setAttribute("id",vehicle.getId());
            request.setAttribute("modele_init",vehicle.getModele());
            request.setAttribute("marque_init",vehicle.getConstructeur());
            request.setAttribute("seats_init",vehicle.getNb_places());

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/modif.jsp").forward(request, response);

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long idVehicle = Long.parseLong(request.getParameter("id"));
        String marque = request.getParameter("manufacturer");
        String modele = request.getParameter("modele");
        int nb_places = Integer.parseInt(request.getParameter("seats"));
        Vehicle vehicle = new Vehicle(idVehicle,marque,modele,nb_places);
        try {
            vehicleService.update(vehicle);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }


        response.sendRedirect("/rentmanager/cars");


    }
}

