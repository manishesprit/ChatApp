package com.rs.timepass.Ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Backend.ChangeAvatarAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.CropImage.CropImage;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeAvatarActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Intent intent;
    private Toolbar toolbar;
    private ImageView imgProfileAvatar;
    private TextView txtMobile;
    private TextView txtStatus;
    private Context context;

    int OPEN_GALLARY_CODE = 200;
    int OPEN_CAMARA_CODE = 400;
    int OPEN_CROP_CODE = 600;
    private File upload_file;
    private Dialog dialog;
    private SwipeRefreshLayout swipeContainer;
    private ChangeAvatarAPI changeAvatarAPI;
    private LinearLayout myprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Change Profile");

        imgProfileAvatar = (ImageView) findViewById(R.id.imgProfileAvatar);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        setData();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                setData();
            }
        });


        imgProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });

        txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setData() {
        Utils.setDefaultRoundImage(context, imgProfileAvatar, R.drawable.default_user);

        if (!Pref.getValue(context, Config.PREF_AVATAR, "").toString().trim().equalsIgnoreCase("")) {
            Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS + Pref.getValue(context, Config.PREF_AVATAR, ""))
                    .asBitmap()
                    .error(R.drawable.default_user).placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imgProfileAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        txtMobile.setText(Pref.getValue(context, Config.PREF_MOBILE, ""));
        txtStatus.setText(Pref.getValue(context, Config.PREF_STATUS, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show_dialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_choose_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        LinearLayout llGallery = (LinearLayout) dialog.findViewById(R.id.llGallery);
        LinearLayout llCamera = (LinearLayout) dialog.findViewById(R.id.llCamera);

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                CreateFolder();

                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, OPEN_GALLARY_CODE);

            }
        });

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

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

            }
        });

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

                    Utils.compressImage(upload_file.getPath(), context, 1);
                    int degree = Utils.getCameraPhotoOrientation(context, Uri.fromFile(upload_file), upload_file.getPath());
                    Utils.rotateBitmap(BitmapFactory.decodeFile(upload_file.getPath()), degree);

                    setAvatar();


                } catch (Exception e) {
                    System.out.println("ERROR========" + e.toString());
                }
            }
        }
    }

    public void setAvatar() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);

            changeAvatarAPI = new ChangeAvatarAPI(context, upload_file.getName().toString(), responseListener);
            changeAvatarAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_CHANGE_AVATAR) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeFile(upload_file.getPath()));
                    circularBitmapDrawable.setCircular(true);
                    imgProfileAvatar.setImageDrawable(circularBitmapDrawable);
                }
            } else {
                AlertDailogView.showAlert(context, "File uploaded fail").show();
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

    public void CreateFolder() {

        File dir = new File(Config.DIR_USERDATA);
        if (dir.exists() == true) {
            System.out.println("delete Derectory");
            dir.delete();
            dir.mkdirs();
        } else {
            dir.mkdirs();
        }
        upload_file = new File(Config.DIR_USERDATA, "Avatar" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()).toString() + ".jpeg");
    }


    private void startCropImage() {
        try {
            Intent intent = new Intent(context, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, upload_file.getPath());
            intent.putExtra(CropImage.SCALE, true);
            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);
            startActivityForResult(intent, OPEN_CROP_CODE);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                setAvatar();
            }
        }
    }
}
