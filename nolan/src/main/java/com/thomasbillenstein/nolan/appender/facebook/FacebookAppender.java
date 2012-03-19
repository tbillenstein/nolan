package com.thomasbillenstein.nolan.appender.facebook;

import com.thomasbillenstein.nolan.NotifyingAppender;
import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.courier.Courier;

public class FacebookAppender extends NotifyingAppender
{
	@Override
	protected Courier createCourier(final Recipient recipient)
	{
		return new FacebookCourier(recipient);
	}
}
