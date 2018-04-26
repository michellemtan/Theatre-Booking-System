package tbs.server;

import java.util.HashMap;
import java.util.Map;

public class Act {

	// Integer for the act ID
	private static int _ActIDint = 0;

	// Maps for each act and their information
	private static Map<String, String> _mapActArtistIDs = new HashMap<String, String>();
	private static Map<String, String> _mapActTitles = new HashMap<String, String>();
	private static Map<String, Integer> _mapActDurations = new HashMap<String, Integer>();

	public Act() {
	}

	public Act(String title, String artistID, int minutesDuration) {
		// Create the new act's ID
		_ActIDint++;
		String actID = "ACT" + Integer.toString(_ActIDint);

		// Store each act's artists, titles and durations using the act ID as a key
		_mapActArtistIDs.put(actID, artistID);
		_mapActTitles.put(actID, title);
		_mapActDurations.put(actID, minutesDuration);
	}

	// Return the actID after it has been created
	public String createActID() {
		return "ACT" + Integer.toString(_ActIDint);
	}

	// Return the artist ID when an actID is given
	public String findArtistID(String actID) {
		String artistID = _mapActArtistIDs.get(actID);
		return artistID;
	}
}
