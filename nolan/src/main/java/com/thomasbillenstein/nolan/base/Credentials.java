package com.thomasbillenstein.nolan.base;

public class Credentials
{
	private String username;
	private String password;
	private String apiKey;
	private String account;

	private String hash;

	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey)
	{
		this.apiKey = apiKey;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(final String hash)
	{
		this.hash = hash;
	}

	public String getConsumerKey()
	{
		return consumerKey;
	}

	public void setConsumerKey(final String consumerKey)
	{
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret()
	{
		return consumerSecret;
	}

	public void setConsumerSecret(final String consumerSecret)
	{
		this.consumerSecret = consumerSecret;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(final String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret()
	{
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(final String accessTokenSecret)
	{
		this.accessTokenSecret = accessTokenSecret;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(final String account)
	{
		this.account = account;
	}
}
