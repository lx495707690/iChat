package com.iapps.ichat.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.iapps.ichat.R;
import com.iapps.ichat.activity.HomeActivity;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

public class MessageService extends Service {

    private WebSocketConnection mConnection;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private PendingIntent mPendingIntent;

    private CommandReceiver mCmdReceiver;//receive the user's cmd

    @Override
    public void onCreate() {
        //init connection,notification
        mConnection = new WebSocketConnection();
        mNotification = new Notification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //receive the user's cmd
        mCmdReceiver = new CommandReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_SENDMESSAGE);
        registerReceiver(mCmdReceiver, filter);

        connectServer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectServer(){
        try{
            mConnection.connect(Constants.WEBSOCKET_HOST, new WebSocket.ConnectionHandler() {
                @Override
                public void onOpen() {
                    sendMessageToUser(Constants.CONNECT_SUCCESSFULLY);
                }

                @Override
                public void onClose(int code, String reason) {
                    sendMessageToUser(Constants.CONNECT_CLOSED);
                }

                @Override
                public void onTextMessage(String payload) {
                        sendMessageToUser(payload);
                        //创建 notification(需要判断是否 开启APP  如果开启的话，就不需要创建notification)
                        //sendNotification(payload);
                }

                @Override
                public void onRawTextMessage(byte[] payload) {

                }

                @Override
                public void onBinaryMessage(byte[] payload) {

                }
            });
        }catch (WebSocketException e) {
            sendMessageToUser(Constants.CONNECT_FAILED);
        }
    }

    private void sendMessageToUser(String message){

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Keys.MESSAGE_DATA, message);
        intent.setAction(Constants.CMD_RECEIVEMESSAGE);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private class CommandReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(Keys.MESSAGE_DATA);
            //send txt message
            if(mConnection != null){
                mConnection.sendTextMessage(message);
            }
        }
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(mCmdReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void sendNotification(String sendText){
        Intent intent = new Intent(MessageService.this, HomeActivity.class);
        mNotification.icon = R.drawable.icon;
        mNotification.tickerText=sendText;
        mNotification.defaults=Notification.DEFAULT_SOUND;

        mPendingIntent=PendingIntent.getActivity(MessageService.this, 0,intent, 0);
        mNotification.setLatestEventInfo(MessageService.this, "title", "text",mPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1989, mNotification);
    }
}
