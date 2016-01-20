package com.iapps.libs.helpers;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class HTTPImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

	private static final int IMAGE_MAX_SIZE = 700;
	private ImageView mImageView;
	private BitmapReceivedListener mListener = null;
	private MemCacheListener mCacheListener = null;

	private int mPos;
	private ProgressBar pbar;
	private String urldisplay;
	private boolean isThumbnail;

	protected void onProgressUpdate(Integer... progress) {
		// add spinner to imageview
	}

	public void setProgressBarToHide(ProgressBar pbar) {
		this.pbar = pbar;
	}

	public void setBitmapReceivedListener(BitmapReceivedListener adapter) {
		this.mListener = adapter;
	}

	public HTTPImageAsyncTask(ImageView mImageView) {
		this.mImageView = mImageView;
	}

	public HTTPImageAsyncTask(int position) {
		this.mPos = position;
	}

	public void setmCacheListener(MemCacheListener mCacheListener) {
		this.mCacheListener = mCacheListener;
	}

	public void setIsThumbnail(boolean isThumbnail) {
		this.isThumbnail = isThumbnail;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		if (urldisplay == null) {
			urldisplay = params[0];
		}
		if (urldisplay == null) {
			return null;
		}

		Bitmap bmp = null;
		int retry = 0;
		do {
			bmp = download();
			retry++;
		} while (bmp == null && retry <= 3);

		if (bmp == null) {
			byte[] b = downloadByte();
			if (b != null) {
				bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
			}
		}

		return bmp;
	}

	protected Bitmap download() {
		Bitmap bitmap = null;
		InputStream in = null;

		URL m;

		try {
			if (!isThumbnail) {
				m = new URL(urldisplay);
				if (m.openConnection() == null
						&& m.openConnection().getInputStream() == null) {
					return null;
				}

				bitmap = decode(m);

				// in = (InputStream) m.getContent();
				//
				// // bis = new BufferedInputStream(in,1024 * 8);
				// // out = new ByteArrayOutputStream();
				// // int len=0;
				// // byte[] buffer = new byte[1024];
				// // while((len = bis.read(buffer)) != -1){
				// // out.write(buffer, 0, len);
				// // }
				// // out.close();
				// // bis.close();
				// //
				// // byte[] data = out.toByteArray();
				// // bitmap = BitmapFactory.decodeByteArray(data, 0,
				// data.length);
				// bitmap = BitmapFactory.decodeStream(new
				// BufferedInputStream(in));
				// in.close();
			} else {
				// get the thumbnail
				m = new URL(urldisplay);
				in = (InputStream) m.getContent();

				BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
				onlyBoundsOptions.inJustDecodeBounds = true;
				onlyBoundsOptions.inDither = true;// optional
				onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

				BitmapFactory.decodeStream(in, null, onlyBoundsOptions);
				in.close();

				if ((onlyBoundsOptions.outWidth == -1)
						|| (onlyBoundsOptions.outHeight == -1))
					return null;

				int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
						: onlyBoundsOptions.outWidth;

				double ratio = (originalSize > BaseConstants.THUMBNAIL_SIZE) ? (originalSize / BaseConstants.THUMBNAIL_SIZE)
						: 1.0;

				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = BaseUIHelper
						.getPowerOfTwoForSampleRatio(ratio);
				bitmapOptions.inDither = true;// optional
				bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

				in = (InputStream) m.getContent();

				bitmap = BitmapFactory.decodeStream(
						new BufferedInputStream(in), null, bitmapOptions);

				in.close();

			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e2) {
			e2.printStackTrace();
		}

		return bitmap;
	}

	private byte[] downloadByte() {
		byte[] output = null;
		InputStream in = null;
		URL m;
		HttpURLConnection conn = null;
		try {
			m = new URL(urldisplay);
			conn = (HttpURLConnection) m.openConnection();
			conn.setConnectTimeout(BaseConstants.TIMEOUT);
			conn.setReadTimeout(BaseConstants.TIMEOUT);
			in = conn.getInputStream();

			if (in != null) {
				output = IOUtils.toByteArray(in);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return output;
	}

	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			if (this.mImageView != null) {
				this.mImageView.setImageBitmap(result);
				this.mImageView.setVisibility(View.VISIBLE);
				ViewParent parent = this.mImageView.getParent();
				((ViewGroup) parent).setVisibility(View.VISIBLE);
			}

			if (this.mListener != null) {
				mListener.onBitmapUpdate(mPos, result);
			}

			if (this.mCacheListener != null) {
				Log.d("image cache", "add to cache");
				mCacheListener.addBitmapToMemoryCache(urldisplay, result);
			}
		}

		BaseHelper.goneView(pbar);

	};

	public interface BitmapReceivedListener {
		public void onBitmapUpdate(int pos, Bitmap bitmap);
	}

	public interface MemCacheListener {
		public void addBitmapToMemoryCache(String key, Bitmap bitmap);
	}

	// public static Bitmap loadImageFromUrl(String url) {
	// URL m;
	// InputStream in = null;
	// BufferedInputStream bis = null;
	// ByteArrayOutputStream out =null;
	// try {
	// m = new URL(url);
	// in = (InputStream) m.getContent();
	// bis = new BufferedInputStream(in,1024 * 8);
	// out = new ByteArrayOutputStream();
	// int len=0;
	// byte[] buffer = new byte[1024];
	// while((len = bis.read(buffer)) != -1){
	// out.write(buffer, 0, len);
	// }
	// out.close();
	// bis.close();
	// } catch (MalformedURLException e1) {
	// e1.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// byte[] data = out.toByteArray();
	// Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	// //Drawable d = Drawable.createFromStream(i, "src");
	// return bitmap;
	// }

	private Bitmap decode(URL url) {
		Bitmap b = null;
		try {
			InputStream in = (InputStream) url.getContent();

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new BufferedInputStream(in), null, o);
			in.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			in = (InputStream) url.getContent();
			b = BitmapFactory.decodeStream(new FlushedInputStream(
					new BufferedInputStream(new PatchInputStream(in))), null,
					o2);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;

	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public class PatchInputStream extends FilterInputStream {
		public PatchInputStream(InputStream in) {
			super(in);
		}

		public long skip(long n) throws IOException {
			long m = 0L;
			while (m < n) {
				long _m = in.skip(n - m);
				if (_m == 0L)
					break;
				m += _m;
			}
			return m;
		}
	}
}
