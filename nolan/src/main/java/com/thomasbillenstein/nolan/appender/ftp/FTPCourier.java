package com.thomasbillenstein.nolan.appender.ftp;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.base.Server;
import com.thomasbillenstein.nolan.courier.Courier;

public class FTPCourier extends Courier
{
	private String username;
	private String password;
	private String account;

	public FTPCourier(final Recipient recipient)
	{
		if (recipient == null)
		{
			throw newIllegalArgumentException("recipient");
		}
		if (recipient.getCredentials() == null)
		{
			throw newIllegalArgumentException("credentials");
		}
		if (recipient.getCredentials().getUsername() == null)
		{
			throw newIllegalArgumentException("username");
		}

		username = recipient.getCredentials().getUsername();
		password = recipient.getCredentials().getPassword();
		account = recipient.getCredentials().getAccount();
	}

	@Override
	public void deliverMessage(final String message) throws Exception
	{
	}

	public void deliverMessage(final String message, final String remoteFilename, final Server server) throws IOException
	{
		final String host = server.getAddress();
		final int port = server.getPort();
		final String cwd = server.getCwd();

		boolean connected = false;

		final FTPClient ftp = new FTPClient();

		try
		{
			ftp.connect(host, port);
			connected = true;

			final int replyCode = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode))
			{
				final String msg = createErrorMessage(host, port, ftp.getReplyString());

				throw new IOException(msg);
			}

			boolean loggedIn = false;

			if (account != null)
			{
				loggedIn = ftp.login(username, password, account);
			}
			else
			{
				loggedIn = ftp.login(username, password);
			}

			if (!loggedIn)
			{
				final String msg = createErrorMessage(host, port, ftp.getReplyString());

				throw new IOException(msg);
			}

			if (!ftp.changeWorkingDirectory(cwd))
			{
				final String msg = createErrorMessage(host, port, ftp.getReplyString());

				throw new IOException(msg);
			}

			final OutputStream outputStream = ftp.storeFileStream(remoteFilename);
			if (outputStream == null)
			{
				final String msg = MessageFormat.format("FTP server [{0}:{1}] - [{2}] / logfile: [{3}]", host, port, ftp.getReplyString(), remoteFilename);
				if (debug)
				{
					trace.out(msg);
				}

				throw new IOException(msg);
			}

			outputStream.write(message.getBytes());
			outputStream.flush();
			outputStream.close();

			ftp.logout();

			if (debug)
			{
				trace.out("sent notification:\n{0}", message);
			}

		}
		catch(IOException ex)
		{
			throw ex;
		}
		finally
		{
			if (connected)
			{
				try
				{
					ftp.disconnect();
				}
				catch(IOException e)
				{
					// ignore
				}
			}
		}
	}

	private String createErrorMessage(final String host, final int port, final String replyString)
	{
		final String msg = MessageFormat.format("FTP server [{0}:{1}] - [{2}]", host, port, replyString);

		if (debug)
		{
			trace.out(msg);
		}

		return msg;
	}
}
