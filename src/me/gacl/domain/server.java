package me.gacl.domain;

public class server {
	private String serverIP;
	private String serverName;
	private String serverPosition;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPosition() {
		return serverPosition;
	}

	public void setServerPosition(String serverPosition) {
		this.serverPosition = serverPosition;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
    public String toString() {
        return serverIP;
    }
}
