package com.xz.activeplan.utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Global;

/**
 * 录音
 */
public class RecordManager {

	public static final String RECORD_SUFFIX = ".amr";
	private static final int MSG_VOICE_CHANGE = 1;

	private Handler handler = new Handler(new Handler.Callback() {

		public boolean handleMessage(Message msg) {

			if (msg.what == MSG_VOICE_CHANGE) {
				if (listener != null) {
					listener.onRecordVoiceChange((Integer) msg.obj);
				}
			}

			return false;
		}
	});

	public interface RecordStateListener {

		public void onRecordStartLoading();

		public void onRecordStart();

		public void onRecordFinish(String file);

		public void onRecordCancel();

		public void onRecordVoiceChange(int v);

		public void onTimeChange(int seconds);

		public void onRecordError();

		public void onTooShoot();

	}

	private void notifyStartLoading() {
		if (listener != null) {
//			handler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					listener.onRecordStartLoading();
//
//				}
//			});
			
			listener.onRecordStartLoading();

		}
	}

	private void notifyTooShoot() {

		if (listener != null) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					listener.onTooShoot();

				}
			});

		}
	}

	private void notifyStart() {
		if (listener != null) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					listener.onRecordStart();

				}
			});

		}
	}

	private void notifyFinish(final String file) {

		if (listener != null) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					listener.onRecordFinish(file);

				}
			});

		}
	}

	private void notifyCancal() {

		if (listener != null) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					listener.onRecordCancel();

				}
			});

		}
	}

	private void notifyVoiceChange(int v) {

		Message message = new Message();
		message.what = MSG_VOICE_CHANGE;
		message.obj = v;

		handler.sendMessage(message);

	}

	private RecordStateListener listener;

	private static RecordManager instance;

	public RecordManager() {

	}

	public static RecordManager getInstance() {
		if (instance == null) {
			instance = new RecordManager();
		}
		return instance;
	}

	MediaRecorder mr;

	private String name;

	public synchronized String genName() {

		return System.currentTimeMillis() + RECORD_SUFFIX;
	}

	public boolean isRunning() {
		return running;
	}

	private long startTime = System.currentTimeMillis();

	public void startRecord(String voices_dir) {

		// Thread recordThread = new Thread(new Runnable() {

		// @Override
		// public void run() {

		try {

			notifyStartLoading();

			mr = new MediaRecorder();

			mr.setAudioSource(AudioSource.MIC);
			// 设置音源,这里是来自麦克风,虽然有VOICE_CALL,但经真机测试,不行
			mr.setOutputFormat(OutputFormat.RAW_AMR);
			// 输出格式
			mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			name = voices_dir + File.separator + genName();

			// 编码
			mr.setOutputFile(name);

			mr.prepare();

			notifyStart();

			// 做些准备工作
			mr.start();

			startTime = System.currentTimeMillis();

			running = true;

			timer = new Timer();

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// "running....");
					// TODO Auto-generated method stub
					int i = mr.getMaxAmplitude();

					if (listener != null) {

						int seconds = (int) ((System.currentTimeMillis() - startTime));

						notifyVoiceSecondsChange(seconds);
						notifyVoiceChange(i);
					}
				}
			}, 0, 50);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			notifyError();
		}

		// }
		// });

		// recordThread.start();

	}

	private Timer timer = new Timer();

	private void notifyError() {
		handler.post(new Runnable() {

			public void run() {

				listener.onRecordError();
			}
		});
	}

	private void notifyVoiceSecondsChange(final int seconds) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				listener.onTimeChange(seconds);

			}
		});

	}

	private boolean running = false;

	private void stopVolumeListener() {

		if (timer != null) {

			timer.cancel();
		}
	}

	public void waitRunning() {

		if (true) {
			return;
		}
		while (!running) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stopRecord() {

		stopVolumeListener();

		if (mr != null) {
			try {
				mr.stop();
				mr.release();
			} catch (Exception e) {
				e.printStackTrace();
			}

			long delay = System.currentTimeMillis() - startTime;

			if (delay <= 500) {
				notifyTooShoot();
			} else {
				notifyFinish(name);
			}
		}
		running = false;
	}

	public synchronized void cancel() {

		stopVolumeListener();

		if (mr != null) {
			try {

				mr.release();

			} catch (Exception e) {
				e.printStackTrace();
			}

			File file = new File(name);

			file.deleteOnExit();

			notifyCancal();

		}
		running = false;
	}

	public void setVoiceVolumeListener(RecordStateListener listener) {
		this.listener = listener;
	}

}
