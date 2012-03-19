package com.thomasbillenstein.nolan.base;

public class Server
{
	private String address;
	private int port;
	private String cwd;

	public String getAddress()
	{
		return address;
	}

	public void setAddress(final String address)
	{
		this.address = address;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(final int port)
	{
		this.port = port;
	}

	public String getCwd()
	{
		return cwd;
	}

	public void setCwd(final String cwd)
	{
		this.cwd = cwd;
	}
}
