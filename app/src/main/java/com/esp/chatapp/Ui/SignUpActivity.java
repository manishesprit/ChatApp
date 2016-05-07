package com.esp.chatapp.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Utils;

/**
 * Created by admin on 2/5/16.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtMobile;
    private TextView txtsignup;
    private Intent intent;
    private ImageView imgFacebook;
    private ImageView imgInsta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgInsta = (ImageView) findViewById(R.id.imgInsta);

        Utils.setDefaultRoundImage(SignUpActivity.this, imgInsta, R.drawable.insta);
        Utils.setDefaultRoundImage(SignUpActivity.this, imgFacebook, R.drawable.facebook);

        txtsignup = (TextView) findViewById(R.id.txtsignup);
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.activity.finish();
                finish();
            }
        });
    }
}
