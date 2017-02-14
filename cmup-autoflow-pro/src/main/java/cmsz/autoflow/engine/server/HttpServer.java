package cmsz.autoflow.engine.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.util.buffer.FileAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmsz.autoflow.engine.AutoEngine;
import cmsz.autoflow.engine.access.MyBatisAccess;
import cmsz.autoflow.engine.cfg.Configuration;

public class HttpServer {

	private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

	private int poolsize = 10;

	private int port = 8108;

	private Container container;

	private Connection connection;

	public HttpServer() {
		this.container = new JsonContainer();
	}

	public void start() {
		logger.info("Start  AutoEngine HttpServer ...");
		Configuration conf = new Configuration();
		AutoEngine engine = conf.buildAutoEngine();
		logger.info("AutoEgine initialized ...");
		try {
			connection = new SocketConnection(new ContainerServer(container,
					new FileAllocator(), poolsize));
			SocketAddress address = new InetSocketAddress(port);
			connection.connect(address);
			logger.info("AutoEngine HttpServer listening on port :{}", port);
		} catch (IOException e) {
			logger.error("AutoEngine HttpServer is failed to start",e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.start();
	}

}
