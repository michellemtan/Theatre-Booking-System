package tbs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seats {

	private String _performanceID;
	private String _seatingDim;

	// Maps for the available and unavailable seats of a performance
	private static Map<String, List<String>> _mapPformAvailableSeats = new HashMap<String, List<String>>();
	private static Map<String, List<String>> _mapPformBookedSeats = new HashMap<String, List<String>>();

	public Seats() {
	}

	public Seats(String performanceID, String seatingDim) {
		_performanceID = performanceID;
		_seatingDim = seatingDim;
	}

	// When a performance is scheduled, initialise all the seats to be available
	public void initialiseSeating() {

		List<String> newSeats = new ArrayList<String>();
		String seatID;

		int rows = Integer.parseInt(_seatingDim);
		int cols = rows;

		// Iterate through each row and seat of the theatre and add the seats to a list
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				seatID = Integer.toString(i) + "\t" + Integer.toString(j);
				newSeats.add(seatID);
			}
		}
		// Store the list of available seats using the performance ID as a key
		_mapPformAvailableSeats.put(_performanceID, newSeats);

		// Create an empty list of sold seats using the performance ID as a key
		List<String> issuedSeats = new ArrayList<String>();
		_mapPformBookedSeats.put(_performanceID, issuedSeats);
	}

	public void issueSeat(String performanceID, String seatID) {

		List<String> availableSeats = new ArrayList<String>();
		List<String> issuedSeats = new ArrayList<String>();

		// Get the list of issued seats and add the newly issued seat to the list
		issuedSeats = _mapPformBookedSeats.get(performanceID);
		issuedSeats.add(seatID);
		_mapPformBookedSeats.put(performanceID, issuedSeats);

		// Get the list of available seats and remove the issued seat from the list
		availableSeats = _mapPformAvailableSeats.get(performanceID);
		availableSeats.remove(seatID);
		_mapPformAvailableSeats.put(performanceID, availableSeats);
	}

	// List all the available seats for the performance
	public List<String> listAvailableSeats(String performanceID) {
		return _mapPformAvailableSeats.get(performanceID);
	}

	public boolean isAvailable(String performanceID, String seatID) {

		List<String> availableSeats = new ArrayList<String>();
		availableSeats = _mapPformAvailableSeats.get(performanceID);

		// Check if the list of available seats contains the specified seat
		if (availableSeats.contains(seatID)) {
			return true;
		} else {
			return false;
		}
	}
}
