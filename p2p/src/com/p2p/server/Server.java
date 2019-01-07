package com.p2p.server;
import java.util.List;

public class Server {
	private int port;
	private List<ClientF> listClientF;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public List<ClientF> getListClientF() {
		return listClientF;
	}
	public void setListClientF(List<ClientF> listClientF) {
		this.listClientF = listClientF;
	}
}
