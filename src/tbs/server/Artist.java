package tbs.server;

import java.util.ArrayList;
import java.util.List;

public class Artist {

	// Integer for the artist ID
	private static int _newID = 0;

	public Artist() {
	}

	public Artist(String name) {
		// Create a new ID for each new artist
		_newID++;
	}

	// Return the artistID
	public String getArtistID() {
		return "ART" + Integer.toString(_newID);
	}

	public boolean checkArtistName(String artistName, List<String> artistNames) {
		// Checks if there is already an artist with the same name
		boolean isAdded = false;
		String name;

		if (!artistNames.isEmpty()) {
			// Iterate through the list of artists' names
			for (int i = 0; i < artistNames.size(); i++) {
				name = artistNames.get(i);

				// Check if there is already the same name
				if (artistName.equalsIgnoreCase(name)) {
					isAdded = true;
				}
			}
		}
		return isAdded;
	}

	public boolean checkArtistID(String artistID, List<String> artistIDs) {
		// Check if there is a list of artistIDs
		if (artistIDs != null && !artistIDs.isEmpty()) {

			// Check if there is already an artist with the same ID
			if (artistIDs.contains(artistID)) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	public List<String> getArtistsActIDs(List<String> actIDs, String artistID) {

		List<String> artistsActIDs = new ArrayList<String>();
		String id, actID;
		Act tempAct = new Act();

		for (int i = 0; i < actIDs.size(); i++) {
			// Find the artistID for every act
			actID = actIDs.get(i);
			id = tempAct.findArtistID(actID);

			// If the IDs match, add the act to the artist's list of acts
			if (id.equals(artistID)) {
				artistsActIDs.add(actIDs.get(i));
			}
		}
		// Return the list of the IDs for all the acts by the artist with the specified
		// ID in lexicographical order
		java.util.Collections.sort(artistsActIDs);
		return artistsActIDs;
	}
}
