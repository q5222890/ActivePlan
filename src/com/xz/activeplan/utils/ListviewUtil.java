package com.xz.activeplan.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ListviewUtil {

	   public static void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {  
	            // pre-condition  
	            return;  
	        }  
	  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        listView.setLayoutParams(params);  
	    } 
	   //加额外的高度
	   public static void setListViewHeightBasedOnChildren(ListView listView,Double extraHeight) {  
		   ListAdapter listAdapter = listView.getAdapter();   
		   if (listAdapter == null) {  
			   // pre-condition  
			   return;  
		   }  
		   
		   int totalHeight = 0;  
		   for (int i = 0; i < listAdapter.getCount(); i++) {  
			   View listItem = listAdapter.getView(i, null, listView);  
			   listItem.measure(0, 0);  
			   totalHeight += listItem.getMeasuredHeight();  
		   }  
		   
		   ViewGroup.LayoutParams params = listView.getLayoutParams();  
		   params.height = (int) (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+extraHeight);  
		   listView.setLayoutParams(params);  
	   } 
	   
	   public static void setGridViewHeightBasedOnChildren(GridView gridView) {  
		   ListAdapter listAdapter = gridView.getAdapter();   
		   if (listAdapter == null) {  
			   // pre-condition  
			   return;  
		   }  
//		   long num=gridView.getNumColumns();
		   float childcount=listAdapter.getCount();
		   Double line=Math.ceil(childcount/4.0);
		   int totalHeight = 0;  
		   for (int i = 0; i < line; i++) {  
			   View listItem = listAdapter.getView(i, null, gridView);  
			   listItem.measure(0, 0);  
			   totalHeight += listItem.getMeasuredHeight();  
		   }  
		   
		   ViewGroup.LayoutParams params = gridView.getLayoutParams();  
		   params.height = (int) (totalHeight+(line-1)*10); 
		   gridView.setLayoutParams(params);  
		   gridView.setVerticalSpacing(10);
	   } 
	   //给定高度
	   public static void setGridViewHeight(GridView gridView,int height) {  
		   if (gridView == null) {  
			   // pre-condition  
			   return;  
		   }  
		   
		   ViewGroup.LayoutParams params = gridView.getLayoutParams();  
		   params.height = height; 
		   gridView.setLayoutParams(params);  
		   gridView.setVerticalSpacing(10);
	   } 
	   
	   //二级评论增加的高度
	   public static double computeScHeight(List<String> list){
		   double height=0;
		   double h=0;
		   double line=0;
		   int num=0;
		   try {
			   if(list!=null&&list.size()>0){
				  for (int i = 0; i < list.size(); i++) {
					String content=list.get(i);
					if (content.length()>17) {
						line=Math.ceil(content.length()/17.0);
						h=(line+1)*30;
						num++;
					}
					if(i==list.size()-1){
						if(num>5) {
							h+=num*20;
						}else{
							h+=num*5;
						}
					}
					height+=h;
					h=0;
				}
				  return height;
			   }
		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	   }
	   
}
