package com.rs.timepass.Uc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rs.timepass.R;


public class AlertDailogView {
    // Handle Ok Button by current activity
    public static final int BUTTON_OK = 1;
    // Handle Cancel Button by current activity
    public static final int BUTTON_CANCEL = 2;

    public static Dialog showAlert(Context context, String message) {
        return showAlert(context, context.getString(R.string.Alert), message,
                context.getString(R.string.OK), false, "", null, 1);
    }

    public static Dialog showAlert(Context context, String message,
                                   String btnText) {
        return showAlert(context, context.getString(R.string.Alert), message,
                btnText, false, "", null, 1);
    }

    public static Dialog showAlert(Context context, String title,
                                   String message, String btnText) {
        return showAlert(context, title, message, btnText, false, "", null, 1);
    }

    public static Dialog showAlert(Context context, String message,
                                   String btnText, OnPopUpDialogButoonClickListener clickListener) {
        return showAlert(context, context.getString(R.string.Alert), message,
                btnText, false, "", clickListener, 1);
    }

    public static Dialog showAlert(Context context, String message,
                                   String btnText, OnPopUpDialogButoonClickListener clickListener,
                                   int tag) {
        return showAlert(context, context.getString(R.string.Alert), message,
                btnText, false, "", clickListener, tag);
    }

    public static Dialog showAlert(Context context, String title,
                                   String message, String btnText,
                                   OnPopUpDialogButoonClickListener clickListener, int tag) {
        return showAlert(context, title, message, btnText, false, "",
                clickListener, tag);
    }

    public static Dialog showAlert(Context context, String title,
                                   String message, String btnText,
                                   OnPopUpDialogButoonClickListener clickListener) {
        return showAlert(context, title, message, btnText, false, "",
                clickListener, 1);
    }

    public static Dialog showAlert(Context context, String title,
                                   String btnTitle_1, boolean isCancelButton, String btnTitle_2,
                                   OnPopUpDialogButoonClickListener clickListener, int tag,
                                   boolean isInput) {
        return showAlert(context, title, "", btnTitle_1, isCancelButton,
                btnTitle_2, clickListener, tag);
    }

    public static Dialog showAlert(Context context, String message,
                                   String btnTitle_1, boolean isCancelButton, String btnTitle_2,
                                   final OnPopUpDialogButoonClickListener clickListener,
                                   final int tag) {
        return showAlert(context, context.getString(R.string.Alert), message,
                btnTitle_1, isCancelButton, btnTitle_2, clickListener, tag);
    }

    // Create Custom popup Alert dailog here
    public static Dialog showAlert(Context context, String title,
                                   String message, String btnTitle_1, boolean isCancelButton,
                                   String btnTitle_2,
                                   final OnPopUpDialogButoonClickListener clickListener,
                                   final int tag) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dailog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        TextView txt_message = (TextView) dialog
                .findViewById(R.id.txtMessage);
        final Button btnFirst = (Button) dialog
                .findViewById(R.id.btnFirst);
        final Button btnSecond = (Button) dialog
                .findViewById(R.id.btnSecond);
        LinearLayout llbtnSecond = (LinearLayout) dialog
                .findViewById(R.id.llbtnSecond);

        if (title != null && !title.equals(""))
            txtTitle.setText(title);

        txt_message.setText(message);
        btnFirst.setText(btnTitle_1);
        btnSecond.setText(btnTitle_2);

        btnFirst.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (clickListener != null)
                    clickListener.OnButtonClick(tag, AlertDailogView.BUTTON_OK,
                            null);

            }
        });

        if (isCancelButton) {
            llbtnSecond.setVisibility(View.VISIBLE);
            btnSecond.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (clickListener != null)
                        clickListener.OnButtonClick(tag,
                                AlertDailogView.BUTTON_CANCEL, null);

                }
            });

        } else {
            llbtnSecond.setVisibility(View.GONE);
        }

        return dialog;
    }

}
