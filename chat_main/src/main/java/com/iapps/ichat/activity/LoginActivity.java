package com.iapps.ichat.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.ichat.adapter.AvatarAdapter;
import com.iapps.ichat.helper.BroadcastManager;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.Keys;
import com.iapps.ichat.helper.MessageService;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.generics.GenericFragmentActivity;
import com.iapps.libs.helpers.BaseUIHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

public class LoginActivity extends GenericFragmentActivity {
    @InjectView(R.id.edtAccount)
    private EditText edtAccount;
    @InjectView(R.id.edtPwd)
    private EditText edtPwd;
    @InjectView(R.id.btnLogin)
    private Button btnLogin;
    @InjectView(R.id.tvAvatar)
    private TextView tvAvatar;
    @InjectView(R.id.imgAvatar)
    private ImageView imgAvatar;

    private ProgressDialog dialog;
    private MessageDataReceiver mDataReceiver;
    private List<String> avatarUrls = new ArrayList<>();
    private String mSelectedAvatar = "";
    private boolean isExit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(Html.fromHtml("<font color='#FFFFFF'>" + getResources().getString(R.string.iapps) + "</font>"));
        init();

        //start service
        Intent myIntent = new Intent(LoginActivity.this, MessageService.class);
        LoginActivity.this.startService(myIntent);
    }

    private void init(){
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Validate field1 = new Validate(edtAccount);
//                field1.addValidator(new NotEmptyValidator(LoginActivity.this));
//
//                Validate field2 = new Validate(edtPwd);
//                field2.addValidator(new NotEmptyValidator(LoginActivity.this));
//
//                Form mForm = new Form();
//                mForm.addValidates(field1);
//                mForm.addValidates(field2);
//
//                if (mForm.validate()) {
//                    //send login cmd
                    BroadcastManager.sendLoginMessage(LoginActivity.this,edtAccount.getText().toString(), edtPwd.getText().toString(),mSelectedAvatar);
                    dialog.show();
//                }
            }
        });

        tvAvatar.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAvatar.getPaint().setAntiAlias(true);
        tvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarDialog();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarDialog();
            }
        });

        //register  broadcast
        mDataReceiver = new MessageDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_TO_USER);
        registerReceiver(mDataReceiver, filter);
    }

    private class MessageDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Bundle bundle = intent.getExtras();
                String data = bundle.getString(Keys.CONTENT);
                String cmd = bundle.getString(Keys.CMD);
                if(cmd.equals(Constants.CMD_CONNECT)){ //connect server successfully.
                    dialog.dismiss();
                    btnLogin.setEnabled(true);
                }else if(cmd.equals(Constants.CMD_RECONNECT)){
                    dialog.show();
                    BroadcastManager.sendReconnectMessage(LoginActivity.this);
                    btnLogin.setEnabled(false);
                }else{
                    JSONObject json = new JSONObject(data);
                    if(json.getString(Keys.CMD).equals(Constants.CMD_LOGIN) && json.getInt(Keys.STATUS_CODE) == Constants.LOGIN_SUCCESSFULLY){ // login successfully.
                        String clientId = json.getString(Keys.USER_ID);
                        UserInfoManager userInfoManager = UserInfoManager.getInstance(LoginActivity.this);
                        userInfoManager.saveUserInfo(clientId, edtAccount.getText().toString(), edtPwd.getText().toString(), mSelectedAvatar);
                        dialog.dismiss();
                        isExit = false;
                        finish();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }else{
                        dialog.dismiss();
                        Helper.showAlert(LoginActivity.this, json.getString(Keys.MESSAGE));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void showAvatarDialog(){

        if(avatarUrls.size() == 0){
            initAvatarUrls();
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar,null);
        GridView gvAvatar = (GridView)view.findViewById(R.id.gvAvatar);

        AvatarAdapter avatarAdapter = new AvatarAdapter(this,avatarUrls);
        gvAvatar.setAdapter(avatarAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final Dialog d = builder.create();
        d.show();

        gvAvatar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imgAvatar.setVisibility(View.VISIBLE);
                mSelectedAvatar = avatarUrls.get(i);
                BaseUIHelper.loadImageWithPlaceholderResizeThumb(LoginActivity.this, mSelectedAvatar, imgAvatar, R.drawable.default_useravatar);
                d.dismiss();
            }
        });
    }

    private void initAvatarUrls(){
        avatarUrls.add("http://www.rtysm.com/uploads/allimg/130717/1-130GG13057-51.gif");
        avatarUrls.add("http://img.woyaogexing.com/2016/01/17/bd07eb855cd94829!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/07/05/0e6d1fc235e245bc!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/06/21/bccf136cd46ad136!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/06/22/da63bdf5959a0cdd!200x200.png");
        avatarUrls.add("http://img.woyaogexing.com/2015/02/23/648ce6c426d58542!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/02/04/8739e19f9767f4fc!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/01/03/0ca7bc97c693d305!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2014/10/17/55d5b604bff7b326!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2014/09/11/3a366f6f268bc206!200x200.png");
        avatarUrls.add("http://img.woyaogexing.com/touxiang/katong/2014/0426/04a5db927b16c458!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/touxiang/katong/2014/0509/f947397e25c42e2c!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/touxiang/katong/20140319/cbe5ba16c81aad37!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/touxiang/katong/20140310/d756d6df118f74cf!200x200.jpg");
        avatarUrls.add("http://img.woyaogexing.com/2015/06/07/81c34813617b2620!200x200.png");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDataReceiver);

        if(isExit){
            stopService(new Intent(this,MessageService.class));
        }
    }
}
