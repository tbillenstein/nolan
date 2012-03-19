package com.thomasbillenstein.nolan.examples.ftp.server;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class TestFtpServer
{
	private static final String USER_CONFIG_FILE = "./config/ftpserver/users.properties";
	private static final int PORT = 21;

	public static void main(final String[] args)
	{
		final FtpServerFactory serverFactory = new FtpServerFactory();

		final ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setPort(PORT);

		serverFactory.addListener("default", listenerFactory.createListener());

        final PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(USER_CONFIG_FILE));

        serverFactory.setUserManager(userManagerFactory.createUserManager());

        final Map<String, Ftplet> ftplets = new LinkedHashMap<String, Ftplet>();
        ftplets.put("testFtplet", new TestFtplet());

        serverFactory.setFtplets(ftplets);

        final FtpServer server = serverFactory.createServer();

		try
		{
			server.start();

			System.in.read();

			server.stop();
		}
		catch (final Exception e)
		{
			System.out.println("Error starting embedded FTPServer: ");
			e.printStackTrace();
		}
	}
}
