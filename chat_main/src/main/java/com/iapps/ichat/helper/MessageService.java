package com.iapps.ichat.helper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

public class MessageService extends Service {

    private WebSocketConnection mConnection;
    private CommandReceiver mCmdReceiver;//receive the user's cmd

    @Override
    public void onCreate() {
        //init connection,notification
        mConnection = new WebSocketConnection();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //receive the user's cmd
        if(mCmdReceiver == null){
            mCmdReceiver = new CommandReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_TO_SERVICE);
        registerReceiver(mCmdReceiver, filter);

        if(!mConnection.isConnected()){
            connectServer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectServer(){
        try{
            mConnection.connect(Constants.WEBSOCKET_HOST, new WebSocket.ConnectionHandler() {
                @Override
                public void onOpen() {
                    sendCMDToUser(Constants.CMD_CONNECT, Constants.CONNECT_SUCCESSFULLY);
                }

                @Override
                public void onClose(int code, String reason) {
                    sendCMDToUser(Constants.CMD_RECONNECT, Constants.CONNECT_FAILED);
                }

                @Override
                public void onTextMessage(String payload) {
                    sendCMDToUser(Constants.CMD_MESSAGE, payload);
                }

                @Override
                public void onRawTextMessage(byte[] payload) {

                }

                @Override
                public void onBinaryMessage(byte[] payload) {

                }
            });
        }catch (WebSocketException e) {
            sendCMDToUser(Constants.CMD_RECONNECT, Constants.CONNECT_FAILED);
        }
    }

    private void sendCMDToUser(String cmd,String content){

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Keys.CONTENT, content);
        bundle.putString(Keys.CMD, cmd);
        intent.setAction(Constants.CMD_TO_USER);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    //service receive cmd.
    private class CommandReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra(Keys.CMD);
            String content = intent.getStringExtra(Keys.CONTENT);
            if(cmd.equals(Constants.CMD_RECONNECT)){         // reconnect
                if(!mConnection.isConnected()){
                    mConnection.reconnect();
                }
            }else if(cmd.equals(Constants.CMD_DISCONNECT)){// disconnect
                if(mConnection.isConnected()){
                    mConnection.disconnect();
                }
            }else{                                           //send txt message
                if(mConnection != null){
                    mConnection.sendTextMessage(content);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(mCmdReceiver);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
