package io.rong;

import java.util.ArrayList;
import java.util.List;

import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

public class Test {

	
	public static void main(String args[]){
		List<String> list = new ArrayList<String>();
		list.add("9");
		try {
			//SdkHttpResult result = ApiHttpClient.createGroup("8brlm7ufra0p3", "LMKw6KmyqKOGp", list, "100", "1000goru", FormatType.json);
			
			SdkHttpResult result = ApiHttpClient.queryGroupUserList("8brlm7ufra0p3", "LMKw6KmyqKOGp", "100", FormatType.json);
			
			System.out.println(result.getResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
