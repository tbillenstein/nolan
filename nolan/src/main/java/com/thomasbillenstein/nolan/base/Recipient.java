package com.thomasbillenstein.nolan.base;

public class Recipient
{
	private Credentials credentials;
	private String name;

	public Credentials getCredentials()
	{
		return credentials;
	}

	public void setCredentials(final Credentials credentials)
	{
		this.credentials = credentials;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}
}
