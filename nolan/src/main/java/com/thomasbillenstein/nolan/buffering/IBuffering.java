package com.thomasbillenstein.nolan.buffering;

public interface IBuffering
{
	void addMessage(final String key, final String message);

	IBuffer releaseBuffer(final String key);
}
