package com.iapps.libs.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleBean implements SimpleObjectInterface, Parcelable {

	private int id;
	private String name;

	public SimpleBean(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public SimpleBean(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasExtra() {
		return false;
	}

	@Override
	public String getExtraName() {
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		out.writeInt(id);
		out.writeString(name);
	}

	public static final Creator<SimpleBean> CREATOR = new Creator<SimpleBean>() {
		@Override
		public SimpleBean createFromParcel(final Parcel source) {
			return new SimpleBean(source);
		}

		@Override
		public SimpleBean[] newArray(final int size) {
			return new SimpleBean[size];
		}
	};

	@Override
	public String toString() {
		return name;
	}

}
