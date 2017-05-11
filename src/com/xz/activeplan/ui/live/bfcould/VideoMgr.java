package com.xz.activeplan.ui.live.bfcould;


import com.xz.activeplan.utils.HMACSHA1;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.alipay.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;


public class VideoMgr {
	private final String IFPUBLIC = "ifpublic";
	private final String CHANNLENAME = "channelname";
	private final String DEADLINE = "deadline";
	private final String CHANNELID = "channelid";
	private final String IFRECORD = "ifrecord";
	private final String RECORDNAME = "recordname";
	private final String SIZELIMIT = "sizelimit";
	private final String TIMELIMIT = "timelimit";
	private final String RECORDPOSITION = "recordposition";
	private final String CALLBACK = "callback";

	private final String CREATE_CHANNEL_URL = "http://channelmgr.baofengcloud.com/createchannel";
	private final String DELETE_CHANNEL_RUL = "http://channelmgr.baofengcloud.com/deletechannel";
	private final String LIVEQUERY_URL = "http://livequery.baofengcloud.com/";
	//for vod
	//added 9th July 2015
	///begin
	private final String _ACCESS_URL = "http://access.baofengcloud.com/";
	private final String _CDNQUERY_URL = "http://cdnquery.baofengcloud.com/";
	private final int _segment_length = 4194304;	//断点续传最大段长
	private  String SecretKey= "ocddhokn1jLflQpZb667Qd09bXAUvKxt9i6hTfiT";
	private  String AccessKey = "=X6dhokn1S09lQY51667QdQs9c7jPYnXWZQPd3Y-";
	private String ChannelId = null;
	private String GCId = null;
	private String URL = null;
	private int mErrorCode = 0;
	private int _uptype = 0;				//0:表示表单上传，1:表示断点续传
	private int _servicetype = 1;			//业务id，0：视频云托管服务，1： 点播服务
	private String _filekey = null;			//自定义的文件名前缀，可为空
	private String _callback_url = null;	//回调url，视频上传成功后会返回结果给回调url，可为空

	private String _response = "";		//response from server
	private LinkList linkList = new LinkList();

	private int _enable_html5 = 0;
	///end

	public VideoMgr(String SecretKey, String AccessKey){
		this.SecretKey = SecretKey;
		this.AccessKey = AccessKey;
		if (judgeAkSk())
			//System.out.println("SecretKey or AccessKey is invailid");
			;
	}

	public static void print(String str){
		System.out.print(str + "\n");
	}

	private String sendPostAndGetResult(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	private String getCreateToken(String channelname, long deadline, int ifpublic, int ifrecord,
								  String recordname, long sizelimit, int timelimit, int recordposition,
								  String callback) throws UnsupportedEncodingException{
		String token = null;
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put(IFPUBLIC, ifpublic);
			jsonObj.put(CHANNLENAME, channelname);
			jsonObj.put(DEADLINE, deadline);
			if (!(callback == null || callback.length() == 0)) {
				jsonObj.put(CALLBACK, callback);
			}

			if (ifrecord == 0) {
				jsonObj.put(IFRECORD, ifrecord);
			} else {
				jsonObj.put(IFRECORD, ifrecord);
				if (!(recordname == null || recordname.length() == 0)) {
					jsonObj.put(RECORDNAME, recordname);
				}
				if (sizelimit != 0) {
					jsonObj.put(SIZELIMIT, sizelimit);
				}
				if (timelimit != 0) {
					jsonObj.put(TIMELIMIT, timelimit);
				}
				if (recordposition == 0 || recordposition == 1) {
					jsonObj.put(RECORDPOSITION, recordposition);
				}
			}
		}catch (Exception e)
		{

		}
		print("--------------mrg---------"+jsonObj.toString());
		String EncodedJson = getBase64UrlEncodeResoult(jsonObj.toString());
		print("--------------mrg--EncodedJson-------"+EncodedJson);
		EncodedJson = EncodedJson.replaceAll("\r|\n", "");
		token = AccessKey + ":" + getEncodeSign(EncodedJson) + ":" + EncodedJson;
		print("--------------mrg--AccessKey-------"+AccessKey);
		print("--------------mrg--getEncodeSign-------"+getEncodeSign(EncodedJson).toString());
		print("--------------mrg--EncodedJson-------"+EncodedJson);
		return token;
	}

