package com.thomasbillenstein.nolan.notification;

import java.util.List;

import com.thomasbillenstein.nolan.buffering.IBuffer;

public class DefaultNotificationBuilder implements INotificationBuilder
{
	@Override
	public String build(final IBuffer buffer)
	{
		final List<String> logMessages = buffer.asList();
		final StringBuilder notification = new StringBuilder(logMessages.size() * 80);
		for (final String message : logMessages)
		{
			notification.append(message);
		}

		return notification.toString();
	}
}
