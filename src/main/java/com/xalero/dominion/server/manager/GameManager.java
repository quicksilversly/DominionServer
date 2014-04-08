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
package com.xalero.dominion.server.manager;

import com.xalero.dominion.server.model.DominionModel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author jonathan
 */
public class GameManager {

    private final static ConcurrentMap<Long, DominionModel> games;

    static {
        games = new ConcurrentHashMap<>();
    }

    public static void addGame(Long gameId, DominionModel dominionModel) {
        games.put(gameId, dominionModel);
    }

    public static void removeGame(Long gameId) {
        games.remove(gameId);
    }

    public static boolean gameExists(Long gameId) {
        return games.containsKey(gameId);
    }
    
    public static DominionModel getGame(Long gameId) {
        return games.get(gameId);
    }
}
