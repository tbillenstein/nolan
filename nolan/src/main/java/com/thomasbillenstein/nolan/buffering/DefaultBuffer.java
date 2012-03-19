package com.thomasbillenstein.nolan.buffering;

import java.util.LinkedList;
import java.util.List;

public class DefaultBuffer implements IBuffer
{
	private LinkedList<String> list;
	private int maxSize;
	private long lastAddTimestamp;

	public DefaultBuffer()
	{
		list = new LinkedList<String>();
	}

	@Override
	public void add(final String message)
	{
		if (list.size() >= maxSize)
		{
			list.removeFirst();
		}

		list.add(message);

		lastAddTimestamp = System.currentTimeMillis();
	}

	@Override
	public List<String> asList()
	{
		return list;
	}

	@Override
	public void setMaxSize(final int maxSize)
	{
		this.maxSize = maxSize;
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	public long getTimestamp()
	{
		return lastAddTimestamp;
	}
}
