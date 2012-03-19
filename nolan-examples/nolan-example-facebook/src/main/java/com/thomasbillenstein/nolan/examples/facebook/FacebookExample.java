package com.thomasbillenstein.nolan.examples.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookExample
{
	private final Logger logger = LoggerFactory.getLogger(FacebookExample.class);

	public FacebookExample()
	{
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	public static void main(final String[] args)
	{
		new FacebookExample();
	}
}
