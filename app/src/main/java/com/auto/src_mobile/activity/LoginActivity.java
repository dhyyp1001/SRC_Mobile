package com.auto.src_mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.auto.src_mobile.R;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editId = (EditText) findViewById(R.id.login_id);
        final EditText  editPassword = (EditText) findViewById(R.id.login_password);
    }
}
