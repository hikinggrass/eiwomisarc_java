package de.hikinggrass.eiwomisarc;

import java.io.IOException;

/**
 * This Class handles all network functionalities of the program such as receiving UDP packets from clients (and
 * communication with other software?)
 * 
 * @author Kai Hermann
 * @version 0.1
 */
public class NetworkServer {
	private Core core;

	public NetworkServer(Core core) {
		this.core = core;
		try {
			new NetworkServerThread(this.core).start();
			// Core.debugMessage("[net] Listening on port" + core.getSettingsNetwork().getPort());
		} catch (IOException e) {
			// Core.errorMessage("[net] Coudn't listen on port " + core.getSettingsNetwork().getPort() + ": "
			// + e.getMessage());
		}
	}

}
