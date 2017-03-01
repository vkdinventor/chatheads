package com.example.chatheads;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class ChatHeadService extends Service {

	private WindowManager windowManager;
//	private ImageView chatHead;
	private View chatHead;
	WindowManager.LayoutParams params;
	private static final String FRAME_VIDEO = "<html><body>Video From YouTube<br><iframe width=\"320\" height=\"280\" src=\"https://www.youtube.com/embed/_oEA18Y8gM0\" frameborder=\"0\"></iframe></body></html>";


	@Override
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main,null);
		WebView webView =(WebView) chatHead.findViewById(R.id.webview);
		Button back = (Button)chatHead.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				stopSelf();
			}
		});

		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadData(FRAME_VIDEO, "text/html", "utf-8");


		params= new WindowManager.LayoutParams(
				300,300,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = -10;
		
		//this code is for dragging the chat head
		chatHead.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(chatHead, params);
					return true;
				}
				return false;
			}
		});
		windowManager.addView(chatHead, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null)
			windowManager.removeView(chatHead);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}



}
