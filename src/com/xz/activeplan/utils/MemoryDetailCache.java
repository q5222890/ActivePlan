/**
 *  Copyright (c) 2012, SXH COMPUTER TECH LTD.
 *  All rights reserved.
 *
 *  File name  :  MemoryDetailCache.java
 *  Description:  内存缓存器
 *
 *  ========================================================================
 *  DATE     	AUTHOR   		VERSION  	REMARKS
 *  ----------------------------------------------------------------------
 *  2012-6-8 	XiaoZilong     	1.0   		 Create
 *  ======================================================================
 *
 */

package com.xz.activeplan.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryDetailCache {

	private static final String TAG = "MemoryDetailCache";
	private boolean isDebug = false;

	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU最近最少使用算法
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 0.75f, true));
	
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	long size = 0;
	
	// 缓存只能占用的最大堆内存
	private long limit = 1000000;
	
	public MemoryDetailCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
		if (isDebug)
		Log.i(TAG, "Max " + Runtime.getRuntime().maxMemory() / 1024. / 1024. + "MB");
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		if (isDebug)
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public synchronized void put(String id, Bitmap bitmap) {
		try {
//			if(cache.size() <= 0)
//				size = 0;
//			if (cache.containsKey(id))
//				size -= getSizeInBytes(cache.get(id));
//			cache.put(id, bitmap);
//			size += getSizeInBytes(bitmap);
//			checkSize();

			if(cache.size() <= 0)
				size = 0;
			if (!cache.containsKey(id)){
				cache.put(id, bitmap);
				size += getSizeInBytes(bitmap);
				checkSize();
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最少使用的那个图片缓存
	 * 
	 */
	private void checkSize() {
		if (isDebug)
		Log.i(TAG, "cache size=" + size + " length=" + cache.size()+" limit="+limit);
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			List<Entry<String, Bitmap>> delList = new ArrayList<Entry<String, Bitmap>>();// 用来装需要删除的元素
			Bitmap bitmapTemp = null;
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				delList.add(entry);
				if (size <= limit){
					cache.entrySet().removeAll(delList);
					for(int i = 0; i < delList.size(); i++){
						bitmapTemp = delList.get(i).getValue();
						if (bitmapTemp != null && !bitmapTemp.isRecycled()){
							bitmapTemp.recycle();
							bitmapTemp = null;
						}
					}
					System.gc();
					break;
				}
			}
			if (isDebug)
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}
	
	public boolean isOutCache(Bitmap bitmap){
		if(getSizeInBytes(bitmap) + size > limit){
			return true;
		}
		return false;
	}
	
	public void clear() {
		cache.clear();
	}

	public Map<String, Bitmap> getCache(){
		return cache;
	}
	/**
	 * 图片占用的内存
	 * 
	 * @param bitmap
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}