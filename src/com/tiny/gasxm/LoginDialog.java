package com.tiny.gasxm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginDialog extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);
    }
    
    private String name,usernamestr,passwordstr,ipaddstr;
    private OnCustomDialogListener customDialogListener;
    EditText username,password,ipadd;

    public LoginDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
            super(context);
            this.name = name;
            this.customDialogListener = customDialogListener;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.logindialog);
            //设置标题
            setTitle(name); 
            username = (EditText)findViewById(R.id.username);
            password = (EditText)findViewById(R.id.password);
            ipadd = (EditText)findViewById(R.id.ipadd);
            Button clickBtn = (Button) findViewById(R.id.clickbtn);
            clickBtn.setOnClickListener(clickListener);
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	usernamestr =String.valueOf(username.getText());
            	passwordstr =String.valueOf(password.getText());
            	ipaddstr =String.valueOf(ipadd.getText());
            	//String qlcs=(GetTotalPack.gettotalpack(qbqlhex, 8)+GetTotalPack.gettotalpack(cishuhex, 4)).toUpperCase();
            	
                customDialogListener.back(usernamestr+"|"+passwordstr+"|"+ipaddstr);
                LoginDialog.this.dismiss();
            }
    };

}
