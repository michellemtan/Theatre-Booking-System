package tbs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class TBSServerImpl implements TBSServer {

	// Server stores the IDSs and names of the information added by the user
	private List<String> _theatreIDs = new ArrayList<String>();
	private List<String> _artistNames = new ArrayList<String>();
	private List<String> _artistIDs = new ArrayList<String>();
	private List<String> _performanceIDs = new ArrayList<String>();
	private List<String> _actIDs = new ArrayList<String>();

	private static Map<String, String> _mapTheatreSeats = new HashMap<String, String>();
	private static Map<String, String> _mapTheatreFloorSpace = new HashMap<String, String>();

	@Override
	public String initialise(String path) {

		String checkFile = new String("");

		try {
			File file = new File(path);
			file.openFile();

			// Save theatre data from file into server
			_theatreIDs = file.saveTheatreIDs();
			_mapTheatreSeats = file.saveSeatingDim();
			_mapTheatreFloorSpace = file.saveFloorSpace();

		} catch (IOException e) {
			checkFile = "ERROR: There is no file associated with the path";

		} catch (TBSExceptions IncorrectFormatException) {
			checkFile = IncorrectFormatException.getMessage();
		}
		return checkFile;
	}

	@Override
	public List<String> getTheatreIDs() {
		// Sort out theatre IDs into lexicographical order and return the list
		List<String> theatreIDs = _theatreIDs;
		java.util.Collections.sort(theatreIDs);

		return theatreIDs;
	}

	@Override
	public List<String> getArtistIDs() {
		// Sort out artist IDs into lexicographical order and return the list
		List<String> artistIDs = _artistIDs;
		java.util.Collections.sort(artistIDs);

		return artistIDs;
	}

	@Override
	public List<String> getArtistNames() {
		// Sort out artist names into lexicographical order and return the list
		List<String> artistNames = _artistNames;
		java.util.Collections.sort(artistNames);

		return artistNames;
	}

	@Override
	public List<String> getActIDsForArtist(String artistID) {

		List<String> artistsActIDs = new ArrayList<String>();
		Artist tempArtist = new Artist();

		// Check for any problems with the artist ID
		if (artistID.isEmpty()) {
			String message = "ERROR: No artist ID";
			artistsActIDs.add(message);
		} else if (!tempArtist.checkArtistID(artistID, _artistIDs)) {
			String message = "ERROR: Artist not found";
			artistsActIDs.add(message);

		} else {
			// Go through the list of acts and find the act IDs linked to the artist ID
			artistsActIDs = tempArtist.getArtistsActIDs(_actIDs, artistID);
		}
		return artistsActIDs;
	}

	@Override
	public List<String> getPeformanceIDsForAct(String actID) {

		List<String> performanceIDs = new ArrayList<String>();
		Act tempAct = new Act();

		// Check if there is a problem with the act ID
		if (actID.equals("")) {
			performanceIDs.add("ERROR: actID is empty");
		} else if (tempAct.findArtistID(actID) == null) {
			performanceIDs.add("ERROR: actID is invalid");

		} else {
			Performance tempPform = new Performance();
			// Get the performance IDs of an act from a hash map using the act ID as a key
			performanceIDs = tempPform.getPerformanceIDsForAct(actID);
		}
		return performanceIDs;
	}

	@Override
	public List<String> getTicketIDsForPerformance(String performanceID) {

		List<String> tickets = new ArrayList<String>();

		// Check if there is a problem with the performance ID
		if (performanceID.equals("")) {
			tickets.add("ERROR: No performance ID");
		} else if (!_performanceIDs.contains(performanceID)) {
			tickets.add("ERROR: No performance with specified ID");
		}

		Ticket newTicket = new Ticket();

		// Check if there are any tickets issued for the performance ID
		if (newTicket.checkIssued(performanceID)) {

			// If there are tickets issued, return a list of the ticket IDs
			tickets = newTicket.returnTickets(performanceID);
			java.util.Collections.sort(tickets);
		}
		return tickets;
	}

	@Override
	public String addArtist(String name) {

		String artistID;

		// If the artist name is empty, return an error message
		if (name.isEmpty()) {
			return ("ERROR: No name entered");
		}

		Artist tempArtist = new Artist();

		// Check if the server's list of artist names already contains the artist name
		if (tempArtist.checkArtistName(name, _artistNames)) {
			return ("ERROR: Artist already exists");

		} else {
			// Create a new artist
			Artist newArtist = new Artist(name);
			artistID = newArtist.getArtistID();

			// Store their name and ID in the server
			_artistNames.add(name);
			_artistIDs.add(artistID);

			return artistID;
		}
	}

	@Override
	public String addAct(String title, String artistID, int minutesDuration) {

		Artist temp = new Artist();

		// Check if the title is empty
		if (title.isEmpty()) {
			return ("ERROR: Title is empty");
			// Check if the artist ID is empty
		} else if (artistID.isEmpty()) {
			return ("ERROR: Artist not found");
			// Check if there is an artist stored in the server for the artist ID
		} else if (!temp.checkArtistID(artistID, _artistIDs)) {
			return ("ERROR: Artist not found");
			// Check if the duration is non-negative)
		} else if (minutesDuration <= 0) {
			return ("ERROR: Time is negative");

		} else {
			// Create a new act and act ID
			Act newAct = new Act(title, artistID, minutesDuration);
			String newActID = newAct.createActID();

			// Add the new act's details into the server
			_actIDs.add(newActID);

			return newActID;
		}
	}

	@Override
	public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr,
			String cheapSeatsStr) {

		String performanceID = "";
		List<String> theatreIDs = this.getTheatreIDs();

		Act tempAct = new Act();
		Performance tempPform = new Performance();
		String artistID = tempAct.findArtistID(actID);

		try {
			// Check if any of the parameters are wrong
			if (artistID.equals(null)) {
				return performanceID = "ERROR: No artist exists for the act ID";
			} else if (!theatreIDs.contains(theatreID)) {
				return performanceID = "ERROR: No theatre exists for the theatre ID";
			} else if (!premiumPriceStr.matches("\\$\\d+")) {
				return performanceID = "ERROR: Prices are invalid";
			} else if (!cheapSeatsStr.matches("\\$\\d+")) {
				return performanceID = "ERROR: Prices are invalid";
			} else if (!tempPform.timeFormatChecker(startTimeStr)) {
				return performanceID = "ERROR: Incorrect time format not ISO8601";

			} else {
				// Create a new performance and update the list of performance IDs stored in the
				// server
				Performance newPerformance = new Performance(actID, theatreID, startTimeStr);
				performanceID = newPerformance.getPerformanceID();
				_performanceIDs.add(performanceID);

				// Initialise the seating for the performance for when tickets are issued
				String seatingDim = _mapTheatreSeats.get(theatreID);
				Seats newSeats = new Seats(performanceID, seatingDim);
				newSeats.initialiseSeating();

				// Initialise the sales log for the performance for when tickets are issued
				Sale newSale = new Sale(performanceID, premiumPriceStr, cheapSeatsStr);
				newSale.initialiseSales(seatingDim);
			}

		} catch (NullPointerException e) {
			performanceID = "ERROR: No artist exists for the act ID";
		}
		return performanceID;
	}

	@Override
	public String issueTicket(String performanceID, int rowNumber, int seatNumber) {

		String ticketID;

		try {
			// Check if there is a performance for the ID
			if (!_performanceIDs.contains(performanceID)) {
				throw new TBSExceptions("ERROR: No seat found");
			}

			// Get the theatre ID and the seating dimensions for the performance
			Performance currentPerformance = new Performance();
			String theatreID = currentPerformance.getTheatreID(performanceID);
			String seatingDim = _mapTheatreSeats.get(theatreID);

			// Check if the specified seat location exists
			if (seatNumber > Integer.parseInt(seatingDim)) {
				throw new TBSExceptions("ERROR: The seat does not exist");
			} else if (rowNumber > Integer.parseInt(seatingDim)) {
				throw new TBSExceptions("ERROR: The seat does not exist");
			}

			// Create the seat ID for the specified location
			String seatID = rowNumber + "\t" + seatNumber;
			Seats seatingPlan = new Seats();

			// Check if the specified seat is available
			if (seatingPlan.isAvailable(performanceID, seatID)) {
				// If available, issue a ticket for the seat
				Ticket newTicket = new Ticket(performanceID, rowNumber, seatNumber);
				ticketID = newTicket.getTicketID();

				// Issue the specified seat and generate the sale of the seat
				seatingPlan.issueSeat(performanceID, seatID);
				Sale newSale = new Sale();
				newSale.updateSales(rowNumber, seatNumber, performanceID);

			} else {
				// If the seat is unavailable, return an error message
				throw new TBSExceptions("ERROR: The seat is booked");
			}

		} catch (TBSExceptions SeatErrorException) {
			ticketID = SeatErrorException.getMessage();
		}
		return ticketID;
	}

	@Override
	public List<String> seatsAvailable(String performanceID) {

		List<String> availableSeats = new ArrayList<String>();

		try {
			// Check if there is a performance for the specified ID
			if (!_performanceIDs.contains(performanceID)) {
				throw new TBSExceptions("ERROR: No performance with specified ID");
			}

			// List the seats available for the performance ID
			Seats seatingPlan = new Seats();
			availableSeats = seatingPlan.listAvailableSeats(performanceID);

		} catch (TBSExceptions IncorrectIDException) {
			availableSeats.add(IncorrectIDException.getMessage());
		}
		// Return list of seats available - if there are none available, an empty list
		// will be returned
		return availableSeats;
	}

	@Override
	public List<String> salesReport(String actID) {

		String sales;
		String performanceID;
		String numberOfTickets;

		List<String> salesReport = new ArrayList<String>();
		List<String> listPerformanceIDs = new ArrayList<String>();

		// Check if there is an act with the specified ID
		if (!_actIDs.contains(actID)) {
			salesReport.add("ERROR: No act exists");
			return salesReport;
		}

		Performance newPerformance = new Performance();
		Ticket newTicket = new Ticket();

		// Get a list of all the performances for the act
		listPerformanceIDs = newPerformance.getPerformanceIDsForAct(actID);

		// Check if there are performances for the act
		if (listPerformanceIDs != null && !listPerformanceIDs.isEmpty()) {

			// Iterate through all the performances for the act
			for (int i = 0; i < listPerformanceIDs.size(); i++) {

				// Get performance ID and the number of tickets sold for the performance
				performanceID = listPerformanceIDs.get(i);
				numberOfTickets = newTicket.getNumberOfTicketsSold(performanceID);

				// Create strings representing the sales report for each performance of the act
				Sale newSale = new Sale();
				sales = performanceID + "\t" + newPerformance.getStartTime(performanceID) + "\t" + numberOfTickets
						+ "\t$" + newSale.calculateTotalSales(performanceID);
				salesReport.add(sales);
			}

		} else {
			// If there are no performances scheduled for the act
			salesReport.add("ERROR: No performances found for the act");
		}
		return salesReport;
	}

	@Override
	public List<String> dump() {
		return null;
	}
}
