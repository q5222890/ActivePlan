/**
 *  Copyright (c) 2012, SXH COMPUTER TECH LTD.
 *  All rights reserved.
 *
 *  File name  :  MemoryCache.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MemoryCache {

	private static final String TAG = "MemoryCache";
	private boolean isDebug = false;

	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU最近最少使用算法
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, DrawableItem> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, DrawableItem>(10, 0.75f,
					true));

	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;

	// 缓存只能占用的最大堆内存
	private long limit = 1000000;

	public MemoryCache() {
		// 使用25%的可用内存
		setLimit(Runtime.getRuntime().maxMemory() / 20);
//		setLimit(1000000);
		if (isDebug)
			Log.i(TAG, "Max " + Runtime.getRuntime().maxMemory() / 1024.
					/ 1024. + "MB");
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		if (isDebug)
			Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024.
					+ "MB");
	}

	public Drawable getDrawable(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id).drawable;
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public synchronized void put(String id, DrawableItem drawableItem) {
		try {
			if (cache.containsKey(id))
				size -= cache.get(id).size;
			cache.put(id, drawableItem);
			size += cache.get(id).size;;
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 * 
	 */
	private void checkSize() {
		if (isDebug)
			Log.i(TAG, "cache size=" + size + " length=" + cache.size()
					+ " limit=" + limit);
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, DrawableItem>> iter = cache.entrySet()
					.iterator();
			List<Entry<String, DrawableItem>> delList = new ArrayList<Entry<String, DrawableItem>>();// 用来装需要删除的元素
			while (iter.hasNext()) {
				Entry<String, DrawableItem> entry = iter.next();
				size -= entry.getValue().size;
				delList.add(entry);
				if (size <= limit) {
					cache.entrySet().removeAll(delList);
					for(int i = 0; i < delList.size(); i++){
						if (delList.get(i).getValue().drawable != null){
							delList.get(i).getValue().drawable.setCallback(null);
							delList.get(i).getValue().drawable = null;
						}
					}
					delList.clear();
					break;
				}
			}
			if (isDebug)
				Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * 图片占用的内存
	 * 
	 * @param Drawable
	 * @return
	 */
	private long getSizeInBytes(Drawable drawable) {
		if (drawable == null)
			return 0;
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		long size = bitmap.getRowBytes() * bitmap.getHeight();
		bitmap.recycle();
		return size;
	}
	
	/**
	 * Drawable对应的内存大小
	 * @author Xiaozilong
	 */
	public class DrawableItem {
		public Drawable drawable;
		public int size;
	}
}