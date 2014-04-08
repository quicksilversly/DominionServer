package com.xalero.dominion.server;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xalero.dominion.commons.model.PlayerType;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;

public class TestJoinGame {

	private final int port;
	private final DominionServer server;

	public TestJoinGame() throws UnknownHostException, IOException {
		port = 6781;
		server = new DominionServer(port);

		new Thread(new Runnable() {
			@Override
			public void run() {
				server.startServer();
			}
		}).start();
		
	}

	@Test
	public void testJoinGame() throws IOException {
		Long gameId = new Long(createGame());

		PlayerDto playerDto = new PlayerDto();
		playerDto.setName("Jonathan");
		playerDto.setPlayerType(PlayerType.COMPUTER);
		
		PlayerGameDto playerGameDto = new PlayerGameDto();
		playerGameDto.setGameId(gameId);
		playerGameDto.setPlayerDto(playerDto);

		try (Socket playerSocket = new Socket("localhost", port);
				PrintWriter pw = new PrintWriter(playerSocket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						playerSocket.getInputStream()))) {
			String jsonPlayerGame = new Gson().toJson(playerGameDto);
			DominionMessage request = new DominionMessage(
					DominionEvent.JOIN_GAME, jsonPlayerGame);
			String jsonRequest = new Gson().toJson(request);
			pw.println(jsonRequest);
			pw.flush();
			String response = br.readLine();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		server.stopServer();
	}

	private long createGame() {
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

		Long gameId = null;

		try (Socket playerSocket = new Socket("localhost", port);
				PrintWriter pw = new PrintWriter(playerSocket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						playerSocket.getInputStream()))) {
			pw.println(jsonRequest);
			pw.flush();
			String response = br.readLine();
			DominionMessage responseMessage = new Gson().fromJson(response,
					DominionMessage.class);
			gameId = new Gson()
					.fromJson(responseMessage.getValue(), Long.class);
			assertNotNull(gameId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gameId.longValue();
	}
}
