package com.xz.activeplan.ui.live.bfcould;

public class Tools {
	
	/*
	 * convert byte array to hexadecimal digits
	 */
	public static String byteArrayToHex(byte[] byteArray) {  
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f'};
		//char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F'};
		char[] resultCharArray =new char[byteArray.length * 2];		//one byte corresponds to two HexDigits
		int index = 0;  
		for (byte b : byteArray) {  
			resultCharArray[index++] = hexDigits[(b & 0xf0) >> 4];  
			resultCharArray[index++] = hexDigits[b & 0x0f];  
		}  
		return new String(resultCharArray);
	}
}
