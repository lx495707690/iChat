package com.iapps.ichat.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.iapps.ichat.R;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.Keys;
import com.iapps.ichat.helper.MessageService;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.generics.GenericFragmentActivity;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import org.json.JSONObject;
import roboguice.inject.InjectView;

public class LoginActivity extends GenericFragmentActivity {
    @InjectView(R.id.edtAccount)
    private EditText edtAccount;
    @InjectView(R.id.edtPwd)
    private EditText edtPwd;
    @InjectView(R.id.btnLogin)
    private Button btnLogin;
    private ProgressDialog dialog;
    private MessageDataReceiver mDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(Html.fromHtml("<font color='#FFFFFF'>" + getResources().getString(R.string.iapps) + "</font>"));
        init();
    }

    private void init(){
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Validate field1 = new Validate(edtAccount);
                field1.addValidator(new NotEmptyValidator(LoginActivity.this));

                Validate field2 = new Validate(edtPwd);
                field2.addValidator(new NotEmptyValidator(LoginActivity.this));

                Form mForm = new Form();
                mForm.addValidates(field1);
                mForm.addValidates(field2);

                if (mForm.validate()) {
                    //start service
                    Intent myIntent = new Intent(LoginActivity.this, MessageService.class);
                    LoginActivity.this.startService(myIntent);
                    dialog.show();
                }
            }
        });

        //register  broadcast
        mDataReceiver = new MessageDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_RECEIVEMESSAGE);
        registerReceiver(mDataReceiver, filter);
    }

    private class MessageDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Bundle bundle = intent.getExtras();
                String data = bundle.getString(Keys.MESSAGE_DATA);
                if(data.equals(Constants.CONNECT_SUCCESSFULLY)){ //connect server successfully.
                    //send login cmd
                    sendLoginMessage(edtAccount.getText().toString(),edtPwd.getText().toString());
                }else if(data.equals(Constants.CONNECT_FAILED)){

                }else{
                    JSONObject json = new JSONObject(data);
                    if(json.getString(Keys.CMD).equals(Constants.CMD_LOGIN)){ // login successfully.
                        String clientId = json.getString(Keys.FD);
                        UserInfoManager userInfoManager = UserInfoManager.getInstance(LoginActivity.this);
                        userInfoManager.saveClientId(clientId);
                        dialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }else{
                        Helper.showAlert(LoginActivity.this, R.string.invalid_account_or_password);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void sendLoginMessage(String account,String pwd){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_SENDMESSAGE);
        String message = Helper.generateLoginMessage(account,pwd);
        intent.putExtra(Keys.MESSAGE_DATA, message);
        sendBroadcast(intent);
    }
}
