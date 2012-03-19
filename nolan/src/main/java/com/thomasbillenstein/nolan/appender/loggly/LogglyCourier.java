package com.thomasbillenstein.nolan.appender.loggly;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.HttpClientCourier;

public class LogglyCourier extends HttpClientCourier
{
	private static final String URL = "https://logs.loggly.com/inputs/";

	private String url;

	public LogglyCourier(final Recipient recipient)
	{
		if (recipient == null)
		{
			throw newIllegalArgumentException("recipient");
		}
		if (recipient.getCredentials() == null)
		{
			throw newIllegalArgumentException("credentials");
		}
		if (recipient.getCredentials().getHash() == null)
		{
			throw newIllegalArgumentException("hash");
		}

		url = URL + recipient.getCredentials().getHash();
	}

	@Override
	public void deliverMessage(final String notification) throws Exception
	{
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(notification));

		executeRequest(httpPost);

		if (debug)
		{
			trace.out("sent notification:\n{0}", notification);
		}
	}

	private void executeRequest(final HttpPost httpPost) throws Exception
	{
		final HttpResponse httpResponse = super.execute(httpPost);

		final int statusCode = httpResponse.getStatusLine().getStatusCode();
		final String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		final String body = EntityUtils.toString(httpResponse.getEntity());

		if (debug)
		{
			trace.out("Loggly response [{0}], [{1}], [{2}]", statusCode, reasonPhrase, body);
		}

		if (statusCode != 200)
		{
			throw new Exception("Error sending notification: statusCode=" + statusCode + ", reasonPhrase=" + reasonPhrase);
		}
	}
}
