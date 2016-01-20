package com.iapps.libs.helpers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.TypedValue;

import com.iapps.libs.objects.ExtraBean;
import com.iapps.libs.objects.SimpleBean;

public class BaseConverter {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static long convertBitmapToBytes(Bitmap bitmap) {
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	        return bitmap.getByteCount();
	    } else {
	        return bitmap.getRowBytes() * bitmap.getHeight();
	    }
	}
	
	public static int convertToDp(Context context, int dp){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
		return (int) px;
	}
	
	public static ArrayList<SimpleBean> convertToSimpleObjects(JSONArray arrays){
		ArrayList<SimpleBean> res = new ArrayList<SimpleBean>();
		for(int i = 0; i< arrays.length(); i++){
			JSONObject ob = arrays.optJSONObject(i);
			if(ob != null){
				SimpleBean b = convertToSimpleObject(ob);
				if(b != null){
					res.add(b);
				}
			}
		}
		return res;
	}
	
	public static SimpleBean convertToSimpleObject(JSONObject json, String idKey, String nameKey){
		SimpleBean o = null;
		try {
			int id = json.getInt(idKey);
			String name = json.getString(nameKey);
			
			o = new SimpleBean(id, name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	public static SimpleBean convertToSimpleObject(JSONObject json, String idKey, String[] nameKeys){
		SimpleBean o = null;
		try {
			int id = json.getInt(idKey);
			String name ="";
			
			for(String key : nameKeys){
				if(!json.isNull(key)){
					if(nameKeys[nameKeys.length -1].equals(key)){
						name += json.optString(key);
					}else{
						name += json.optString(key) +" - ";
					}
				}
			}
			
			o = new SimpleBean(id, name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	public static ExtraBean convertToExtraObject(JSONObject json, String extraKey, String extraIdKey){
		return convertToExtraObject(json, BaseKeys.ID, BaseKeys.NAME, extraKey, extraIdKey);
	}
	
	public static ExtraBean convertToExtraObject(JSONObject json, String extraKey){
		return convertToExtraObject(json, BaseKeys.ID, BaseKeys.NAME, extraKey, null);
	}
	
	public static ExtraBean convertToExtraObject(JSONObject json, String idKey, String nameKey, String extraKey, String extraIdKey){
		ExtraBean o = null;
		try {
			int id = json.getInt(idKey);
			String name = json.getString(nameKey);
			String extra = null;
			if(extraKey != null && !json.isNull(extraKey)){
				extra = json.getString(extraKey);
			}
			
			
			o = new ExtraBean(id, name, extra);
			
			if(extraIdKey != null && !json.isNull(extraIdKey)){
				int xtraId = json.optInt(extraIdKey);
				o.setExtraId(xtraId);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	public static SimpleBean convertToSimpleObject(JSONObject json){
		return convertToSimpleObject(json, BaseKeys.ID, BaseKeys.NAME);
	}
	
}
