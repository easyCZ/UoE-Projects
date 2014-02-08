package com.ug3.selp.timetableapp.parser;

import java.util.List;

import org.w3c.dom.Document;

/**
 * @author s1115104
 *
 * Abstract interface for parsers.
 *
 * @param <A>
 */
public interface Parser<A> {
	
	public abstract void extract(Document d);
	public abstract List<A> get();
	public Class<A> getMyType();

}