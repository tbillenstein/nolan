package com.thomasbillenstein.nolan.courier;

import com.thomasbillenstein.nolan.base.Trace;

public abstract class Courier
{
	protected boolean debug;
	protected Trace trace = new Trace(this.getClass());
	protected String proxyHost;
	protected int proxyPort;

	public abstract void deliverMessage(final String message) throws Exception;

	public void setProxy(final String host, final int port)
	{
		proxyHost = host;
		proxyPort = port;

		final String sPort = String.valueOf(port);

		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", sPort);

		System.setProperty("https.proxyHost", host);
		System.setProperty("https.proxyPort", sPort);

		if (debug)
		{
			trace.out("Using proxy - host [{0}], port [{1}]", host, String.valueOf(port));
		}
	}

	public void setDebug(final boolean debug)
	{
		this.debug = debug;
	}

	public boolean isDebug()
	{
		return debug;
	}

	protected IllegalArgumentException newIllegalArgumentException(final String argument)
	{
		return new IllegalArgumentException(argument + " must not be null");
	}
}
