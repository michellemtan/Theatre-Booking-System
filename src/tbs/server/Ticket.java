package tbs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket {

	private String _newTicketID;

	// Stores the list of tickets issued for each performance and which performances
	// have had tickets issued
	private static Map<String, List<String>> _mapPformTickets = new HashMap<String, List<String>>();
	private static List<String> _performancesTicketed = new ArrayList<String>();

	public Ticket() {
	}

	public Ticket(String performanceID, int rowNumber, int seatNumber) {

		List<String> issuedTickets = new ArrayList<String>();

		// Create a new ID for the ticket based on the performance, row number and seat
		// number
		_newTicketID = "TKT" + performanceID.replace("PFORM", "") + Integer.toString(rowNumber)
		+ Integer.toString(seatNumber);

		// If the performance already has tickets issued, use the existing list of
		// tickets for the performance
		if (checkIssued(performanceID)) {
			issuedTickets = returnTickets(performanceID);

		} else {
			// If the performance does not have any tickets issued, create a new list of
			// tickets for the performance
			issuedTickets = new ArrayList<String>();
			_performancesTicketed.add(performanceID);
		}

		// Add the new ticket ID onto the list of tickets for the performance
		issuedTickets.add(_newTicketID);
		_mapPformTickets.put(performanceID, issuedTickets);
	}

	public boolean checkIssued(String performanceID) {
		// Check if there is a list of performances that have been issued tickets for
		if (_performancesTicketed != null && !_performancesTicketed.isEmpty()) {

			// Check if the performance has been issued tickets for
			if (_performancesTicketed.contains(performanceID)) {
				return true;
			} else {
				return false;
			}
		} else {
			// If there is no list or the list is empty, there are no tickets issued for the
			// performance ID
			return false;
		}
	}

	public String getTicketID() {
		return _newTicketID;
	}

	public List<String> returnTickets(String performanceID) {
		// Return the list of tickets issued for the performanceID
		return _mapPformTickets.get(performanceID);
	}

	public String getNumberOfTicketsSold(String performanceID) {

		List<String> ticketsSold = new ArrayList<String>();
		ticketsSold = _mapPformTickets.get(performanceID);

		// Check if there have been any tickets sold for the performance ID
		if (ticketsSold != null && !ticketsSold.isEmpty()) {
			int numberSold = 0;

			// Iterate through the tickets sold and count how many have been sold
			for (int i = 0; i < ticketsSold.size(); i++) {
				numberSold++;
			}

			// Return the number of tickets sold
			return Integer.toString(numberSold);

		} else {
			// If the list is empty or does not exist, there are no tickets sold
			return "0";
		}
	}
}
