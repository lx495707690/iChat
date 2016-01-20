package com.iapps.ichat.helper;
import com.iapps.libs.helpers.BaseConverter;
import org.json.JSONObject;

import java.util.Date;

import me.itangqi.greendao.DBMessage;

public class Converter
	extends BaseConverter {

	public static DBMessage toTxtMessage(String data,String toId,String imgUrl,boolean fromMe) {
        DBMessage m = null;
		try {
			JSONObject j = new JSONObject(data);
            Date date = new Date();
            String message = "";
            String fromId = "";
            String channelId = "";

            if (!j.isNull(Keys.DATA))
				message = j.getString(Keys.DATA);

			if (!j.isNull(Keys.FROM))
                fromId = j.getString(Keys.DATA);

			if (!j.isNull(Keys.CHANNAL))
                channelId = j.getString(Keys.DATA);

            m = new DBMessage(null, message,date.toString(), fromId, toId,channelId,imgUrl, fromMe);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
}
