package com.thomasbillenstein.nolan.courier;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public abstract class HttpClientCourier extends Courier
{
	private DefaultHttpClient httpClient;
	private String basicAuthUsername;
	private String basicAuthPassword;

	protected DefaultHttpClient getHttpClient()
	{
		if (httpClient == null)
		{
			httpClient = new DefaultHttpClient();

			if (proxyHost != null)
			{
				final HttpHost proxy = new HttpHost(proxyHost, proxyPort);

				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			if (basicAuthUsername != null && basicAuthPassword != null)
			{
				final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
				final BasicScheme basicAuthScheme = new BasicScheme();

				getHttpClient().addRequestInterceptor(new HttpRequestInterceptor()
				{
					@Override
					public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException
					{
						request.addHeader(basicAuthScheme.authenticate(credentials, request));
					}
				});
			}
		}

		return httpClient;
	}

	protected void setBasicAuthCredentials(final String username, final String password)
	{
		basicAuthUsername = username;
		basicAuthPassword = password;
	}

	protected HttpResponse execute(final HttpUriRequest request) throws IOException
	{
		if (debug)
		{
			trace.out("Executing {0} request [{1}]", request.getMethod(), request.getURI().toString());
		}

		final HttpResponse response = getHttpClient().execute(request);

		if (debug)
		{
			trace.out("Got response [{0}]", response.toString());
		}

		return response;
	}
}
