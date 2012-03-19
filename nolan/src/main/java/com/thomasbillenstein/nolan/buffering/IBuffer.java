package com.thomasbillenstein.nolan.buffering;

import java.util.List;

public interface IBuffer
{
	void setMaxSize(final int maxSize);

	void add(final String message);

	List<String> asList();
}
