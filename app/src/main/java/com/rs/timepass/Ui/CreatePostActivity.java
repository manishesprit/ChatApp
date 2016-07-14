package com.rs.timepass.Ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rs.timepass.Backend.CreateFeedAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.CropImage.CropImage;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private TextView txtRemove;
    private Context context;
    private PostBean postBean;
    private CreateFeedAPI createFeedAPI;
    private LinearLayout myprogressBar;

    int OPEN_GALLARY_CODE = 200;
    int OPEN_CAMARA_CODE = 400;
    int OPEN_CROP_CODE = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);
        context = this;
        edtCaption = (EditText) findViewById(R.id.edtCaption);
        txtRemove = (TextView) findViewById(R.id.txtRemove);
        txtGallary = (TextView) findViewById(R.id.txtGallary);
        txtCamara = (TextView) findViewById(R.id.txtCamara);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        imgUploadView = (ImageView) findViewById(R.id.imgUploadView);
        rlUploadView = (RelativeLayout) findViewById(R.id.rlUploadView);
        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);

        txtRemove.setOnClickListener(this);
        txtGallary.setOnClickListener(this);
        txtCamara.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

        postBean = new PostBean();
        postBean.image_url = "";
        postBean.caption = "";
    }

    public void CreateFolder() {

        File dir = new File(Config.DIR_FEEDDATA);
        if (dir.exists() == true) {
            Log.print("delete Derectory");
            dir.delete();
            dir.mkdirs();
        } else {
            dir.mkdirs();
        }
        upload_file = new File(Config.DIR_FEEDDATA, "Image" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()).toString() + ".jpeg");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtGallary:
                CreateFolder();

                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, OPEN_GALLARY_CODE);

                break;

            case R.id.txtCamara:
                CreateFolder();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    Uri mImageCaptureUri = Uri.fromFile(upload_file);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("return-data", false);
                    startActivityForResult(intent, OPEN_CAMARA_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.txtSubmit:

                postBean.caption = edtCaption.getText().toString().trim();

                if (!postBean.caption.equals("") || !postBean.image_url.equals("")) {

                    if (Utils.isOnline(context)) {
                        Log.print("=======postBean.image_url=====" + postBean.image_url);
                        myprogressBar.setVisibility(View.VISIBLE);
                        createFeedAPI = new CreateFeedAPI(context, responseListener, postBean);
                        createFeedAPI.execute();
                    } else {
                        AlertDailogView.showAlert(context, "Internet not available").show();
                    }

                } else {
                    AlertDailogView.showAlert(context, "Can not blank post upload").show();
                }
                break;

            case R.id.txtRemove:
                rlUploadView.setVisibility(View.INVISIBLE);
                postBean.image_url = "";
                break;
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_CREATE_FEED) {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                AlertDailogView.showAlert(context, "Feed uploaded fail").show();
            }
        }
    };

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

            if (requestCode == OPEN_GALLARY_CODE) {
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fos = new FileOutputStream(upload_file);
                    copyStream(is, fos);
                    fos.close();
                    is.close();
                    startCropImage();
                } catch (Exception e) {

                }
            }

            if (requestCode == OPEN_CAMARA_CODE) {
                startCropImage();
            }

            if (requestCode == OPEN_CROP_CODE) {
                try {

                    Utils.compressImage(upload_file.getPath(), context, 2);
                    int degree = Utils.getCameraPhotoOrientation(context, Uri.fromFile(upload_file), upload_file.getPath());
                    Utils.rotateBitmap(BitmapFactory.decodeFile(upload_file.getPath()), degree);
                    imgUploadView.setImageBitmap(null);
                    imgUploadView.setImageBitmap(BitmapFactory.decodeFile(upload_file.getPath()));
                    rlUploadView.setVisibility(View.VISIBLE);
                    postBean.image_url = upload_file.getName();

                } catch (Exception e) {
                    Log.print("ERROR========" + e.toString());
                }
            }
        }
    }

    private void startCropImage() {
        try {
            Intent intent = new Intent(context, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, upload_file.getPath());
            intent.putExtra(CropImage.SCALE, true);
            intent.putExtra(CropImage.ASPECT_X, 0);
            intent.putExtra(CropImage.ASPECT_Y, 0);
            startActivityForResult(intent, OPEN_CROP_CODE);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
