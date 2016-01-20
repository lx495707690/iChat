package com.iapps.libs.helpers;

import com.iapps.libs.objects.SimpleObjectInterface;

public interface CheckableObjectInterface extends SimpleObjectInterface{
	public void setChecked(boolean isChecked);
	public boolean isChecked();
	
}
