package com.iapps.libs.objects;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.iapps.libs.helpers.BaseConstants;
import com.iapps.libs.helpers.BaseKeys;

public class Response {

	private int statusCode = 400;
	private JSONObject content;
	private Map<String, List<String>> headerContent;

	public static final Response getDummyResponse() {
		return new Response(200, "");
	}

	public Response(int statusCode, String content) {
		this.statusCode = statusCode;
		try {
			this.content = new JSONObject(new JSONTokener(content));
		} catch (JSONException e) {

			e.printStackTrace();

			try {
				JSONArray jAr = new JSONArray(new JSONTokener(content));
				this.content = new JSONObject();
				this.content.put(BaseKeys.RESULTS, jAr);
			} catch (JSONException ex) {
				this.content = new JSONObject();
				ex.printStackTrace();

				Log.e("Response", content);
			}
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public JSONObject getContent() {
		return content;
	}

	public DateTime getHeaderTime() {
		if (headerContent == null)
			return DateTime.now();

		try {
			for (Map.Entry<String, List<String>> k : headerContent.entrySet()) {
				if (k.getKey() != null && k.getKey().equals(BaseKeys.DATE_))
					for (String v : k.getValue()) {
						Log.d("ASD", "SUCCESS");
						v = v.substring(0, v.trim().lastIndexOf(" "));
						return DateTime.parse(v,
								DateTimeFormat.forPattern(BaseConstants.DATE_EDMYHMS));
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return DateTime.now();
	}

	public void setHeaderContent(Map<String, List<String>> headerContent) {
		this.headerContent = headerContent;
	}

}
