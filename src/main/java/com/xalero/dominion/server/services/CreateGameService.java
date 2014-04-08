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

package com.xalero.dominion.server.services;

import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;

/**
 *
 * @author jonathan
 */
public class CreateGameService {
    
    public static synchronized Long createGame(GameSettingsDto gameSettings) {
        DominionModel dominionModel = new DominionModel(gameSettings);
        Long gameId = new Long(dominionModel.getGameId());
        GameManager.addGame(gameId, dominionModel);
        return gameId;
    }
}
