package com.didiglobal.booster.instrument;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;


/**
 * @author johnsonlee
 */
public class ShadowAlertDialog extends AlertDialog {

    private final Handler mainThreadHandler;

    public ShadowAlertDialog(Context context) {
        super(context);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAlertDialog", "create");
    }

    public ShadowAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAlertDialog", "create");
    }

    public ShadowAlertDialog(final Context context, final boolean cancelable, final DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAlertDialog", "create");
    }

    @Override
    public void dismiss() {
        Log.d("ShadowAlertDialog", "dismiss");
        mainThreadHandler.post(super::dismiss);
    }
}