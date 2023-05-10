package com.didiglobal.booster.instrument;

import android.content.Context;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


/**
 * @author johnsonlee
 */
public class ShadowDialog extends Dialog {

    private Handler mainThreadHandler;

    public ShadowDialog(Context context) {
        super(context);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowDialog", "create");
    }

    public ShadowDialog(Context context, int themeResId) {
        super(context, themeResId);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowDialog", "create");
    }

    public ShadowDialog(final Context context, final boolean cancelable, final DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowDialog", "create");
    }

    public ShadowDialog(final Context context, final int themeResId, final boolean cancelable, final DialogInterface.OnCancelListener cancelListener) {
        super(context, themeResId, cancelable, cancelListener);
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d("ShadowDialog", "create");
    }

    @Override
    public void dismiss() {
        Log.d("ShadowDialog", "dismiss");
        //mainThreadHandler.post(() -> super.dismiss());
    }
}