	private String getEncodeSign(String str){
		byte[] sign = null;
		try {
			sign = HMACSHA1.HmacSHA1Encrypt(str, SecretKey);
		} catch (Exception e) {
			e.printStackTrace();

		}
		String encodeSign = getBase64UrlEncodeResoult(sign);
		return encodeSign;
	}

	private String getDeleteToken(String channelId, long deadline) throws UnsupportedEncodingException{
		String token = null;
		JSONObject jsonObj = new JSONObject();

		try {
			jsonObj.put(CHANNELID, channelId);
			jsonObj.put(DEADLINE, deadline);
		} catch (JSONException e) {

		}

		String EncodedJson = getBase64UrlEncodeResoult(jsonObj.toString());
		EncodedJson = EncodedJson.replaceAll("\r|\n", "");

		token = AccessKey + ":" + getEncodeSign(EncodedJson) + ":" + EncodedJson;
		return token;
	}

	private String getBase64UrlEncodeResoult(String str) throws UnsupportedEncodingException{
		if (str == null || str.length() == 0)
			return null;
		return  Base64.encode(str.getBytes("UTF-8"));
	}

	private String getBase64UrlEncodeResoult(byte[] str){
		return Base64.encode(str);
	}

	//使用缺省值的重载：私有、不录制
	public int createChannel(String name) throws UnsupportedEncodingException{
		return createChannel(name, 3600, 0, 0, null, 0, 0, 0, null);
	}

