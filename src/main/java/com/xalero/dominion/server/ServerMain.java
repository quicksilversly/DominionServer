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

import java.io.IOException;

/**
 *
 * @author jonathan
 */
public class ServerMain {

    public static void main(String args[]) {
        if (args != null && args.length > 0 && args[0] != null) {
            try {
				new DominionServer(Integer.parseInt(args[0])).startServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
