package com.thomasbillenstein.nolan.examples.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterExample
{
	private final Logger logger = LoggerFactory.getLogger(TwitterExample.class);

	public TwitterExample()
	{
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	public static void main(final String[] args)
	{
		new TwitterExample();
	}
}
