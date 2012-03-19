package com.thomasbillenstein.nolan.appender.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.Courier;

public class TwitterCourier extends Courier
{
	private static final int MAX_MESSAGE_LENGTH = 140;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private Twitter twitter;

	public TwitterCourier(final Recipient recipient)
	{
		if (recipient == null)
		{
			throw newIllegalArgumentException("recipient");
		}
		if (recipient.getCredentials() == null)
		{
			throw newIllegalArgumentException("credentials");
		}
		if (recipient.getCredentials().getConsumerKey() == null)
		{
			throw newIllegalArgumentException("consumerKey");
		}
		if (recipient.getCredentials().getConsumerSecret() == null)
		{
			throw newIllegalArgumentException("consumerSecret");
		}
		if (recipient.getCredentials().getAccessToken() == null)
		{
			throw newIllegalArgumentException("accessToken");
		}
		if (recipient.getCredentials().getAccessTokenSecret() == null)
		{
			throw newIllegalArgumentException("accessTokenSecret");
		}

		consumerKey = recipient.getCredentials().getConsumerKey();
		consumerSecret = recipient.getCredentials().getConsumerSecret();
		accessToken = recipient.getCredentials().getAccessToken();
		accessTokenSecret = recipient.getCredentials().getAccessTokenSecret();
	}

	@Override
	public void deliverMessage(final String message) throws Exception
	{
		execute(message);

		if (debug)
		{
			trace.out("Sent notification:\n{0}", message);
		}
	}

	private void execute(final String message) throws Exception
	{
		final String text = message.length() > MAX_MESSAGE_LENGTH ? message.substring(0, MAX_MESSAGE_LENGTH) : message;

		if (twitter == null)
		{
			twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
		}

		final Status status = twitter.updateStatus(text);

		if (debug)
		{
			trace.out("Twitter response [{0}]", status.toString());
		}
	}

	@Override
	public void setProxy(final String host, final int port)
	{
		System.setProperty("twitter4j.http.proxyHost", host);
		System.setProperty("twitter4j.http.proxyPort", String.valueOf(port));

		super.setProxy(host, port);
	}
}
