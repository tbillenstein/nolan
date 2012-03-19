package com.thomasbillenstein.nolan.appender.ftp;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;

import com.thomasbillenstein.nolan.NotifyingAppender;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.base.Server;
import com.thomasbillenstein.nolan.buffering.IBuffer;
import com.thomasbillenstein.nolan.courier.Courier;

public final class FTPAppender extends NotifyingAppender
{
	private static final String DEFAULT_FTP_SERVER_ADDRESS = "localhost";
	private static final int DEFAULT_FTP_SERVER_PORT = 21;
	private static final String DEFAULT_FTP_SERVER_CWD = ".";
	private static final String DEFAULT_REMOTE_FILENAME_PATTERN = "log.%date{HH-mm-ss-SSS}.txt";

	private Layout<ILoggingEvent> remoteFilenameLayout;
	private String remoteFilename;
	private Server server;

	@Override
	protected Courier createCourier(final Recipient recipient)
	{
		return new FTPCourier(recipient);
	}

	@Override
	protected void initializeParameters()
	{
		remoteFilenameLayout = createPatternLayout(remoteFilename != null ? remoteFilename : DEFAULT_REMOTE_FILENAME_PATTERN);

		if (server == null)
		{
			server = new Server();
			server.setAddress(DEFAULT_FTP_SERVER_ADDRESS);
			server.setPort(DEFAULT_FTP_SERVER_PORT);
			server.setCwd(DEFAULT_FTP_SERVER_CWD);
		}

		super.initializeParameters();
	}

	@Override
	protected void deliverMessage(final ILoggingEvent event, final IBuffer buffer)
	{
		final String message = createNotification(buffer);
		final String finalRemoteFilename = remoteFilenameLayout.doLayout(event);

		for (final Courier courier : couriers)
		{
			try
			{
				((FTPCourier)courier).deliverMessage(message, finalRemoteFilename, server);
			}
			catch (Exception e)
			{
				addError("Unable to send notification in appender [{0}]", e, name);
			}
		}
	}

	public String getRemoteFilename()
	{
		return remoteFilename;
	}

	public void setRemoteFilename(final String remoteFilename)
	{
		this.remoteFilename = remoteFilename;
	}

	public Server getServer()
	{
		return server;
	}

	public void setServer(final Server server)
	{
		this.server = server;
	}
}
