package net;

public class ProxyServer {
	public String server;
	public short port;
	
	public ProxyServer(String host, short hostPort ) {
		server = host;
		port = hostPort;
	}
}
