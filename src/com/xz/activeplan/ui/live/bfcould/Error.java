package com.xz.activeplan.ui.live.bfcould;

public class Error {
	private int errorNum;
	private String message = null;
	
	public Error(int errornum) {
		errorNum = errornum;
	}
	
	public int getErrorNum() {
		return errorNum;
	}
	
	public String getMessage() {
		if (message != null){
			return message;
		}
		
		switch (errorNum) {
		case 0:
			message = "success";
			break;
		case 91:
			message = "91:set access key first";
			break;
		case 92:
			message = "92:set secret key first";
			break;
		case 99:
			message = "99:system error";
			break;
		case 101:
			message = "101:file already uploaded";
			break;
		case 102:
			message = "102:file is being uploaded";
			break;
		case 103:
			message = "103:file is being transfered";
			break;
		case 104:
			message = "104:user does not buy the service";
			break;
		case 105:
			message = "105:lack of user space";
			break;
		case 131:
			message = "131:user request is not supported";
			break;
		case 132:
			message = "132:illegal users";
			break;
		case 133:
			message = "133:parity error";
			break;
		case 134:
			message = "134:user request can not be resolved or missing fields";
			break;
		case 135:
			message = "135:file format is not supported";
			break;
		case 136:
			message = "136:file length long";
			break;
		case 137:
			message = "137:request timestamp expires";
			break;
		case 138:
			message = "138:file does not exist";
			break;
		case 200:
			message = "200:file is being uploaded";
			break;
		case 210:
			message = "210:file upload success";
			break;
		case 211:
			message = "211:file transfer success";
			break;
		case 212:
			message = "212:pass review";
			break;
		case 214:
			message = "214:cdn publish success";
			break;
		case 220:
			message = "220:upload failed";
			break;
		case 221:
			message = "221:transfer failed";
			break;
		case 222:
			message = "222:review failed";
			break;
		case 224:
			message = "224:CDN publish failed";
			break;
		case 230:
			message = "230:time out";
			break;
		case -1:
			message = "";
			break;
		case -2:
			message = "error occurs during full file uploading";
			break;
		case -3:
			message = "error occurs during fetching header information";
		case -4:
			message = "error occurs during resume uploading";
		default:
			message = "unknown error: " + errorNum;
			break;
		}
		return message;
	}
}
