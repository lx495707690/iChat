package com.iapps.libs.views;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iapps.common_library.R;
import com.iapps.libs.helpers.BaseHelper;

public class ShowMoreCell extends LinearLayout {

	private ProgressBar progress;
	private TextView textview;
	private String noMore = "No More Result";
	private boolean isLoading;
	
	
	public ShowMoreCell(Context context) {
		this(context, null);
	}
	
	public ShowMoreCell(Context context, AttributeSet attrs){
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cell_showmore, this, true);
		
		progress = (ProgressBar) this.findViewById(R.id.progressBarShowMore);
		textview = (TextView) this.findViewById(R.id.textViewShowMore);
		
		try {
			noMore = context.getResources().getString(R.string.iapps__no_more);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void setLoading(){
		isLoading = true;
		BaseHelper.visibleView(progress);
		BaseHelper.goneView(textview);
	}
	
	public void stopLoading(){
		isLoading = false;
		BaseHelper.goneView(progress);
		BaseHelper.visibleView(textview);
	}
	
	public boolean isLoading(){
		return isLoading;
	}
	
	public void setNoMoreResult(){
		textview.setText(noMore);
		this.disable();
	}
	
	public void setNoMoreText(String no){
		this.noMore = no;
	}
	
	private void disable(){
		this.setEnabled(false);
		this.setOnClickListener(null);
	}
	
	public void enable(View.OnClickListener listener){
		this.textview.setText(R.string.iapps__show_more);
		this.setEnabled(true);
		this.setOnClickListener(listener);
	}
	


}
