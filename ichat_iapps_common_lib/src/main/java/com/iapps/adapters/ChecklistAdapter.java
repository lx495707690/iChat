package com.iapps.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.iapps.common_library.R;
import com.iapps.libs.helpers.CheckableObjectInterface;

public class ChecklistAdapter<T extends CheckableObjectInterface> extends ArrayAdapter<T> {

	public ChecklistAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<T> objects){
		super(context, resource, textViewResourceId, objects);
	}
	
	private class ViewHolder {
		CheckBox name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.custom_checkbox_layout, null);

			holder = new ViewHolder();
			holder.name = (CheckBox) convertView.findViewById(R.id.checkBoxList);
			holder.name.setTag(position);
			convertView.setTag(holder);
			
			holder.name.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v ;  
					CheckableObjectInterface cObject = (CheckableObjectInterface) cb.getTag();
				    cObject.setChecked(cb.isChecked());
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CheckableObjectInterface cObject = this.getItem(position);
		

		if (cObject != null) {
			String interest = cObject.getName();
			boolean isChecked = cObject.isChecked();
			holder.name.setText(interest);
			holder.name.setChecked(isChecked);
			holder.name.setTag(cObject);
		}
		
		return convertView;
	} 
}
