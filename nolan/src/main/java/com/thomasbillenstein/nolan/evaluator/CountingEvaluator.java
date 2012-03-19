package com.thomasbillenstein.nolan.evaluator;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class CountingEvaluator extends EventEvaluatorBase<ILoggingEvent>
{
	private static final int DEFAULT_LIMIT = 256;

	private int count = 0;
	private int limit = DEFAULT_LIMIT;

	/**
	 * @param event logging event that triggers the evaluation
	 *
	 * @return true if count has reached limit (CountingEvaluator has seen count logging events),
	 * else false.
	 */
	public boolean evaluate(final ILoggingEvent event) throws EvaluationException
	{
		if (++count >= limit)
		{
			count = 0;
			return true;
		}

		return false;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(final int limit)
	{
		this.limit = limit;
	}
}
