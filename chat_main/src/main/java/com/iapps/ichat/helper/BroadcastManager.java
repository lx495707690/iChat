package com.iapps.ichat.helper;


import android.content.Context;
import android.content.Intent;

public class BroadcastManager {

    public BroadcastManager(){

    }

    //command get friends
    public static void sendGetFriends(Context ctx){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        intent.putExtra(Keys.CMD, Constants.CMD_FRIEND_LIST);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(ctx);
        String message = Helper.generateGetFriendMessage(userInfoManager.getClientId());
        intent.putExtra(Keys.CONTENT, message);
        ctx.sendBroadcast(intent);
    }

    //command reconnect
    public static void sendReconnectMessage(Context ctx){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        intent.putExtra(Keys.CMD, Constants.CMD_RECONNECT);
        intent.putExtra(Keys.CONTENT, "");
        ctx.sendBroadcast(intent);
    }

    //command disconnect
    public static void sendDisconnectMessage(Context ctx){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        intent.putExtra(Keys.CMD, Constants.CMD_DISCONNECT);
        ctx.sendBroadcast(intent);
    }

    //command login
    public static void sendLoginMessage(Context ctx,String account,String pwd,String avatar){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(ctx);
        String message = Helper.generateLoginMessage(account, pwd, avatar);
        intent.putExtra(Keys.CMD, Constants.CMD_LOGIN);
        intent.putExtra(Keys.CONTENT, message);
        ctx.sendBroadcast(intent);
    }

    //command send message
    public static void sendTxtMessage(Context ctx,String message,String channalId,String toId){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        String mClientId = UserInfoManager.getInstance(ctx).getClientId();
        message = Helper.generateTxtMessage(message,channalId,mClientId,toId);
        intent.putExtra(Keys.CONTENT, message);
        intent.putExtra(Keys.CMD, Constants.CMD_MESSAGE);
        ctx.sendBroadcast(intent);
    }

    //command create group
    public static void sendCreateGroupMessage(Context ctx,String name,String avatar,String inviteUsers){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        String mClientId = UserInfoManager.getInstance(ctx).getClientId();
        String message = Helper.generateCreateGroupMessage(name, mClientId, avatar, inviteUsers);
        intent.putExtra(Keys.CONTENT, message);
        intent.putExtra(Keys.CMD, Constants.CMD_CREATE_GROUP);
        ctx.sendBroadcast(intent);
    }

    //command offline message
    public static void sendGetOffLineMessage(Context ctx){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_TO_SERVICE);
        String mClientId = UserInfoManager.getInstance(ctx).getClientId();
        String message = Helper.generateGetOffLineMessage(mClientId);
        intent.putExtra(Keys.CONTENT, message);
        intent.putExtra(Keys.CMD, Constants.CMD_GET_OFFLINE_MESSAGE);
        ctx.sendBroadcast(intent);
    }
}
