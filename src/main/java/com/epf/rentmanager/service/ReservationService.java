package com.epf.rentmanager.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    public static ReservationService instance;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

//    public static ReservationService getInstance() {
//        if (instance == null) {
//            instance = new ReservationService();
//        }
//        return instance;
//    }


    public long create(Reservation reservation) throws ServiceException {
        try{
            return reservationDao.create(reservation);
        }catch(DaoException e){
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public void delete(Reservation reservation) throws ServiceException {
        try{
            reservationDao.delete(reservation);
        }catch(DaoException e){
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try{
            return reservationDao.findAll();
        }catch(DaoException e){
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public int count() throws DaoException, SQLException {
        return reservationDao.count();
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException{
        try {
            return reservationDao.findResaByClientId(id);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Reservation reservation) throws ServiceException{
        try {
            reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try{
            return reservationDao.findById(id);
        }catch(DaoException e){
            e.printStackTrace();
            throw new ServiceException();
        }
    }
}
