package tbs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sale {

	private String _performanceID;
	private int _seatingDim;

	private int _premiumPrice;
	private int _cheapPrice;

	private int _numberOfPremiumTickets;
	private int _numberOfCheapTickets;

	// These maps store each performance's ticket prices, amount of tickets sold
	// (cheap and premium) and the seating dimensions for premium and non-premium
	// rows
	private static Map<String, List<Integer>> mapPformPrices = new HashMap<String, List<Integer>>();
	private static Map<String, Integer> mapPformSeatingDim = new HashMap<String, Integer>();
	private static Map<String, Integer> mapPformCheapTickets = new HashMap<String, Integer>();
	private static Map<String, Integer> mapPformPremiumTickets = new HashMap<String, Integer>();

	public Sale() {
	}

	public Sale(String performanceID, String premiumPriceStr, String cheapSeatsStr) {

		List<Integer> seatPrices = new ArrayList<Integer>();
		_performanceID = performanceID;

		_cheapPrice = Integer.parseInt(premiumPriceStr.replace("$", ""));
		_premiumPrice = Integer.parseInt(cheapSeatsStr.replace("$", ""));

		// Store the ticket pricing (premium and cheap) of each performance
		seatPrices.add(_premiumPrice);
		seatPrices.add(_cheapPrice);
		mapPformPrices.put(performanceID, seatPrices);
	}

	// Initialise the sales log for each performance when it is scheduled
	public void initialiseSales(String seatingDim) {

		_seatingDim = Integer.parseInt(seatingDim);
		mapPformSeatingDim.put(_performanceID, _seatingDim);
		
		_numberOfPremiumTickets = 0;
		mapPformPremiumTickets.put(_performanceID, _numberOfPremiumTickets);
		
		_numberOfCheapTickets = 0;
		mapPformCheapTickets.put(_performanceID, _numberOfCheapTickets);

	}

	public void updateSales(int rowNumber, int seatNumber, String performanceID) {

		_seatingDim = mapPformSeatingDim.get(performanceID);
		int premiumRows = _seatingDim / 2;

		// Check if the ticket sold is for a premium seat or a regular seat
		if (rowNumber <= premiumRows) {
			// Update the number of premium tickets sold for the performance
			_numberOfPremiumTickets = mapPformPremiumTickets.get(performanceID);
			_numberOfPremiumTickets++;
			mapPformPremiumTickets.put(performanceID, _numberOfPremiumTickets);

		} else {
			// Update the number of cheap tickets sold for the performance
			_numberOfCheapTickets = mapPformCheapTickets.get(performanceID);
			_numberOfCheapTickets++;
			mapPformCheapTickets.put(performanceID, _numberOfCheapTickets);
		}
	}

	public String calculateTotalSales(String performanceID) {

		int totalSales;
		// Get the number of tickets sold for premium and cheap seats
		_numberOfPremiumTickets = mapPformPremiumTickets.get(performanceID);
		_numberOfCheapTickets = mapPformCheapTickets.get(performanceID);

		// Get the premium seat and cheap seat price for the performance
		List<Integer> seatPrices = new ArrayList<Integer>();
		seatPrices = mapPformPrices.get(performanceID);
		_premiumPrice = seatPrices.get(0);
		_cheapPrice = seatPrices.get(1);

		// Calculate the total sales based on the tickets sold and their prices
		totalSales = _cheapPrice * _numberOfCheapTickets + _premiumPrice * _numberOfPremiumTickets;
		return Integer.toString(totalSales);
	}
}
