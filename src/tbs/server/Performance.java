package tbs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Performance {

	// Integer for the performance ID
	private static int _newPerformanceID = 0;

	// Map for each act and their performances
	private static Map<String, List<String>> _mapActPform = new HashMap<String, List<String>>();

	// Maps for performances and their theatre locations and start times
	private static Map<String, String> _mapPformTheatre = new HashMap<String, String>();
	private static Map<String, String> _mapPformStart = new HashMap<String, String>();

	public Performance() {
	}

	public Performance(String actID, String theatreID, String startTimeStr) {

		// Create a new ID for the new performance
		_newPerformanceID++;
		String performanceID = getPerformanceID();

		// Store the theatre ID and start time in the server
		_mapPformTheatre.put(performanceID, theatreID);
		_mapPformStart.put(performanceID, startTimeStr);

		// Check if the act has had any performances scheduled
		boolean isAlreadyAdded = checkScheduled(actID);

		// Update the list of performances scheduled for an act
		updateList(performanceID, isAlreadyAdded, actID);
	}

	public boolean checkScheduled(String actID) {
		boolean isAlreadyAdded = true;
		// Check if the act has had performances scheduled previously
		if (isAlreadyAdded != _mapActPform.containsKey(actID)) {
			isAlreadyAdded = false;
		}
		// If performances have been scheduled, return true otherwise return false
		return isAlreadyAdded;
	}

	public void updateList(String performanceID, boolean isAdded, String actID) {

		List<String> actsPerformanceIDs = new ArrayList<String>();

		if (isAdded == false) {
			// If there are NO performances scheduled for the act, create a new list
			actsPerformanceIDs.add(performanceID);
		} else {
			// If there are performances scheduled for the act, add onto the existing list
			actsPerformanceIDs = _mapActPform.get(actID);
			actsPerformanceIDs.add(performanceID);
		}
		// Update the list of performances scheduled for the act
		_mapActPform.put(actID, actsPerformanceIDs);
	}

	// Check if the performance's time is in the format yyyy-mm-ddThh:mm
	public boolean timeFormatChecker(String startTime) {
		return startTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}");
	}

	// Return the performance ID
	public String getPerformanceID() {
		return ("PFORM" + Integer.toString(_newPerformanceID));
	}

	// Find the corresponding theatre ID for the performance ID
	public String getTheatreID(String performanceID) {
		return _mapPformTheatre.get(performanceID);
	}

	// Find the corresponding start time for the performance
	public String getStartTime(String performanceID) {
		return _mapPformStart.get(performanceID);
	}

	public List<String> getPerformanceIDsForAct(String actID) {

		List<String> listPerformanceIDs = new ArrayList<String>();

		// Check if the act has had performances scheduled
		if (checkScheduled(actID)) {
			listPerformanceIDs = _mapActPform.get(actID);
		}
		// This will return the act's performance IDs or an empty list if there are no
		// performances
		return listPerformanceIDs;
	}
}
