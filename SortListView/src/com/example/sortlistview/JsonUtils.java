package com.example.sortlistview;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;

/**
 * 
 * @项目名称：	Make_wish
 * @类名称： 	JsonUtils.java
 * @创建人：	Mr.ladeng (zbl704@yeah.net)
 * @创建时间： 2015-5-15下午7:14:35
 * @修改备注：    
 * @version 1.0   
 * @类描述：	工具类
 * md5加密
 * http GET 请求
 * http post 请求
 * https GET 请求
 * http POST 上传文字图片
 * 
 */
public class JsonUtils {
	
	public static String TAG = "JsonUtils";
	
	Context context = null;

	
	
	public static JSONObject ReadHttpGet(String path) {
		path=path.replace(" ", "");
		HttpGet httpRequest = new HttpGet(path);
		HttpParams connParam = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(connParam, 50 * 1000);
		HttpConnectionParams.setSoTimeout(connParam, 50 * 1000);
		HttpClient httpClient = new DefaultHttpClient(connParam);
		JSONObject jsonObject = null;
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
				jsonObject = new JSONObject(strResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return jsonObject;
	}

	}
