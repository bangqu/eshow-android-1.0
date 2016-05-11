package cn.org.eshow.demo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.framwork.util.AbLogUtil;
import cn.org.eshow.framwork.util.AbStrUtil;
import cn.org.eshow.framwork.util.AbToastUtil;
import cn.org.eshow.framwork.util.AbViewUtil;
import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.InactivityTimer;
import zxing.decoding.RGBLuminanceSource;
import zxing.view.ViewfinderView;

public class ScanActivity extends CommonActivity implements Callback,OnClickListener{
	private Context context = ScanActivity.this;
	/** scan */
	public CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	public Vector<BarcodeFormat> decodeFormats;
	public String characterSet;
	public InactivityTimer inactivityTimer;
	public MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	/** Called when the activity is first created. */
	
	ImageButton ibLignt;
	ImageButton ibPhoto;
	ImageButton ibClose;
	private Parameters parameters;
	private static final int REQUEST_CODE = 100;
	private ProgressDialog mProgress;
	private String photo_path;
	private Bitmap scanBitmap;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private SurfaceView surfaceView;

	private RelativeLayout rlScanFailed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		AbViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));
		initView();
	}

	
	public void initView(){
		CameraManager.init(this);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		rlScanFailed = (RelativeLayout) findViewById(R.id.rlScanFailed);
		rlScanFailed.setOnClickListener(this);
		rlScanFailed.setVisibility(View.GONE);
		ibClose = (ImageButton) findViewById(R.id.ibClose);
		ibLignt=(ImageButton)findViewById(R.id.ibLignt);
		ibPhoto=(ImageButton)findViewById(R.id.ibPhoto);
		ibClose.setOnClickListener(this);
		ibLignt.setOnClickListener(this);
		ibPhoto.setOnClickListener(this);
		ibClose.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ibLignt:
				try {
					CameraManager.get().camera.cancelAutoFocus();
					parameters = CameraManager.get().camera.getParameters();
					if(parameters.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)){
						parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						ibLignt.setBackgroundResource(R.drawable.ic_light_close);

					}else{
						parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						ibLignt.setBackgroundResource(R.drawable.ic_light_open);
					}
					CameraManager.get().camera.setParameters(parameters);
					CameraManager.get().camera.autoFocus(CameraManager.get().autoFocusCallback);
				} catch (Exception e) {
					AbToastUtil.showToast(context, "设备故障");
				}
				break;
			case R.id.ibPhoto:
				//打开手机中的相册
				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
				innerIntent.setType("image/*");
				Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
				this.startActivityForResult(wrapperIntent, REQUEST_CODE);
				break;
			case R.id.ibClose:
				finish();
				break;
			case R.id.rlScanFailed:
				onResume();

				rlScanFailed.setVisibility(View.GONE);
				break;
		}

	}
	
	/** 以下是扫描 相关代码 */
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	public void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	
	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String scan_res = result.getText();
		if(!AbStrUtil.isEmpty(scan_res)){
			onResultHandler(scan_res,barcode);
		}else{
			rlScanFailed.setVisibility(View.VISIBLE);
		}
		onPause();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case REQUEST_CODE:
					AbLogUtil.d(context, "返回的地址：" + data.getData().toString());
					//获取选中图片的路径
					Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
					if (cursor != null && cursor.moveToFirst()) {
						photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();

						mProgress = new ProgressDialog(ScanActivity.this);
						mProgress.setMessage("正在扫描...");
						mProgress.setCancelable(false);
						mProgress.show();

						new Thread(new Runnable() {
							@Override
							public void run() {
								Result result = scanningImage(photo_path);
								if (result != null) {
									Message m = mHandler.obtainMessage();
									m.what = PARSE_BARCODE_SUC;
									m.obj = result.getText();
									mHandler.sendMessage(m);
								} else {
									Message m = mHandler.obtainMessage();
									m.what = PARSE_BARCODE_FAIL;
									m.obj = "扫描失败!";
									mHandler.sendMessage(m);
								}
							}
						}).start();
					}else{
						AbToastUtil.showToast(context,"无法访问图片地址！");
					}
					break;

			}
		}
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mProgress.dismiss();
			switch (msg.what) {
				case PARSE_BARCODE_SUC:
					onResultHandler((String) msg.obj, scanBitmap);
					break;
				case PARSE_BARCODE_FAIL:
					AbToastUtil.showToast(context, (String) msg.obj);
					break;

			}
		}

	};
	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到上一个页面
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap){
		if(TextUtils.isEmpty(resultString)){
			AbToastUtil.showToast(context,"Scan failed!");
			return;
		}
		AbLogUtil.d(context, "result:" + resultString);
		//进入扫描结果显示页面
		if(resultString.startsWith("http")){
			Intent intent = new Intent(context,WebActivity.class);
			intent.putExtra(WebActivity.INTENT_TAG_URL,resultString);
			startActivity(intent);
			finish();
		}else{
			ScanTextResultActivity_.intent(context).extra(ScanTextResultActivity_.INTENT_RESULT,resultString).start();
		}
	}
}