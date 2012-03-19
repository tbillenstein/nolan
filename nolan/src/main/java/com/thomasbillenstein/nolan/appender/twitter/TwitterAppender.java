package com.thomasbillenstein.nolan.appender.twitter;

import com.thomasbillenstein.nolan.NotifyingAppender;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.Courier;

public final class TwitterAppender extends NotifyingAppender
{
	@Override
	protected Courier createCourier(final Recipient recipient)
	{
		return new TwitterCourier(recipient);
	}
}
