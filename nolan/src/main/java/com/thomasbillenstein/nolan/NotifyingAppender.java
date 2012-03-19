package com.thomasbillenstein.nolan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.boolex.OnErrorEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.sift.DefaultDiscriminator;
import ch.qos.logback.core.sift.Discriminator;

import com.thomasbillenstein.nolan.base.Recipient;
import com.thomasbillenstein.nolan.base.Trace;
import com.thomasbillenstein.nolan.buffering.DefaultBuffering;
import com.thomasbillenstein.nolan.buffering.IBuffer;
import com.thomasbillenstein.nolan.buffering.IBuffering;
import com.thomasbillenstein.nolan.courier.Courier;
import com.thomasbillenstein.nolan.notification.DefaultNotificationBuilder;
import com.thomasbillenstein.nolan.notification.INotificationBuilder;

//TODO config: reverse message sequence in buffer (latest on top)
//TODO config: retryCount (if not deliverable)
//TODO config: retryDelay

//TODO TimeBasedEventEvaluator
//TODO FTP: Zippin' log events before notifying

public abstract class NotifyingAppender extends AppenderBase<ILoggingEvent>
{
	protected List<Courier> couriers = new ArrayList<Courier>();
	protected Trace trace = new Trace(this.getClass());
	protected boolean debug;

	private List<Recipient> recipients = new ArrayList<Recipient>();
	private Layout<ILoggingEvent> layout;
	private EventEvaluator<ILoggingEvent> eventEvaluator;
	private Discriminator<ILoggingEvent> discriminator;
	private IBuffering buffering;
	private INotificationBuilder notificationBuilder;
	private String proxyHost;
	private int proxyPort;
	private ExecutorService threadPool;
	private boolean asynchronous;
	private int errorCount;

	protected abstract Courier createCourier(final Recipient recipient);

	public void start()
	{
		if (debug)
		{
			trace.out("Starting {0}", name);
		}

		if (layout == null)
		{
			addError("No layout set for appender [" + name + "]");
		}
		else
		{
			initializeParameters();

			super.start();

			if (asynchronous)
			{
				threadPool = Executors.newFixedThreadPool(1);
			}

			initializeCouriers();
		}
	}

	public void stop()
	{
		if (threadPool != null)
		{
			threadPool.shutdown();
		}

		super.stop();
	}

	@Override
	protected void append(final ILoggingEvent event)
	{
		try
		{
			final String key = discriminator.getDiscriminatingValue(event);
			final String message = layout.doLayout(event);

			buffering.addMessage(key, message);

			if (eventEvaluator.evaluate(event))
			{
				final IBuffer buffer = buffering.releaseBuffer(key);
				doNotify(event, buffer);
			}
		}
		catch (EvaluationException e)
		{
			if (++errorCount < CoreConstants.MAX_ERROR_COUNT)
			{
				addError("EventEvaluator for appender [" + name + "] threw an Exception", e);
			}
		}
	}

	private void doNotify(final ILoggingEvent triggerEvent, final IBuffer buffer)
	{
		if (threadPool != null)
		{
			if (debug)
			{
				trace.out("notify asynchronous [{0}]", triggerEvent);
			}

			final Runnable runnable = new Runnable()
			{
				@Override
				public void run()
				{
					deliverMessage(triggerEvent, buffer);
				}
			};

			threadPool.execute(runnable);
		}
		else
		{
			deliverMessage(triggerEvent, buffer);
		}
	}

	protected void deliverMessage(final ILoggingEvent event, final IBuffer buffer)
	{
		final String notification = createNotification(buffer);

		for (final Courier courier : couriers)
		{
			try
			{
				courier.deliverMessage(notification);
			}
			catch (Exception e)
			{
				addError("Unable to send notification in appender [{0}]", e, name);
			}
		}
	}

	protected void initializeParameters()
	{
		if (discriminator == null)
		{
			discriminator = new DefaultDiscriminator<ILoggingEvent>();
		}

		if (buffering == null)
		{
			buffering = new DefaultBuffering();
		}

		if (eventEvaluator == null)
		{
			final OnErrorEvaluator onError = new OnErrorEvaluator();
			onError.setContext(getContext());
			onError.setName("onError");
			onError.start();

			eventEvaluator = onError;
		}

		if (notificationBuilder == null)
		{
			notificationBuilder = new DefaultNotificationBuilder();
		}
	}

	protected void initializeCouriers()
	{
		for (final Recipient recipient : recipients)
		{
			try
			{
				final Courier courier = createCourier(recipient);
				courier.setDebug(debug);

				if (proxyHost != null)
				{
					courier.setProxy(proxyHost, proxyPort);
				}

				couriers.add(courier);
			}
			catch(Exception e)
			{
				addError("Error initializing courier in appender [{0}]", e, name);
			}
		}
	}

	protected String createNotification(final IBuffer buffer)
	{
		return notificationBuilder.build(buffer);
	}

	protected Layout<ILoggingEvent> createPatternLayout(final String pattern)
	{
		final PatternLayout pl = new PatternLayout();
		pl.setContext(getContext());
		pl.setPattern(pattern);
		pl.setPostCompileProcessor(null);
		pl.start();

		return pl;
	}

	public void addError(final String msg, final Object ... arguments)
	{
		addError(trace.build(msg, arguments));
	}

	public void addError(final String msg)
	{
		if (debug)
		{
			trace.out(msg);
		}

		super.addError(msg);
	}

	public void addError(final String msg, final Throwable ex, final Object ... arguments)
	{
		addError(trace.build(msg, arguments), ex);
	}

	public void addError(final String msg, final Throwable ex)
	{
		if (debug)
		{
			trace.out(msg + ", " + ex);
		}

		super.addError(msg, ex);
	}

	public Layout<ILoggingEvent> getLayout()
	{
		return layout;
	}

	public void setLayout(final Layout<ILoggingEvent> layout)
	{
		this.layout = layout;
	}

	public EventEvaluator<ILoggingEvent> getEventEvaluator()
	{
		return eventEvaluator;
	}

	public void setEventEvaluator(final EventEvaluator<ILoggingEvent> triggerEventEvaluator)
	{
		this.eventEvaluator = triggerEventEvaluator;
	}

	public Discriminator<ILoggingEvent> getDiscriminator()
	{
		return discriminator;
	}

	public void setDiscriminator(final Discriminator<ILoggingEvent> discriminator)
	{
		this.discriminator = discriminator;
	}

	public IBuffering getBuffering()
	{
		return buffering;
	}

	public void setBuffering(final IBuffering buffering)
	{
		this.buffering = buffering;
	}

	public INotificationBuilder getNotificationBuilder()
	{
		return notificationBuilder;
	}

	public void setNotificationBuilder(final INotificationBuilder notificationBuilder)
	{
		this.notificationBuilder = notificationBuilder;
	}

	public void addRecipient(final Recipient recipient)
	{
		recipients.add(recipient);
	}

	public boolean isAsynchronous()
	{
		return asynchronous;
	}

	public void setAsynchronous(final boolean asynchronous)
	{
		this.asynchronous = asynchronous;
	}

	public String getProxyHost()
	{
		return proxyHost;
	}

	public void setProxyHost(final String proxyHost)
	{
		this.proxyHost = proxyHost;
	}

	public int getProxyPort()
	{
		return proxyPort;
	}

	public void setProxyPort(final int proxyPort)
	{
		this.proxyPort = proxyPort;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(final boolean debug)
	{
		this.debug = debug;
	}
}
