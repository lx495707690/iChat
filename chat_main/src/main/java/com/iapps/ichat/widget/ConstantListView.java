package com.iapps.ichat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ConstantListView extends ListView {

	public ConstantListView(Context context) {
		this(context, null);
	}

	public ConstantListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ConstantListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}