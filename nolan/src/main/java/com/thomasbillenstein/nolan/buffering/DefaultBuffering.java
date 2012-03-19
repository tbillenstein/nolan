package com.thomasbillenstein.nolan.buffering;

import java.util.HashMap;

public class DefaultBuffering implements IBuffering
{
	private static final int DEFAULT_BUFFER_SIZE = 256;
	private static final int DEFAULT_MAX_BUFFERS = 64;

	private HashMap<String, DefaultBuffer> bufferMap = new HashMap<String, DefaultBuffer>();
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private int maxBuffers = DEFAULT_MAX_BUFFERS;

	@Override
	public void addMessage(final String key, final String message)
	{
		final DefaultBuffer buffer = get(key);

		buffer.add(message);
	}

	@Override
	public IBuffer releaseBuffer(final String key)
	{
		final IBuffer buffer = bufferMap.get(key);
		if (buffer != null)
		{
			bufferMap.remove(key);
		}

		return buffer;
	}

	private DefaultBuffer get(final String key)
	{
		DefaultBuffer buffer = bufferMap.get(key);
		if (buffer == null)
		{
			if (bufferMap.size() >= maxBuffers)
			{
				long oldestTimestamp = System.currentTimeMillis();
				String oldestKey = null;

				for (final String k : bufferMap.keySet())
				{
					final DefaultBuffer b = bufferMap.get(k);
					final long ts = b.getTimestamp();
					if (ts < oldestTimestamp)
					{
						oldestTimestamp = ts;
						oldestKey = k;
					}
				}

				bufferMap.remove(oldestKey);
			}

			buffer = new DefaultBuffer();
			buffer.setMaxSize(bufferSize);

			bufferMap.put(key, buffer);
		}

		return buffer;
	}

	public int getBufferSize()
	{
		return bufferSize;
	}

	public void setBufferSize(final int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

	public int getMaxBuffers()
	{
		return maxBuffers;
	}

	public void setMaxBuffers(final int maxBuffers)
	{
		this.maxBuffers = maxBuffers;
	}
}
