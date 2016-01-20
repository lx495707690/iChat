package com.iapps.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iapps.common_library.R;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseKeys;
import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.objects.SimpleObjectInterface;

public class SimpleObjectAdapter<T extends SimpleObjectInterface>
	extends ArrayAdapter<T> {

	int textResColor, textExtraResColor;
	boolean isForSpinner;

	/**
	 * Constructor for simple object adapter
	 * 
	 * @param context
	 *            , the current context
	 * @param objects
	 *            , arraylist containing objects implementing
	 *            SimpleObjectInterface
	 */
	public SimpleObjectAdapter(Context context, List<T> objects) {
		super(context, R.layout.cell_single, objects);
	}

	/**
	 * Static method to create simple object adapter from a jsonArray, with the
	 * default key "id" for id and default key "name" for the name of the object
	 * 
	 * @param context
	 *            , the current context
	 * @param arrays
	 *            , arraylist containing objects implementing
	 *            SimpleObjectInterface
	 * @return new SimpleObjectAdapter
	 */
	public static SimpleObjectAdapter<SimpleBean> createFromJSONArray(
			Context context, JSONArray arrays) {
		return createFromJSONArray(context, arrays, BaseKeys.ID, BaseKeys.NAME);
	}

	/**
	 * Static method to create simple object adapter from a jsonArray, by
	 * supplying the key for the id and name of the object.
	 * 
	 * @param context
	 *            , the current context
	 * @param arrays
	 *            , arraylist containing objects implementing
	 *            SimpleObjectInterface
	 * @param idKey
	 *            , the key for the id
	 * @param nameKey
	 *            , the key for the name
	 * @return
	 */
	public static SimpleObjectAdapter<SimpleBean> createFromJSONArray(
			Context context, JSONArray arrays, String idKey, String nameKey) {
		ArrayList<SimpleBean> list = new ArrayList<SimpleBean>();
		for (int i = 0; i < arrays.length(); i++) {
			JSONObject object = arrays.optJSONObject(i);
			if (object != null) {
				int id = object.optInt(idKey, 0);
				String name = object.optString(nameKey);
				SimpleBean bean = new SimpleBean(id, name);
				list.add(bean);
			}
		}

		return new SimpleObjectAdapter<SimpleBean>(context, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater mInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (isForSpinner)
				convertView = mInflater.inflate(R.layout.cell_single_spinner,
						null);
			else
				convertView = mInflater.inflate(R.layout.cell_single, null);

			holder.value = (TextView) convertView.findViewById(R.id.text1);
			holder.extra = (TextView) convertView
					.findViewById(R.id.textViewCellExtra);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		T object = this.getItem(position);

		holder.value.setText(object.getName());

		if (object.hasExtra()) {
			BaseHelper.visibleView(holder.extra);
			holder.extra.setText(object.getExtraName());
		} else {
			BaseHelper.goneView(holder.extra);
		}

		if (textResColor != 0) {
			holder.value.setTextColor(textResColor);
		}

		if (textExtraResColor != 0) {
			holder.extra.setTextColor(textExtraResColor);
		}

		return convertView;
	}

	protected class ViewHolder {

		TextView value;
		TextView extra;
	}

	public void setTextColor(int res) {
		this.textResColor = res;
	}

	public void setTextExtraColor(int res) {
		this.textExtraResColor = res;
	}

	public void setForSpinner(boolean isForSpinner) {
		this.isForSpinner = isForSpinner;
	}
}
