package com.example.mybrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @项目名称：	Make_wish
 * @类名称： 	ActivityWebView.java
 * @创建人：	Mr.ladeng (zbl704@yeah.net)
 * @创建时间： 2015-6-5上午10:10:29
 * @修改备注：    
 * @version 1.0   
 * @类描述：	webView 页面（加载web）
 */
@SuppressLint("SetJavaScriptEnabled")
public class ActivityWebView extends Activity implements OnClickListener {

	private WebView webView;

	private WebSettings webSettings = null;
	boolean blockLoadingNetworkImage = true;

	private ProgressBar pb;
	private ImageView title_back;
	private TextView web_title_t ;
	
	private String path = "http://www.hao123.com" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview_);

		webView = (WebView) findViewById(R.id.webView_);
		web_title_t = (TextView) findViewById(R.id.web_title_t);
		title_back = (ImageView) findViewById(R.id.title_back_wb);
		title_back.setOnClickListener(this);
		((TextView) findViewById(R.id.web_close)).setOnClickListener(this);

		pb = (ProgressBar) findViewById(R.id.pb_wb);
		pb.setMax(100);

		initView();

	}

	@SuppressWarnings("deprecation")
	private void initView() {

		webSettings = webView.getSettings();// 网页设置默认属性

		webView.setInitialScale(39);// 适应竖屏
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 去掉底部和右边的滚动条

		// webView.getSettings().setUseWideViewPort(true);// 是否任意比例缩放
		webView.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小

		webSettings.setJavaScriptEnabled(true);// 设置是否支持Javascript
		webSettings.setAllowFileAccess(true);// 启用或禁止WebView访问文件数据

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);// 设置布局方式
		// webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//
		// 设置缓冲的模式
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存，只从网络获取数据

		webSettings.setDefaultZoom(ZoomDensity.MEDIUM);// 设置适应屏幕模式
		webSettings.setBuiltInZoomControls(false); // 设置是否支持缩放

		// webSettings.setSupportZoom(false);// 设置是否支持变焦,仅支持双击缩放
		webSettings.setNeedInitialFocus(false);// 设置是否可以访问文件

		webView.getSettings().setJavaScriptEnabled(true);// 给js暴露接口
		webView.getSettings().setDomStorageEnabled(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		webSettings.setBlockNetworkImage(true);// 是否显示网络图像
		blockLoadingNetworkImage = true;

		webView.setWebViewClient(new MyWebViewClient());// 处理各种事件
		webView.setWebChromeClient(new MyWebChromeClient());

		webView.getSettings().setDomStorageEnabled(true);

		webView.loadUrl(path);

	}

	/** * 处理各种通知、请求事件,当前界面打开网络连接 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			pb.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// 页面下载完毕,却不代表页面渲染完毕显示出来
			// WebChromeClient中progress==100时也是一样
			if (webView.getContentHeight() != 0) {
				// 这个时候网页才显示
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/** * 处理Javascript的对话框、网站图标、网站title、加载进度等 */
	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int progress) {
			if (progress == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			pb.setProgress(progress);
			pb.postInvalidate();

			if (progress == 100) {
				pb.setVisibility(View.GONE);
				if (blockLoadingNetworkImage) {
					webSettings.setBlockNetworkImage(false);
					blockLoadingNetworkImage = false;
				}
			}
		}

		/**
		 * 获取网页title
		 */
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			Log.d("ANDROID_LAB", "TITLE=" + title);
			web_title_t.setText(title);
		}
	}

	/******* 回退 **********/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_wb:
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			
			break;

		case R.id.web_close :
			finish();
			break;
		}
	}
}
