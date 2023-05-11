package com.didiglobal.booster.instrument;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;


/**
 * @author Daniel Olsson
 */
public class ShadowAppCompatDialog extends AppCompatDialog {

    private final Handler mainThreadHandler;

    public ShadowAppCompatDialog(Context context) {
        super(context);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAppCompatDialog", "create");
    }

    public ShadowAppCompatDialog(Context context, int themeResId) {
        super(context, themeResId);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAppCompatDialog", "create");
    }

    public ShadowAppCompatDialog(final Context context, final boolean cancelable, final DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowAppCompatDialog", "create");
    }

    @Override
    public void dismiss() {
        Log.d("ShadowAppCompatDialog", "dismiss");
        mainThreadHandler.post(super::dismiss);
    }
}