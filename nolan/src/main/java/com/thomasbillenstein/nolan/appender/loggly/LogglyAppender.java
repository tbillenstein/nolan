package com.thomasbillenstein.nolan.appender.loggly;

import com.thomasbillenstein.nolan.NotifyingAppender;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.Courier;

public class LogglyAppender extends NotifyingAppender
{
	@Override
	protected Courier createCourier(final Recipient recipient)
	{
		return new LogglyCourier(recipient);
	}
}
