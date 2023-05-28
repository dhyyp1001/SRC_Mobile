package com.auto.src_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.auto.src_mobile.R;
import com.auto.src_mobile.network_side.NetworkUserConnection;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);


        final EditText editId = (EditText) findViewById(R.id.login_id);
        final EditText  editPassword = (EditText) findViewById(R.id.login_password);

        Button loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    public void run(){
                        NetworkUserConnection nuc = new NetworkUserConnection(editId.getText().toString(), editPassword.getText().toString());
                        if(nuc.okSign.equals("ok")) {
                            Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
                            startActivity(intent);
                        }
                    }
                }.start();
            }
        });
    }
}
