package com.esp.chatapp.Ui;

import android.app.Activity;
import android.os.Bundle;

import com.esp.chatapp.R;

/**
 * Created by admin on 2/5/16.
 */
public class CreatePostActivity extends Activity {

//    private EditText edtEmail;
//    private EditText edtPassword;
//    private EditText edtRePassword;
//    private EditText edtMobile;
//    private TextView txtsignup;
//    private Intent intent;
//    private ImageView imgFacebook;
//    private ImageView imgInsta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);

//        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
//        imgInsta = (ImageView) findViewById(R.id.imgInsta);
//
//        Utils.setDefaultRoundImage(CreatePostActivity.this, imgInsta, R.drawable.insta);
//        Utils.setDefaultRoundImage(CreatePostActivity.this, imgFacebook, R.drawable.facebook);
//
//        txtsignup = (TextView) findViewById(R.id.txtsignup);
//        txtsignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(CreatePostActivity.this, HomeActivity.class);
//                startActivity(intent);
//                LoginActivity.activity.finish();
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
