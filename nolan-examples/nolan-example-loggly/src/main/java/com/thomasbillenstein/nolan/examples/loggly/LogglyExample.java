package com.thomasbillenstein.nolan.examples.loggly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogglyExample
{
	private final Logger logger = LoggerFactory.getLogger(LogglyExample.class);

	public LogglyExample()
	{
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	public static void main(final String[] args)
	{
		new LogglyExample();
	}
}
