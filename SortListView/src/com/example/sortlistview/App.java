package com.example.sortlistview;

import java.io.File;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class App extends Application {

	public static final String TAG = "App";


	public boolean isLogining = false;
	public boolean isNopic = false;
	public boolean isPush = true;

	public boolean m_bKeyRight = true;

	public static String USER_UUID = "";// 判断用户是否登陆
	public static String USER_TOKEN = "";// 判断用户是否在其他机器上登录
	public static String USER_PHONE = "";// 用户手机�?
	public static String USER_NAME = "";// 用户昵称
	public static String CITYNUMBER = "";

	public static String MAKEYID = "";// 愿望人id
	public static String TYPE = ""; // 举报类型

	public static String UNWISHPAGE = "1"; // 许愿未完�?
	public static String UNWISHMARK = "n"; // 许愿 -- 是否已领�? y已领�? n 未领�?

	public ImageLoader imageLoader;

	public static App application;



	public static String testUid = "1";
//	public static String testToken = "d19832fdf83ecb17dbd6c81f10d6520f";//104
	public static String testToken = "635f325bfae1ca049fcdad258cf50178";//114
//	public static String testUid = "3";
//	public static String testToken = "eff97088a360c961140917107fd14928";

	public static App getInstance() {

		return application;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		application = this;

		imageLoader = new ImageLoader(Volley.newRequestQueue(this),
				new MyImageCache());


	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public String getDownFileStorePath(String fileName) // 传�?�一�?"/image/"
	{
		File sdDir = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		if (hasSDCard) {
			sdDir = Environment.getExternalStorageDirectory();
		} else {
			return "";
		}
		// String dirPath = sdDir+"/Android/data/" +
		// context.getPackageName()+"/video";
		File destDir = new File(sdDir.toString() + "/Android/data/"
				+ this.getPackageName() + fileName);// "/cardImages"
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return destDir.toString();
	}

	


}
