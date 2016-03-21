package com.iapps.ichat.helper;

import com.iapps.libs.helpers.BaseConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.itangqi.greendao.DBMessage;

public class Converter
	extends BaseConverter {

	public static DBMessage toTxtMessage(String data,String my_userId,boolean fromMe,boolean isSended) {
        DBMessage m = null;
		try {
			JSONObject j = new JSONObject(data);
            String message = "";
            String fromId = "";
			String toId = "";
            String channelId = "";
			String fromName = "";
			String channalName = "";

            if (!j.isNull(Keys.DATA))
				message = j.getString(Keys.DATA);
			if (!j.isNull(Keys.FROM))
				fromId = j.getString(Keys.FROM);
			if (!j.isNull(Keys.TO))
				toId = j.getString(Keys.TO);
			if (!j.isNull(Keys.CHANNAL))
                channelId = j.getString(Keys.CHANNAL);
			if (!j.isNull(Keys.FROM_NAME))
				fromName = j.getString(Keys.FROM_NAME);
			if (!j.isNull(Keys.CHANNAL_NAME))
				channalName = j.getString(Keys.CHANNAL_NAME);

			Date date = new Date();
			SimpleDateFormat sdf =   new SimpleDateFormat( Constants.DATE_TIME_JSON );
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			sdf.format(date);

			m = new DBMessage(null,my_userId, message,date.toString(), fromId, toId,channelId,"",fromName, channalName,fromMe,isSended);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	public static List<DBMessage > toListTxtMessage(String data,String my_userId,boolean fromMe,boolean isSended) {
		List<DBMessage> messages = new ArrayList<>();
		try {

			JSONObject json = new JSONObject(data);
			JSONArray jsonArray = json.getJSONArray(Keys.DATA);
			for (int i = 0; i< jsonArray.length();i++){
				JSONObject j = jsonArray.getJSONObject(i);
				String date = "";
				String message = "";
				String fromId = "";
				String toId = "";
				String channelId = "";
				String fromName = "";
				String channalName = "";

				if (!j.isNull(Keys.DATA))
					message = j.getString(Keys.DATA);
				if (!j.isNull(Keys.FROM))
					fromId = j.getString(Keys.FROM);
				if (!j.isNull(Keys.TO))
					toId = j.getString(Keys.TO);
				if (!j.isNull(Keys.CHANNAL))
					channelId = j.getString(Keys.CHANNAL);
				if (!j.isNull(Keys.FROM_NAME))
					fromName = j.getString(Keys.FROM_NAME);
				if (!j.isNull(Keys.CHANNAL_NAME))
					channalName = j.getString(Keys.CHANNAL_NAME);
				if (!j.isNull(Keys.CREATED_AT))
					date = j.getString(Keys.CREATED_AT);

				SimpleDateFormat sdf =   new SimpleDateFormat( Constants.DATE_TIME_JSON );
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date d = sdf.parse(date);


				DBMessage m = null;
				m = new DBMessage(null,my_userId, message,d.toString(), fromId, toId,channelId,"",fromName, channalName,fromMe,isSended);
				messages.add(m);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}
}
