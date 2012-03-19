package com.thomasbillenstein.nolan.examples.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpExample
{
	private final Logger logger = LoggerFactory.getLogger(FtpExample.class);

	public FtpExample()
	{
		logger.trace("trace");
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	public static void main(final String[] args)
	{
		new FtpExample();
	}
}
