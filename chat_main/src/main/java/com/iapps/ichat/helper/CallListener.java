package com.iapps.ichat.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallListener extends PhoneStateListener {
	 private boolean called = false;
     private final TelephonyManager telephonyManager;
     private final Context context;
     private Activity activity;
     public static final String PARAM_CALL_DONE = "CALL_DONE";

     private CallListener(Activity act) {
         this.context = act.getBaseContext();
         this.activity = act;
         this.telephonyManager = (TelephonyManager) context
                 .getSystemService(Activity.TELEPHONY_SERVICE);
     }

     public static void createListenerFor(Activity act) {
    	 CallListener listener = new CallListener(act);
         listener.telephonyManager.listen(listener, LISTEN_CALL_STATE);
     }

     @Override
     public void onCallStateChanged(int state, String incomingNumber) {
         if (called && state == TelephonyManager.CALL_STATE_IDLE) {
             called = false;
             telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
             try {
                 Intent t = new Intent(context, activity.getClass());
                 t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 t.putExtras(activity.getIntent());
                 t.putExtra(PARAM_CALL_DONE, true);
                 t.setAction(Intent.ACTION_MAIN);
                 activity.finish();
                 activity = null;
                 context.startActivity(t);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         } else {
             if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                 called = true;
             }
         }
     }
}
