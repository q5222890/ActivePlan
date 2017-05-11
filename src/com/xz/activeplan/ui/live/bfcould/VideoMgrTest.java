package com.xz.activeplan.ui.live.bfcould;

import com.xz.activeplan.utils.Utiles;

import java.io.UnsupportedEncodingException;

public class VideoMgrTest {
	private  String SecretKey= "ocddhokn1jLflQpZb667Qd09bXAUvKxt9i6hTfiT";
	private  String AccessKey = "=X6dhokn1S09lQY51667QdQs9c7jPYnXWZQPd3Y-";

	//	@Test
	public void createChannel() {
		String channelName = "中文频道";
		VideoMgr cm = new VideoMgr(SecretKey, AccessKey);
		try {
			//int res = cm.createChannel(channelName, 529, 1, 1, null, 0, 222, '\0', null);
			int res = cm.createChannel(channelName, 529, 1, 1, "中文测试", 0, 3600, '0', null);
			//int res = cm.createChannel(channelName, 529, 1, 1, "aaa", 1024*1024, 0, 11, "http://tom.test.com");
			//int res = cm.createChannel(channelName);
			System.out.print("" + res);
			String channelId = cm.getChannelId();
			String gcId = cm.getGCId();
			String url = cm.getURL();
			int errorCode = cm.getErrorCode();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//	@Test
	public void deleteChannel(String channelId) throws UnsupportedEncodingException {
		VideoMgr cm = new VideoMgr(SecretKey, AccessKey);
		int res = cm.deleteChannel(channelId);
		Utiles.log("--------deleteChannelId----"+res);
	}

	//	@Test
	public void getPlayChannelToken() throws UnsupportedEncodingException {
		String gcId = "367DB54E39364EB5BE6ED6742BC15E7E6C3F1C45";
		VideoMgr cm = new VideoMgr(SecretKey, AccessKey);
		String res = cm.getChannelPlayToken(gcId);
		System.out.print("" + res);
	}

	/************************for vod
	 * @throws UnsupportedEncodingException ***********************/
//	@Test
	public void testUpload() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(0, "");
		int status = vm.upload("C:\\中文测试.mp4", 1, 1, System.currentTimeMillis()/1000 + 60*60);
		Error error = new Error(status);
		System.out.println(error.getMessage());
		System.out.println(vm.getResponse());
	}

	//	@Test
	public void testDelete() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(0, "");
		int status = vm.delete("中文测试.mp4", System.currentTimeMillis()/1000 + 60*60);
		Error error = new Error(status);
		System.out.println(error.getMessage());
		System.out.println(vm.getResponse());
	}

	//	@Test
	public void testQuery() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(1, "");
		int status = vm.query("一心一意_20160805180045_6902.mp4");
		Error error = new Error(status);
		System.out.println(error.getMessage());
		Utiles.log("--------testQuery  error  "+error.getMessage());
		Utiles.log("--------testQuery  response  " +vm.getResponse());
	}

	//	@Test
	public void testChange() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(0, "");
		int status = vm.change("中文测试.mp4", 0);
		Error error = new Error(status);
		System.out.println(error.getMessage());
		System.out.println(vm.getResponse());
	}

	//	@Test
	public void testPlay() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(0, "");
		System.out.println(vm.createPlayToken("9DA22D66EA9082C4D0B0869B0D03ECFA",
				System.currentTimeMillis()/1000 + 60*60));
		int status;
		status = vm.getVideoPlayUrl("中文测试.mp4", 8374034, System.currentTimeMillis()/1000 + 60*60);
		//status = vm.getVideoPlayUrl("1909961527.mp4", 8374034, 1);
		//status = vm.getVideoPlayUrlByUrl("servicetype=1&uid=8374034&fid=E093A17612C74AA76768B49CF68CE75D", 1, 8374034);
		//status = vm.getVideoPlayUrlByUrl("servicetype=1&uid=8374034&fid=E093A17612C74AA76768B49CF68CE75D", 0, 8374034, 0);
		Error error = new Error(status);
		System.out.println(error.getMessage());
		System.out.println(vm.getResponse());
	}

	//	@Test
	public void testDownloadUrl() throws UnsupportedEncodingException {
		VideoMgr vm = new VideoMgr(SecretKey, AccessKey);
		vm.init(0);
		String url = vm.getDownloadUrl("中文测试.mp4", System.currentTimeMillis()/1000 + 60*60);
		System.out.println(url);
	}

}
