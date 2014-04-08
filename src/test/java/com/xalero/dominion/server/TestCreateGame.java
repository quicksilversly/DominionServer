/*
 * Copyright 2014 jonathan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xalero.dominion.server;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;

/**
 *
 * @author jonathan
 */
public class TestCreateGame {

	@Test
	public void testCreateGame() throws IOException {
		final int port = 6780;
		final DominionServer server = new DominionServer(port);

		new Thread(new Runnable() {
			@Override
			public void run() {
				server.startServer();
			}
		}).start();

		GameSettingsDto gameSettingsDto = new GameSettingsDto();

		List<String> cards = new ArrayList<>();
		cards.add("adventurer");
		cards.add("market");
		cards.add("chancellor");
		cards.add("witch");
		cards.add("woodcutter");
		cards.add("workshop");
		cards.add("village");
		cards.add("thief");
		cards.add("throne room");
		cards.add("spy");

		gameSettingsDto.setGameCards(cards);
		gameSettingsDto.setNumCompPlayers(2);
		gameSettingsDto.setNumHumanPlayers(2);

		Gson gson = new GsonBuilder().create();
		String jsonSettings = gson.toJson(gameSettingsDto);

		DominionMessage request = new DominionMessage(
				DominionEvent.CREATE_GAME, jsonSettings);
		String jsonRequest = gson.toJson(request);

		try (Socket socket = new Socket("localhost", port);
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						socket.getInputStream()))) {
			pw.println(jsonRequest);
			pw.flush();
			String response = br.readLine();

			assertNotNull(response);
		}

		server.stopServer();
	}
}
