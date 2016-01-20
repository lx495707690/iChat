package com.iapps.libs.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;

public abstract class DownloadByteAsync extends AsyncTask<String, Void, byte[]> {

	private URL url;

	public URL getUrl() {
		return url;
	}

	public void setUrl(String url) {

		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	protected abstract void onPostExecute(byte[] result);

	@Override
	protected byte[] doInBackground(String... urls) {
		return download(this.url);
	}

	/**
	 * @return
	 */
	public static byte[] download(URL url) {
		HttpURLConnection conn = null;
		InputStream in = null;
		byte[] response = null;
		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(BaseConstants.TIMEOUT);
			conn.setReadTimeout(BaseConstants.TIMEOUT);
			in = conn.getInputStream();

			if (in != null) {
				// read input from server
				response = IOUtils.toByteArray(in);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			response = null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}
		return response;
	}

}
