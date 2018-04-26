package tbs.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileReader;
import java.io.BufferedReader;

public class File {

	private String path;

	//These maps and lists store the theatre information read from the file
	private List<String> _theatreIDs = new ArrayList<String>();
	private Map<String, String> _mapTheatreSeats = new HashMap<String, String>();
	private Map<String, String> _mapTheatreFloorSpace = new HashMap<String, String>();

	public File(String filePath) {
		path = filePath;
	}

	public boolean formatChecker(String aLine) {
		//Checks if the line matches the correct line format
		return aLine.matches("THEATRE\\t\\w+\\t\\d{1,}\\t\\d{1,}");
	}

	int readLines() throws IOException, TBSExceptions {
		
		FileReader fileToRead = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileToRead);

		String aLine;
		int numberOfLines = 0;

		try {
			//Iterate through the lines of the file and count as each line is processed
			while ((aLine = bf.readLine()) != null) {
				
				// Check if the lines are formatted correctly
				if (!formatChecker(aLine)) {
					throw new TBSExceptions("ERROR: Incorrect format");
				}
				numberOfLines++;
			}
			//Check if there is no data in the file (no lines)
			if (numberOfLines == 0) {
				throw new TBSExceptions("ERROR: Incorrect format");
			}
			return numberOfLines;

		//Close the text reader
		} finally {
			bf.close();
		}
	}

	public void openFile() throws IOException, TBSExceptions {

		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);

		//Find the number of lines in the file and check if they are correctly formatted
		int numberOfLines = readLines();
		String[] textData = new String[numberOfLines];
		
		//Read each line and store the parts of information in different lists and maps
		for (int i = 0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();

			String[] parts = textData[i].split("\t");
			
			_theatreIDs.add(parts[1]);
			_mapTheatreSeats.put(parts[1], parts[2]);
			_mapTheatreFloorSpace.put(parts[1], parts[3]);
		}
		textReader.close();
	}

	//These methods return the data stored in the file to the server
	public List<String> saveTheatreIDs() {
		return _theatreIDs;
	}

	public Map<String, String> saveSeatingDim() {
		return _mapTheatreSeats;
	}

	public Map<String, String> saveFloorSpace() {
		return _mapTheatreFloorSpace;
	}
}
