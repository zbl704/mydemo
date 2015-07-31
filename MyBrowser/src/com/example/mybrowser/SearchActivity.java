package com.example.mybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @项目名称： Mr_ladeng_wish
 * @类名称： SearchActivity.java
 * @创建人： Mr.ladeng (zbl704@yeah.net)
 * @创建时间： 2015-4-14下午7:33:30
 * @修改备注：
 * @version 1.0
 * @类描述： 搜索类
 */
public class SearchActivity extends Activity {

	private WebView webView;
	/** 页面标题 **/
	private TextView tv_title, tv_right_search;
	private EditText et_search;

	private WebSettings webSettings = null;
	boolean blockLoadingNetworkImage = true;
	private ProgressBar pb;
	private ImageView title_back_search;

	private String searchString;// editText 输入的内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		initView();

		initWeb();

	}

	private void initView() {

		tv_title = (TextView) findViewById(R.id.tv_search);
		tv_right_search = (TextView) findViewById(R.id.tv_right_search);
		tv_right_search.setOnClickListener(listener);

		webView = (WebView) findViewById(R.id.webView_search);
		title_back_search = (ImageView) findViewById(R.id.title_back_search);
		title_back_search.setOnClickListener(listener);

		pb = (ProgressBar) findViewById(R.id.pb_search);
		pb.setMax(100);
		et_search = (EditText) findViewById(R.id.et_search);

	}

	private void initWeb() {

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

	}

	/** * 处理各种通知、请求事件,当前界面打开网络连接 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			pb.setVisibility(android.view.View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
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
//			Intent intent = new Intent(SearchActivity.this,
//					WebViewLoadActivity.class);
//			intent.putExtra("url", url);
//			startActivity(intent);
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
			tv_title.setText(title);
		}
	}

	/******* 回退 **********/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
	protected void onResume() {
		super.onResume();
		et_search.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.title_back_search:

				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}

				break;

			case R.id.tv_right_search:
				
				//强制隐藏键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

				et_search.setVisibility(View.GONE);
				searchString = et_search.getText().toString().trim();

				final Uri uri = Uri.parse("http://www.baidu.com/s?wd=" + searchString);

				String uriString = uri.toString().trim();

				webView.loadUrl(uriString);

				// final Intent it = new Intent(Intent.ACTION_VIEW, uri);
				//
				// Timer timer = new Timer();
				// TimerTask task = new TimerTask() {
				// @Override
				// public void run() {
				// startActivity(it); //执行
				// finish();
				// }
				// };
				// timer.schedule(task, 1); //10秒后
				break;
			}
		}
	};


}
