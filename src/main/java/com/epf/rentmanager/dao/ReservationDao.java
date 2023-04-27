package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

	@Autowired
	private ClientDao clientDao;
	private ClientService clientService;
	@Autowired
	private  VehicleDao vehicleDao;
	private VehicleService vehicleService;
	private ReservationDao(ClientDao clientDao, VehicleDao vehicleDao) {
		this.clientDao = clientDao;
		this.vehicleDao = vehicleDao;
	}

	public ReservationDao() {
	}
	//	public static ReservationDao getInstance() {
//		if(instance == null) {
//			instance = new ReservationDao();
//		}
//		return instance;
//	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String FIND_RESERVATION_QUERY = "SELECT client_id, vehicle_id, debut, fin FROM Reservation WHERE id=?;";
	private static final String COUNT_RESERVATIONS_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, debut=?, fin=? WHERE id=?;";

	public long create(Reservation reservation) throws DaoException {
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, reservation.getClient().getId());
			ps.setLong(2, reservation.getVehicle().getId());
			ps.setDate(3, Date.valueOf(reservation.getDebut()));
			ps.setDate(4, Date.valueOf(reservation.getFin()));
			ps.execute();
			ResultSet resultSet = ps.getGeneratedKeys();

			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				return id;
			}

			ps.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation.getId();
	}
	
	public void delete(Reservation reservation) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY);
			ps.setLong(1, reservation.getId());
			ps.executeUpdate();
			ps.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}
	}


	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			preparedStatement.setLong(1, clientId);
			ResultSet rs = preparedStatement.executeQuery();

			while(rs.next()){
				long id = rs.getLong("id");
				Client client = this.clientDao.findById(clientId);
				long vehicle_id =rs.getLong("vehicle_id");
				Vehicle vehicle = this.vehicleDao.findById(vehicle_id);
				LocalDate debut = rs.getDate("debut").toLocalDate();
				LocalDate fin = rs.getDate("fin").toLocalDate();

				reservations.add(new Reservation(id, client, vehicle, debut, fin));
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}

		return reservations;
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		return new ArrayList<Reservation>();
	}

	public List<Reservation> findAll() throws DaoException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try {
			Connection connection = ConnectionManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(FIND_RESERVATIONS_QUERY);

			while(rs.next()){
				int id = rs.getInt("id");
				long idClient = rs.getLong("client_id");
				long idVehicle = rs.getLong("vehicle_id");
				Client client = clientDao.findById(idClient);
				Vehicle vehicle = vehicleDao.findById(idVehicle);
				LocalDate debut = rs.getDate("debut").toLocalDate();
				LocalDate fin = rs.getDate("fin").toLocalDate();

				reservations.add(new Reservation(id, client, vehicle, debut, fin));
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}

		return reservations;
	}

	public int count() throws DaoException, SQLException {
		Connection connection = ConnectionManager.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(COUNT_RESERVATIONS_QUERY);
		rs.next();
		return rs.getInt("count");
	}

	public void update(Reservation reservation) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATION_QUERY);
			ps.setLong(1, reservation.getClient().getId());
			ps.setLong(2, reservation.getVehicle().getId());
			ps.setString(3, String.valueOf(reservation.getDebut()));
			ps.setString(4, String.valueOf(reservation.getFin()));
			ps.setLong(5, reservation.getId());

			ps.executeUpdate();

			ps.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Reservation findById(long id) throws DaoException {
		Reservation reservation = new Reservation();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_RESERVATION_QUERY);
			preparedStatement.setLong(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()){
				reservation.setId(id);
				Long idClient = rs.getLong("client_id");
				Client client = clientDao.findById(idClient);
				Long idVehicle = rs.getLong("vehicle_id");
				Vehicle vehicle = vehicleDao.findById(idVehicle);

				reservation.setClient(client);
				reservation.setVehicle(vehicle);
				reservation.setDebut(rs.getDate("debut").toLocalDate());
				reservation.setFin(rs.getDate("fin").toLocalDate());

			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation;
	}
}
