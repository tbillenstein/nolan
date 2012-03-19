package com.thomasbillenstein.nolan.appender.notifo;

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

import com.google.gson.Gson;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.HttpClientCourier;

public class NotifoCourier extends HttpClientCourier
{
	private static final String URL = "https://api.notifo.com/v1/send_notification";
	private static final Gson GSON = new Gson();

	private final String username;

	public NotifoCourier(final Recipient recipient)
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
		if (recipient.getCredentials().getApiKey() == null)
		{
			throw newIllegalArgumentException("apiKey");
		}

		username = recipient.getCredentials().getUsername();
		final String apiKey = recipient.getCredentials().getApiKey();

		setBasicAuthCredentials(username, apiKey);
	}

	@Override
	public void deliverMessage(final String message) throws Exception
	{
	}

	public void deliverMessage(final String message, final String title, final String label, final String uri) throws Exception
	{
		final List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("to", username));
		params.add(new BasicNameValuePair("msg", message));
		if (title != null)
		{
			params.add(new BasicNameValuePair("title", title));
		}
		if (label != null)
		{
			params.add(new BasicNameValuePair("label", label));
		}
		if (uri != null)
		{
			params.add(new BasicNameValuePair("uri", uri));
		}

		final HttpPost httpPost = new HttpPost(URL);
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		executeRequest(httpPost);

		if (debug)
		{
			trace.out("sent notification:\n{0}", message);
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
			trace.out("Notifo response [{0}], [{1}], [{2}]", statusCode, reasonPhrase, body);
		}

		if (statusCode != HttpStatus.SC_OK)
		{
			throw new Exception("Error sending notification: statusCode=" + statusCode + ", reasonPhrase=" + reasonPhrase);
		}
		else
		{
			NotifoResponse response = GSON.fromJson(body, NotifoResponse.class);

			if (!response.status.equals("success"))
			{
				throw new Exception("Error sending notification: responseCode=" + response.response_code + ", reasonMessage=" + response.response_message);
			}
		}
	}

	static class NotifoResponse
	{
		public NotifoResponse()
		{
		}

		public String status;
		public String response_code;
		public String response_message;
	}
}
