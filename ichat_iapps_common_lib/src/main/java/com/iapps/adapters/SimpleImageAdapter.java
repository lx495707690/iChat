package com.iapps.adapters;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iapps.common_library.R;
import com.iapps.libs.helpers.BaseKeys;

public class SimpleImageAdapter extends ArrayAdapter<LinkedHashMap<String,Object>> {
	
	private Context context;
	private int textViewResId;
	private int mDrawablePadding;
	
	/**
	 * Create new adapter which can handle object of Map<String,Object>
	 * to Set the text, Use {@link BaseKeys.VALUE} for {@link String} Object
	 * to Set the image, Use {@link BaseKeys.PHOTO} for {@link Drawable} Object
	 * @param context
	 * @param textViewResourceId, must contain {@link TextView} with R.id.text1
	 * @param objects
	 */
	public SimpleImageAdapter(Context context, int textViewResourceId,
			ArrayList<LinkedHashMap<String, Object>> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.textViewResId = textViewResourceId;
	}
	
	protected class ViewHolder{
		TextView value;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = new ViewHolder();
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(textViewResId, null);
			
			holder.value = (TextView) convertView.findViewById(R.id.text1);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		LinkedHashMap<String,Object> nameValuePair = this.getItem(position);
		
		this.drawOn(holder, nameValuePair, position);
		
		return convertView;
	}
	
	protected void drawOn(ViewHolder holder, LinkedHashMap<String,Object> nameValuePair, int position){
		if(nameValuePair != null){
			if(nameValuePair.get(BaseKeys.VALUE) != null){
				holder.value.setText(nameValuePair.get(BaseKeys.VALUE).toString());
			}
			if(nameValuePair.get(BaseKeys.PHOTO) != null){
				if(nameValuePair.get(BaseKeys.PHOTO) instanceof Drawable){
					this.drawLeft(holder, (Drawable) nameValuePair.get(BaseKeys.PHOTO));					
				}
			}
		}
	}
	
	protected void drawLeft(ViewHolder holder, Drawable d){
		holder.value.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		holder.value.setCompoundDrawablePadding(mDrawablePadding);
	}

	public int getDrawablePadding() {
		return mDrawablePadding;
	}

	public void setDrawablePadding(int mDrawablePadding) {
		this.mDrawablePadding = mDrawablePadding;
	}

}
