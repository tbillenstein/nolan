package com.thomasbillenstein.nolan.examples.notifo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifoExample
{
	private final Logger logger = LoggerFactory.getLogger(NotifoExample.class);

	public NotifoExample()
	{
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	public static void main(final String[] args)
	{
		new NotifoExample();
	}
}
