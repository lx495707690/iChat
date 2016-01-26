package com.iapps.ichat.helper;

import com.iapps.libs.helpers.BaseConverter;

import org.json.JSONObject;

import java.util.Date;

import me.itangqi.greendao.DBMessage;

public class Converter
	extends BaseConverter {

	public static DBMessage toTxtMessage(String data,String my_userId,boolean fromMe,boolean isSended) {
        DBMessage m = null;
		try {
			JSONObject j = new JSONObject(data);
            Date date = new Date();
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

            m = new DBMessage(null,my_userId, message,date.toString(), fromId, toId,channelId,"",fromName, channalName,fromMe,isSended);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
}
