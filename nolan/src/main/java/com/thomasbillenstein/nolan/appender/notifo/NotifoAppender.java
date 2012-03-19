package com.thomasbillenstein.nolan.appender.notifo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;

import com.thomasbillenstein.nolan.NotifyingAppender;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.buffering.IBuffer;
import com.thomasbillenstein.nolan.courier.Courier;


public final class NotifoAppender extends NotifyingAppender
{
	private static final String DEFAULT_TITLE_LAYOUT_PATTERN = "%logger{20} - %m";
	private Layout<ILoggingEvent> titleLayout;
	private String title;
	private String label;
	private String uri;

	@Override
	protected Courier createCourier(final Recipient recipient)
	{
		return new NotifoCourier(recipient);
	}

	@Override
	protected void initializeParameters()
	{
		titleLayout = createPatternLayout(title != null ? title : DEFAULT_TITLE_LAYOUT_PATTERN);

		super.initializeParameters();
	}

	@Override
	protected void deliverMessage(final ILoggingEvent event, final IBuffer buffer)
	{
		final String message = createNotification(buffer);
		final String formattedTitle = titleLayout.doLayout(event);

		for (final Courier courier : couriers)
		{
			try
			{
				((NotifoCourier)courier).deliverMessage(message, formattedTitle, label, uri);
			}
			catch (Exception e)
			{
				addError("Unable to send notification in appender [{0}]", e, name);
			}
		}
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(final String label)
	{
		this.label = label;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(final String uri)
	{
		this.uri = uri;
	}
}
