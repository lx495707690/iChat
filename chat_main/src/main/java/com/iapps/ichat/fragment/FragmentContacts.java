package com.iapps.ichat.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapps.ichat.R;
import com.iapps.ichat.adapter.SortContactsAdapter;
import com.iapps.ichat.helper.ContactsSideBar;
import com.iapps.ichat.helper.GenericFragmentiChat;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.model.BeanContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roboguice.inject.InjectView;

public class FragmentContacts extends GenericFragmentiChat {
	@InjectView(R.id.sbContacts)
	private ContactsSideBar sbContacts;
	@InjectView(R.id.tvDialog)
	private TextView tvDialog;
	@InjectView(R.id.lvContacts)
	private ListView lvContacts;
	@InjectView(R.id.title_layout)
	private LinearLayout titleLayout;
	@InjectView(R.id.title_layout_catalog)
	private TextView title;

//	@InjectView(R.id.LLLoading) private LinearLayout LLLoading;

	private SortContactsAdapter mContactAdapter;

	private int lastFirstVisibleItem = -1;
	private List<BeanContact> mSourceDataList = new ArrayList<>();



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_contacts, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();

	}

	private void initView(){
		sbContacts.setTextView(tvDialog);
		sbContacts.setOnTouchingLetterChangedListener(new ContactsSideBar.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {

				int position = mContactAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lvContacts.setSelection(position);
				}
			}
		});

		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				Toast.makeText(
						getActivity(),
						((BeanContact) mContactAdapter.getItem(position)).getPhoneNumbers() + "",
						Toast.LENGTH_SHORT).show();
			}
		});

		loadContacts();
		lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {

				if (mSourceDataList.size() > 0) {
					int section = getSectionForPosition(firstVisibleItem);
					int nextSection = getSectionForPosition(firstVisibleItem + 1);
					int nextSecPosition = getPositionForSection(+nextSection);
					if (firstVisibleItem != lastFirstVisibleItem) {
						ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
								.getLayoutParams();
						params.topMargin = 0;
						titleLayout.setLayoutParams(params);
						title.setText(mSourceDataList.get(
								getPositionForSection(section)).getSortLetters());
					}
					if (nextSecPosition == firstVisibleItem + 1) {
						View childView = view.getChildAt(0);
						if (childView != null) {
							int titleHeight = titleLayout.getHeight();
							int bottom = childView.getBottom();
							ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
									.getLayoutParams();
							if (bottom < titleHeight) {
								float pushedDistance = bottom - titleHeight;
								params.topMargin = (int) pushedDistance;
								titleLayout.setLayoutParams(params);
							} else {
								if (params.topMargin != 0) {
									params.topMargin = 0;
									titleLayout.setLayoutParams(params);
								}
							}
						}
					}
					lastFirstVisibleItem = firstVisibleItem;
				}
			}
		});
	}

	private void filledData() {

		for (int i = 0; i < mSourceDataList.size(); i++) {
			String pinyin = Helper.converterToPinYin(mSourceDataList.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				mSourceDataList.get(i).setSortLetters(sortString.toUpperCase());
			} else {
				mSourceDataList.get(i).setSortLetters("#");
			}
		}
		Collections.sort(mSourceDataList);
	}

	public int getSectionForPosition(int position) {
		return mSourceDataList.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < mSourceDataList.size(); i++) {
			String sortStr = mSourceDataList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public void loadContacts(){

		LoadContacts lc = new LoadContacts();
		lc.execute();

	}

	private class LoadContacts extends AsyncTask<String,Void, ArrayList<BeanContact>> {

		@Override
		protected ArrayList<BeanContact> doInBackground(String... params) {

			ArrayList<BeanContact> contacts = new ArrayList<BeanContact>();
			if(getActivity() == null){
				return contacts;
			}
			ContentResolver cr = getActivity().getContentResolver();

			String[] projection = new String[]{
					ContactsContract.Contacts._ID,
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.HAS_PHONE_NUMBER
			};

			String orderBy = ContactsContract.Contacts.DISPLAY_NAME +" ASC";

			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
					projection, null, null, orderBy);

			while(cur.moveToNext()){
				BeanContact c = null;

				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(
						cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				if(!Helper.isEmpty(name)){
					c = new BeanContact(id, name);
				}
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					// Query phone here.
					String[]projectionPhone = new String[]{
							ContactsContract.CommonDataKinds.Phone.NUMBER
					};
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							projectionPhone,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
							new String[]{id}, null);
					while (pCur.moveToNext()) {
						// Do something with phones
						String phoneNumber = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						if(phoneNumber.length() > 0 && c != null){
							c.addPhoneNumber(phoneNumber);
						}
					}
					pCur.close();
				}

				if(c != null){
					contacts.add(c);
				}
			}
			cur.close();
			return contacts;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			LLLoading.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPostExecute(ArrayList<BeanContact> contacts){
//			LLLoading.setVisibility(View.GONE);
			if(contacts != null){
				mSourceDataList = contacts;
				filledData();
				mContactAdapter = new SortContactsAdapter(getActivity(), mSourceDataList);
				lvContacts.setAdapter(mContactAdapter);
			}
		}
	}
}
