package com.iapps.libs.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtraBean extends SimpleBean implements Parcelable {

	private String extra;
	private int extraId;
	
	public ExtraBean(int id, String name, String extra) {
		super(id, name);
		this.extra = extra;
	}
	
	public ExtraBean(Parcel source) {
		super(source);
		this.extra = source.readString();
		this.extraId = source.readInt();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean hasExtra() {
		return extra == null? false: true;
	}

	@Override
	public String getExtraName() {
		return extra;
	}

	public int getExtraId() {
		return extraId;
	}

	public void setExtraId(int extraId) {
		this.extraId = extraId;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		super.writeToParcel(out, flag);
		out.writeString(extra);
		out.writeInt(extraId);
	}

	public static final Creator<ExtraBean> CREATOR = new Creator<ExtraBean>() {
        @Override
        public ExtraBean createFromParcel(final Parcel source) {
            return new ExtraBean(source);
        }

        @Override
        public ExtraBean[] newArray(final int size) {
            return new ExtraBean[size];
        }
    };

}
