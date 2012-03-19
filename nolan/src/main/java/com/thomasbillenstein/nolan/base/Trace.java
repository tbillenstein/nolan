package com.thomasbillenstein.nolan.base;

import java.text.MessageFormat;

public class Trace
{
	private String name;

	public Trace(final String name)
	{
		this.name = name;
	}

	public Trace(final Class<?> clazz)
	{
		this(clazz.getName());
	}

	public String out(final String message, final Object ... arguments)
	{
		return out(build(message, arguments));
	}

	public String out(final String message)
	{
		final StringBuilder sb = new StringBuilder(80);
		sb.append("[").append(name).append("] - ").append(message);

		final String s = sb.toString();
		System.out.println(s);

		return s;
	}

	public String build(final String message, final Object ... arguments)
	{
		return MessageFormat.format(message, arguments);
	}
}
