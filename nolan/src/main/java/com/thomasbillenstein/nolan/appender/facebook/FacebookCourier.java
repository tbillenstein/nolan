package com.thomasbillenstein.nolan.appender.facebook;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.HttpClientCourier;

public class FacebookCourier extends HttpClientCourier
{
	private static final String URL = "https://graph.facebook.com/${un}/feed";

	private String accessToken;
	private String url;

	public FacebookCourier(final Recipient recipient)
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
		if (recipient.getCredentials().getAccessToken() == null)
		{
			throw newIllegalArgumentException("accessToken");
		}

		accessToken = recipient.getCredentials().getAccessToken();

		url = URL.replace("${un}", recipient.getCredentials().getUsername());
	}

	@Override
	public void deliverMessage(final String notification) throws Exception
	{
		final List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("message", notification));

		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		executeRequest(httpPost);

		if (debug)
		{
			trace.out("Sent notification:\n{0}", notification);
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
			trace.out("Facebook response [{0}], [{1}], [{2}]", statusCode, reasonPhrase, body);
		}

		if (statusCode != HttpStatus.SC_OK)
		{
			throw new Exception("Error sending notification: statusCode=" + statusCode + ", reasonPhrase=" + reasonPhrase);
		}
	}
}
