Nolan - Notifying Logback Appender (SLF4J)
==========================================

Nolan is an extension to Logback, "the reliable, generic, fast and
flexible logging library for Java", which supplies an implementation of the SLF4J API.

Thanks to @ceki for Logback and SLF4J.

Using Nolan is the easiest way to get a notification from your
applications by a simple log statement.

Whenever the triggering log event occurs, you can

* send your log messages to the Loggly web based logging service
* get notified on your iPhone by the Notifo web service
* transfer your logs to a FTP Server
* and even update your Twitter or Facebook status

Nolan currently consists of 5 Logback appenders

* FacebookAppender
* FTPAppender
* LogglyAppender
* NotifoAppender
* TwitterAppender

Nolan is highly configurable through Logback's configuration
system and supplies configuration options simlilar to Logback's SMTPAppender.

Example Logback configuration file - logback.xml

	<configuration>

	  <appender name="NOTIFO" class="com.thomasbillenstein.nolan.appender.notifo.NotifoAppender">

	    <layout>
	      <pattern>%date{HH:mm:ss.SSS} [%thread] %-5level %logger{80} - %msg%n</pattern>
	    </layout>

		<!-- Notifo supports 3 additional parameters beside the actual message -->

		<!-- title: name of "notification event"; this one can be defined as a pattern, i.e. "%logger{20} - %m"
		     the log event that triggered the notification is used to layout the title
		-->
		<title>%logger{20} - %m</title>

		<!-- label: label describing the "application" -->
		<label>Notifo Logback Appender</label>

		<!-- uri: the uri that will be loaded when the notification is opened; if specified, must be urlencoded;
		     if a web address, must start with http:// or https://
		-->
		<uri>http://thomasbillenstein.com/</uri>

	    <!-- you can add as many recipients as you want -->
	    <recipient>
	    	<credentials>
			    <username>tbillenstein</username>
			    <apiKey>$$notifo-apiKey$$</apiKey>
	    	</credentials>
	    </recipient>

		<!--  configuring the default buffering; only the last 10 log messages are added to the body
		      of the notification; 4 different buffers are seperated by the default discriminator
		      (ch.qos.logback.core.sift.DefaultDiscriminator<E>)
		-->
		<buffering class="com.thomasbillenstein.nolan.buffering.DefaultBuffering">
			<bufferSize>10</bufferSize>
			<maxBuffers>4</maxBuffers>
		</buffering>

		<eventEvaluator class="com.thomasbillenstein.nolan.evaluator.CountingEvaluator">
			<limit>3</limit>
		</eventEvaluator>

		<!-- http proxy configuration -->
	    <proxyHost></proxyHost>
	    <proxyPort></proxyPort>

	    <asynchronous>false</asynchronous>
	    <debug>false</debug>
	  </appender>

	  <root level="DEBUG">
	    <appender-ref ref="NOTIFO" />
	  </root>

	  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
	    <encoder>
	      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
	    </encoder>
	  </appender>

	  <logger name="org.apache" level="INFO">
	    <appender-ref ref="STDOUT" />
	  </logger>

	</configuration>

Some properties are common to all notifying logback appenders others are individual to each appender.

Properties that are common to all notifying logback appenders and that are already supplied by the
lockback implementation:

    <layout/>, <eventEvaluator/>, <discriminator/>


Properties that are common to all notifying logback appenders and that are supplied by the nolan:

    <recipient/>, <buffering/>, <notificationBuilder/>, <asynchronous/>, <proxyHost/>, <proxyPort/>, <debug/>


Properties that are individual to a notifying logback appender are

FTPAppender

    <remoteFilename/>, <server/>

Notifo

    <title/>, <label/>, <uri/>