	//name: 频道名称
	//deadline: 从现在开始有效的秒数
	//ifpublic: 0私有 1公有
	//ifrecord: 是否允许录制，0表示不录制，1表示录制，取1时下面的参数有效
	//recordname: 录制文件名前缀，可以为空，如果不指定该字段则录制文件以频道名称为前缀
	//sizelimit: 录制文件最大字节数，以B为单位，最大不超过2G，与timelimit二选一。设置一个，另一个设为0
	//timelimit: 录制文件最大时长，以秒为单位，最大不超过2小时，与sizelimit二选一。设置一个，另一个设为0
	//recordposition: 录制文件存储位置，0表示云托管，1表示点播，默认存储到云托管
	//callback: 录制完成时的回调地址，可以为空，如果不指定该字段则不回调
	public int createChannel(String name, long deadline, int ifpublic, int ifrecord,
							 String recordname, long sizelimit, int timelimit, int recordposition,
							 String callback) throws UnsupportedEncodingException{
		if (!judgeAkSk()){
			return -1;
		}
		if (name == null || name.length() == 0 || deadline < 0 || (ifpublic != 1 && ifpublic != 0) ||
				(ifrecord != 1 && ifrecord != 0) || sizelimit < 0 || timelimit < 0 ||
				(ifrecord == 1 && sizelimit == 0 && timelimit == 0))
			return -1;
		String token = getCreateToken(name, System.currentTimeMillis()/1000 + deadline, ifpublic,
				ifrecord, recordname, sizelimit, timelimit, recordposition, callback);
		String postContent = makePostContent(token);
		Utiles.log("---------createC- token----"+token);
		Utiles.log("---------createC-- postContent---"+postContent);
		String response = sendPostAndGetResult(CREATE_CHANNEL_URL, postContent);
		Utiles.log("---------createC-----"+response);
		try {
			JSONObject jsonReponse = new JSONObject(response);
			mErrorCode = jsonReponse.getInt("status");
			if (mErrorCode != 0)
				return mErrorCode;
			ChannelId = jsonReponse.getString("channelid");
			GCId = jsonReponse.getString("gcid");
			URL = jsonReponse.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private boolean judgeAkSk(){
		if (AccessKey == null || AccessKey.length() == 0 || SecretKey == null || SecretKey.length() == 0){
			print("AccessKey or SecretKey is invailid");
			return false;
		}
		return true;
	}

	public int deleteChannel(String channelId, long deadline) throws UnsupportedEncodingException{
		if (!judgeAkSk()){
			return -1;
		}
		if (channelId == null || channelId.length() == 0 || deadline < 0)
			return -1;
		String token = getDeleteToken(channelId, System.currentTimeMillis()/1000 + deadline);
		String postContent = makePostContent(token);
		String reponse = sendPostAndGetResult(DELETE_CHANNEL_RUL, postContent);
		Utiles.log("--------delete------"+reponse);
		try {
			JSONObject jsonObj =new JSONObject(_response);
			int errorCode = jsonObj.getInt("status");
			if (errorCode != 0)
				return errorCode;
			ChannelId = null;
			GCId = null;
			URL = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteChannel(String channelId) throws UnsupportedEncodingException{
		return deleteChannel(channelId, 120);
	}

	public String getSecretKey() {
		return SecretKey;
	}

	public void setSecretKey(String mSecretKey) {
		if(mSecretKey == null || mSecretKey.length() == 0){
			print("mSecretKey is invailid");
			return;
		}
		this.SecretKey = mSecretKey;
	}

	public String getAccessKey() {
		return AccessKey;
	}

	public void setAccessKey(String mAccessKey) {
		if(mAccessKey == null || mAccessKey.length() == 0){
			print("mSecretKey is invailid");
			return;
		}
		this.AccessKey = mAccessKey;
	}

	public String getChannelId() {
		return ChannelId;
	}

	public String getGCId() {
		return GCId;
	}

	public String getURL() {
		return URL;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	private String makePostContent(String t){
		JSONObject jo = new JSONObject();
		try {
			jo.put("token", t);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}

	private String getStringLastChar(String str){
		if (str == null || str.length() == 0)
			return null;
		return str.charAt(str.length() - 1) + "";
	}

	public String getChannelPlayToken(String gcId, long deadline) throws UnsupportedEncodingException{
		if (!judgeAkSk()){
			return null;
		}
		if (gcId == null || gcId.length() == 0 || deadline < 0){
			print("gcId is invailid");
			return null;
		}
		String data = LIVEQUERY_URL + gcId + "?" + DEADLINE + "=" + (System.currentTimeMillis()/1000 + deadline);
		String encodedData = getBase64UrlEncodeResoult(data);
		encodedData = encodedData.replaceAll("\r|\n", "");
		String encodedSign = getEncodeSign(encodedData);
		String token= AccessKey + ':'+ encodedSign + ':'+ encodedData;
		return token;
	}

	public String getChannelPlayToken(String gcId) throws UnsupportedEncodingException{
		return getChannelPlayToken(gcId, 3600);
	}


	//////********************for vod**********************////////

	public void init(int servicetype, String filekey, String callback_url){
		if (servicetype != 0 && servicetype != 1){
			print("servicetype must be 0 or 1");
			try {
				throw new Exception("servicetype must be 0 or 1");
			} catch (Exception e) {
			}
		}
		this._servicetype = servicetype;
		this._filekey = filekey;
		this._callback_url = callback_url;
	}

	public void init(int servicetype, String filekey){
		init(servicetype, filekey, "");
	}

	public void init(int servicetype){
		init(servicetype, "", "");
	}

	public String getResponse(){
		return this._response;
	}

	private void setResponse(String response){
		this._response = response;
	}

	/*
	 * encode token
	 * @param JsonObject
	 * @return token in json format
	 */
	private String token_encode(JSONObject jsonObj) throws UnsupportedEncodingException{
		print(jsonObj.toString());
		String EncodedJson = getBase64UrlEncodeResoult(jsonObj.toString());
		EncodedJson = EncodedJson.replaceAll("\r|\n", "");
		String token = AccessKey + ":" + getEncodeSign(EncodedJson) + ":" + EncodedJson;
		//print(token);
		//print(makePostContent(token));
		return makePostContent(token);
	}

	/*
	 * upload file once
	 * @param string $url
	 * @param string $filelname
	 * @return 0:success -2:error occurs
	 */
	private int uploadFullFile(String url, String filename){
		DataOutputStream out = null;
		BufferedReader in = null;
		File file = new File(filename);
		String result = "";
		int responseCode = 0;
		String responseMessage = "unknown response message";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("connection", "close");
			conn.setRequestProperty("Content-Type", "application/octet-stream");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new DataOutputStream(conn.getOutputStream());
			// 发送请求
			DataInputStream din = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = din.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			din.close();
			// flush输出流的缓冲
			out.flush();
			out.close();

			responseCode = conn.getResponseCode();
			responseMessage = conn.getResponseMessage();

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (responseCode != 200){
			setResponse(responseCode + " " + responseMessage + "\n" + result);
			return -2;
		} else {
			setResponse(result);
			return 0;
		}
	}

	/*
	 * read the range information form the header
	 * and save it into object: this.linkList
	 * @param original range information
	 * @param size of the file to be uploaded
	 * @return void
	 */
	private void readHeadRange(String range, long filesize){
		if (range.equals("[]") || range == null) {
			linkList.add(0, filesize - 1);
			return;
		}

		range = range.substring(1);	//delete '['
		range = range.substring(0, range.length() - 1);	//delete ']'
		String rangeArr[] = range.split(",");
		long start = 0;
		for (int i = 0; i < rangeArr.length; i++) {
			String tmp[] = rangeArr[i].split("-");
			if (Integer.parseInt(tmp[0]) != 0) {
				linkList.add(start, Integer.parseInt(tmp[0]) - 1);
			}
			if (tmp.length == 1) {
				return;		//deal with case: [0-10,20-30,40-]
			}
			start = Integer.parseInt(tmp[1]) + 1;
		}
		if (start <= filesize - 1) {
			linkList.add(start, filesize - 1);
		}

		//linkList.printList();
	}

	/*
	 * resume upload
	 * cut large files into fragments at limit of 4 MegaByte
	 * @param the url to post
	 * @param the file to be uploaded
	 * @return 0:success -3:header info fetching failed -4:upload failed
	 */
	private int resumeUpload(String url, String filename){
		File file = new File(filename);
		long filesize = file.length();
		int responseCode = 0;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("connection", "close");
			conn.setRequestProperty("Content-Length", "0");
			conn.setRequestProperty("Content-Range", "bytes */" + filesize);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.getOutputStream();

			responseCode = conn.getResponseCode();
			Map<String, List<String>> headers = conn.getHeaderFields();
			readHeadRange("" + headers.get("Range"), filesize);
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("Exception caught during fetching header information. " + e);
			e.printStackTrace();
		}
		if (responseCode != 308){
			setResponse("HTTPResponseCode: " + responseCode);
			return -3;
		}


		DataOutputStream out = null;
		BufferedReader in = null;
		String result = "";
		responseCode = 0;
		String responseMessage = "unknown response message";
		try {
			DataInputStream din = new DataInputStream(new FileInputStream(file));
			byte[] bufferOut = new byte[_segment_length];
			int len = -1;
			long start = 0;
			while ((len = din.read(bufferOut)) != -1) {
				//print(""+len);
				if (!linkList.delete(start, start + len -1)) {
					start += len;
					continue;
				}
				URL realUrl = new URL(url);
				// 打开和URL之间的连接
				HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
				// 设置通用的请求属性
				//conn.setRequestProperty("Connection", "close");
				conn.setRequestProperty("Content-Length", "" + len);
				conn.setRequestProperty("Content-Range", "bytes " + start + "-" + (start + len - 1) + "/" + filesize);
				MessageDigest messageDigest =MessageDigest.getInstance("MD5");
				messageDigest.update(bufferOut, 0, len);
				String md5 = Tools.byteArrayToHex(messageDigest.digest());
				conn.setRequestProperty("Content-MD5", md5);
				conn.setRequestProperty("Content-Type", "application/octet-stream");
				// 发送POST请求必须设置如下两行
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// 获取URLConnection对象对应的输出流
				out = new DataOutputStream(conn.getOutputStream());
				// 发送请求
				out.write(bufferOut, 0, len);
				// flush输出流的缓冲
				out.flush();
				out.close();

				responseCode = conn.getResponseCode();
				responseMessage = conn.getResponseMessage();

				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				result = "";
				while ((line = in.readLine()) != null) {
					result += line;
				}
				in.close();

				if (responseCode != 200 && responseCode != 206) {
					setResponse(responseCode + " " + responseMessage + "\n" + result);
					return -4;
				}
				if (responseCode == 200) {
					break;
				}
				conn.disconnect();
				start += len;
			}//while(din.read())
			din.close();
		} catch (Exception e) {
			System.out.println("Exception caught during resumeUpload. " + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (responseCode == 200){
			setResponse(result);
			return 0;
		}
		return -1;
	}

	/*
	 * Upload video to the platform
	 * @param  filename
	 * @param  filetype  0:private 1:public
	 * @param  uptype    0:full file upload 1:resume upload
	 * @return status    0:success
	 */
	public int upload(String filename, int filetype, int uptype, long deadline) throws UnsupportedEncodingException{
		if (filename == null || filename.length() == 0){
			print("filename can not be empty");
			return -1;
		}
		if (filetype != 0 && filetype != 1){
			print("filetype must be 0 or 1");
			return -1;
		}
		if (uptype != 0 && uptype != 1){
			print("uptype must be 0 or 1");
			return -1;
		}

		File file = new File(filename);
		if (!file.exists()){
			print("file not exist");
			return -1;
		}
		if (!file.isFile()){
			print("not a normal file, maybe a directory");
			return -1;
		}
		long filesize = file.length();
		String ns[] =filename.split("\\\\");
		print(ns[ns.length - 1]);

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("uptype", uptype);

		jsonObj.put("servicetype", this._servicetype);
		jsonObj.put("filename", ns[ns.length - 1]);
		jsonObj.put("filesize", filesize);
		jsonObj.put("filetype", filetype);
		jsonObj.put("deadline", deadline);
		if (this._filekey.length() != 0){
			jsonObj.put("filekey", this._filekey);
		}
		if (this._callback_url.length() != 0){
			jsonObj.put("callbackurl", this._callback_url);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String token = token_encode(jsonObj);
		print(token);
		String response = sendPostAndGetResult(_ACCESS_URL + "upload", token);
		print(response);

		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status != 0){
			if (status == -1){
				print("'upload url' status get failed");
			}
			return status;
		}

		//print(jsonResponse.getString("url"));
		try {
		if (uptype == 0){

				status = uploadFullFile(jsonResponse.getString("url"), filename);

		} else {
			status = resumeUpload(jsonResponse.getString("url"), filename);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return status;
	}

	/*
	 * delete a video on server
	 * @param file name to delete
	 * @return status 0:success
	 */
	public int delete(String filename, long deadline) throws UnsupportedEncodingException {
		if (filename == null || filename.length() == 0){
			print("filename can not be empty");
			return -1;
		}

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("servicetype", this._servicetype);

		jsonObj.put("filename", filename);
		jsonObj.put("deadline", deadline);
		if (this._filekey.length() != 0){
			jsonObj.put("filekey", this._filekey);
		}
		if (this._callback_url.length() != 0){
			jsonObj.put("callbackurl", this._callback_url);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String token = token_encode(jsonObj);

		String response = sendPostAndGetResult(_ACCESS_URL + "delete", token);
		setResponse(response);

		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status == -1){
			print("'delete' status get failed");
		}
		return status;
	}

	/*
	 * query video information
	 * @param the file name to query
	 * @return status
	 */
	public int query(String filename) throws UnsupportedEncodingException {
		if (filename == null || filename.length() == 0){
			print("filename can not be empty");
			return -1;
		}

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("servicetype", this._servicetype);

		jsonObj.put("filename", filename);
		if (this._filekey.length() != 0){
			jsonObj.put("filekey", this._filekey);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String token = token_encode(jsonObj);

		String response = sendPostAndGetResult(_ACCESS_URL + "query", token);
		setResponse(response);
		Utiles.log("--------Query  response  "+response);
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status == -1){
			print("'query' status get failed");
		}
		return status;
	}

	/*
	 * change video property
	 * @param the file to change
	 * @param the property to change to. 0:private 1:public
	 * @return status
	 */
	public int change(String filename, int ifpublic) throws UnsupportedEncodingException {
		if (filename == null || filename.length() == 0){
			print("filename can not be empty");
			return -1;
		}
		if (ifpublic != 0 && ifpublic != 1){
			print("ifpublic must be 0 or 1");
			return -1;
		}

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("servicetype", this._servicetype);

		jsonObj.put("filename", filename);
		jsonObj.put("filetype", ifpublic);
		if (this._filekey.length() != 0){
			jsonObj.put("filekey", this._filekey);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String token = token_encode(jsonObj);

		String response = sendPostAndGetResult(_ACCESS_URL + "changeproperty", token);
		setResponse(response);

		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status == -1){
			print("'property change' status get failed");
		}
		return status;
	}

	/*
	 * create token for private video
	 * @param file id
	 * @return token after urlencode
	 */
	public String createPlayToken(String fid, long deadline) {
		if (fid == null || fid.length() == 0) {
			return "";
		}

		String token = "id=" + fid + "&deadline=" + deadline + "&enablehtml5=" + _enable_html5;
		String encoded = Base64.encode(token.getBytes());
		encoded = encoded.replaceAll("\r|\n", "");
		token = AccessKey + ":" + getEncodeSign(encoded) + ":" + encoded;
		try {
			token = URLEncoder.encode(token, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("UTF-8 not supported. " + e);
			e.printStackTrace();
		}
		return token;
	}

	/*
	 * get video play url, result written in this._response
	 * @param filename
	 * @param userid
	 * @param whether to play automatically. 1:auto
	 * @return status. 0:success
	 */
	public int getVideoPlayUrl(String filename, int userid, int auto, long deadline) throws UnsupportedEncodingException {
		if (filename == null || filename.length() == 0) {
			print("filename can not be empty");
			return -1;
		}
		if (auto != 0 && auto != 1) {
			print("auto must be 0 or 1");
			return -1;
		}

		query(filename);
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(_response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status != 0){
			print("'query' result get failed");
			return status;
		}

		String play_url = "http://vod.baofengcloud.com/" + userid + "/cloud.swf";
		String url = null;
		try {
			url = jsonResponse.getString("url").trim();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		play_url += "?" + url;
		try {
			if (jsonResponse.getInt("ifpublic") != 1) {
                String token = createPlayToken(url.substring(url.length() - 32), deadline);
                play_url += "&tk=" + token;
            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		play_url += "&auto=" + auto;
		setResponse(play_url);

		return 0;
	}

	public int getVideoPlayUrl(String filename, int userid, long deadline) throws UnsupportedEncodingException {
		return getVideoPlayUrl(filename, userid, 1, deadline);
	}

	public int getVideoPlayUrlByUrl(String url, int ifpublic, int userid, int auto, long deadline) {
		if (url == null || url.length() == 0) {
			print("url can not be empty");
			return -1;
		}
		if (ifpublic != 0 && ifpublic != 1) {
			print("ifpublic must be 0 or 1");
			return -1;
		}
		if (auto != 0 && auto != 1) {
			print("auto must be 0 or 1");
			return -1;
		}

		url = url.trim();
		String play_url = "http://vod.baofengcloud.com/" + userid + "/cloud.swf";
		play_url += "?" + url;
		if (ifpublic != 1) {
			String token = createPlayToken(url.substring(url.length() - 32), deadline);
			play_url += "&tk=" + token;
		}
		play_url += "&auto=" + auto;
		setResponse(play_url);

		return 0;
	}

	public int getVideoPlayUrlByUrl(String url, int ifpublic, int userid, long deadline) {
		return getVideoPlayUrlByUrl(url, ifpublic, userid, 1, deadline);
	}

	/*
	 * send a Http GET request to the given url
	 * @param the url
	 * @return server response body
	 */
	private String sendGetAndGetResult(String url) {
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			conn.setDoInput(true);
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * get the downloading url of a video
	 * @param the file name
	 * @return the recommended url
	 */
	public String getDownloadUrl(String filename, long deadline) throws UnsupportedEncodingException {
		if (filename == null || filename.length() == 0) {
			print("filename can not be empty");
			return "";
		}

		query(filename);
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(_response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int status = jsonResponse.optInt("status", -1);
		if (status != 0) {
			print("'query' result get failed");
			return "";
		}

		String url = null;
		try {
			url = jsonResponse.getString("url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String encoded = Base64.encode(url.getBytes());
		encoded = encoded.replaceAll("\r|\n", "");
		String query_url = _CDNQUERY_URL + encoded;

		int ifpublic = 0;
		try {
			ifpublic = jsonResponse.getInt("ifpublic");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (ifpublic != 1) {
			String token = createPlayToken(url.substring(url.length() - 32), deadline);
			query_url += "?tk=" + token;
		}
		String result = sendGetAndGetResult(query_url);
       JSONArray urllist= null;
		try {
			jsonResponse = new JSONObject(result);

			urllist = jsonResponse.getJSONArray("urllist");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			return urllist.getString(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int get_enable_html5() {
		return _enable_html5;
	}

	public void set_enable_html5(int _enable_html5) {
		this._enable_html5 = _enable_html5;
	}
}
