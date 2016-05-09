package com.esp.chatapp.Ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esp.chatapp.CropImage.Crop;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by admin on 2/5/16.
 */
public class CreatePostActivity extends Activity implements View.OnClickListener {

    private EditText edtCaption;
    private TextView txtGallary;
    private TextView txtCamara;
    private TextView txtSubmit;
    private Intent intent;
    private RelativeLayout rlUploadView;
    private ImageView imgUploadView;
    private File upload_file;
    private Uri destination;
    private TextView txtRemove;
    private boolean isPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);

        edtCaption = (EditText) findViewById(R.id.edtCaption);
        txtRemove = (TextView) findViewById(R.id.txtRemove);
        txtGallary = (TextView) findViewById(R.id.txtGallary);
        txtCamara = (TextView) findViewById(R.id.txtCamara);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        imgUploadView = (ImageView) findViewById(R.id.imgUploadView);
        rlUploadView = (RelativeLayout) findViewById(R.id.rlUploadView);
        imagePath();

        txtRemove.setOnClickListener(this);
        txtGallary.setOnClickListener(this);
        txtCamara.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

    }

    public void imagePath() {
        destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        File dir = new File(Config.DIR_USERDATA);
        if (dir.exists() == true) {
            System.out.println("delete Derectory");
            dir.delete();
            dir.mkdirs();
        } else {
            dir.mkdirs();
        }
        upload_file = new File(Config.DIR_USERDATA, "temp.jpeg");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtGallary:
                Crop.pickImage(CreatePostActivity.this);

                break;

            case R.id.txtCamara:
                Crop.pickCamara(CreatePostActivity.this, Uri.fromFile(upload_file));
                break;

            case R.id.txtSubmit:
                break;

            case R.id.txtRemove:
                isPhoto = false;
                rlUploadView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == Crop.REQUEST_GALLERY) {
                Crop.of(data.getData(), destination).start(this);
            } else if (requestCode == Crop.REQUEST_CAMARA) {
                Crop.of(Uri.fromFile(upload_file), destination).start(this);
            } else if (requestCode == Crop.REQUEST_CROP) {
                try {

                    InputStream is = getContentResolver().openInputStream(Crop.getOutput(data));
                    FileOutputStream fos = new FileOutputStream(upload_file);
                    copyStream(is, fos);
                    fos.close();
                    is.close();

                    Utils.compressImage(upload_file.getPath(), CreatePostActivity.this);
                    int degree = Utils.getCameraPhotoOrientation(CreatePostActivity.this, Uri.fromFile(upload_file), upload_file.getPath());
                    Utils.rotateBitmap(BitmapFactory.decodeFile(upload_file.getPath()), degree);
                    imgUploadView.setImageBitmap(null);
                    imgUploadView.setImageBitmap(BitmapFactory.decodeFile(upload_file.getPath()));
                    isPhoto = true;
                    rlUploadView.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    System.out.println("ERROR========" + e.toString());
                }
            }
        }
    }
}
