package com.iapps.ichat.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;

public class FontUtil {

	public static final String DEFAULT_TYPEFACE	= "fonts/BebasNeue.otf";
	public static final String ARIAL_BOLD = "fonts/BebasNeue.otf";
	
	private Context context;
	private HashMap<String, Typeface>fonts;
	
	public FontUtil(Context context) {
		this.context = context;
		fonts = new HashMap<String, Typeface>();
		
	}
	
	public void setFontRegularType(TextView txtView) {
		setFontType(txtView, DEFAULT_TYPEFACE);
	}
	
	public void setFontBookType(TextView txtView) {
		setFontType(txtView, ARIAL_BOLD);
	}
	
	public void setFontBoldType(TextView txtView) {
		setFontType(txtView, ARIAL_BOLD);
	}
	
	public void setFontType(TextView txtView, String fontType) {
		Typeface typeFace = fonts.get(fontType);
		if( typeFace == null ) {
			try {
				typeFace = Typeface.createFromAsset(context.getAssets(), fontType);
				if( typeFace != null ) {
					txtView.setTypeface(typeFace);
					fonts.put(fontType, typeFace);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
