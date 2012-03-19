package com.thomasbillenstein.nolan.notification;

import com.thomasbillenstein.nolan.buffering.IBuffer;

public interface INotificationBuilder
{
	String build(final IBuffer buffer);
}
