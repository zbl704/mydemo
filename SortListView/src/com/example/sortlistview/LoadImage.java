package com.example.sortlistview;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class LoadImage {

	public static void getImage(ImageView imageView, String picPath) {
		ImageListener listener = ImageLoader.getImageListener(imageView, android.R.drawable.ic_menu_rotate,android.R.drawable.ic_delete);
		try {
			App.getInstance().imageLoader.get(picPath, listener);
		} catch (Exception e) {
		}
	}

	public static void getImage(final ImageView imageView, String picPath,final int width, final int height) {

		App.getInstance().imageLoader.get(picPath, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				imageView.setImageResource(android.R.drawable.ic_delete);
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {

				Bitmap bitmap = response.getBitmap();
				if (bitmap != null) {
					bitmap = Bitmap.createScaledBitmap(bitmap, width, height,true);
					imageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageResource(android.R.drawable.ic_menu_rotate);
				}

			}
		});
	}

	public static void saveBitmap(final String picPath, final Handler handler) {

		App.getInstance().imageLoader.get(picPath, new ImageListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handler.sendEmptyMessage(0);
			}
			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				Bitmap bitmap = response.getBitmap();
				int message = 0;
				if (bitmap != null) {
					File f = new File(App.getInstance().getDownFileStorePath("/image/"), picPath.substring(picPath.lastIndexOf("/")+1));
					if (f.exists()) {
						f.delete();
					}
					try {
						FileOutputStream out = new FileOutputStream(f);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						out.flush();
						out.close();
						message = 1;
					} catch (Exception e) {
						
					}
				}
				handler.sendEmptyMessage(message);
			}
		});
	}

}
