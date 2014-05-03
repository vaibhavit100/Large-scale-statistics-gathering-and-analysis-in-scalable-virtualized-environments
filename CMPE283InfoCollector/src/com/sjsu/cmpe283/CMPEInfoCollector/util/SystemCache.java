/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Bhargav
 *
 */
public class SystemCache {
	private static ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	private static SystemCache systemCache = new SystemCache();
	
	private SystemCache(){
			
	}

	public static SystemCache getInstance( ) {
	      return systemCache;
	}
	
	public ConcurrentMap<String, Object> getCache(){
		return this.cache;
	}
}
