package com.thomasbillenstein.nolan.examples.ftp.server;

import java.io.IOException;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;

class TestFtplet extends DefaultFtplet
{
    public TestFtplet()
	{
	}

	@Override
    public FtpletResult onUploadEnd(final FtpSession session, final FtpRequest request) throws FtpException, IOException
    {
    	String fileName = request.getArgument().replace('\\', '/');
    	final int slashIdx = fileName.lastIndexOf('/');
    	if (slashIdx >= 0)
    	{
    		fileName = fileName.substring(slashIdx + 1);
    	}
    	final String homeFolder = session.getUser().getHomeDirectory();
    	final String filePath = new StringBuilder(256).
    		append(homeFolder).
    		append("/").
    		append(fileName).toString();

    	System.out.println("target file path: " + filePath);

        return super.onUploadEnd(session, request);
    }
}

