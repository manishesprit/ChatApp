/**
 * This is loading bar
 *
 * @author rreddy.avula
 */
package com.esp.chatapp.Uc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

import com.esp.chatapp.R;


public class CustomProgressBarDialog extends Dialog {
    Context objContext;

    public CustomProgressBarDialog(Context context) {
        super(context);
        objContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cutom_progress_bar);
        try {
            ImageView iv = (ImageView) findViewById(R.id.progressBar1);

            iv.setBackgroundResource(R.anim.anim_progress_dailog);
            AnimationDrawable aniFrame = (AnimationDrawable) iv.getBackground();
            aniFrame = (AnimationDrawable) iv.getBackground();
            aniFrame.start();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }


        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

}
