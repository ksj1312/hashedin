package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Data;

public class ReadTSV {

	public List<Data> readBooksFromCSV(String fileName) {

		Path pathToFile = Paths.get(fileName);
		List<Data> players = new ArrayList<>();
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			boolean flag = true;
			while (line != null) {
				if (flag) {
					flag = false;
					line = br.readLine();
					continue;
				}
				String[] attributes = line.split("	");
				Data player = createPlayer(attributes);
				players.add(player);
				line = br.readLine();
			}
		} catch (IOException ioe) {
//			ioe.printStackTrace();
		}
		return players;
	}

	private static Data createPlayer(String[] metadata) {

		String uuid = metadata[0];
		String tstamp = metadata[1];
		String source = metadata[2];
		String date = metadata[3];
		String event_type = metadata[4];
		String event_category = metadata[5];
		String event_action = metadata[6];
		String event_label = metadata[7];
		int event_value = Integer.parseInt(metadata[8]);
		String created_at = metadata[9];
		String last_updated_at = metadata[10];
		String location = metadata[11];
		String id = metadata[12];

		return new Data(uuid, tstamp, source, date, event_type, event_category, event_action, event_label,
				event_value, created_at, last_updated_at, location, id);
	}
}
