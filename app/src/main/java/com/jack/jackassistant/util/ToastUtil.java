package com.jack.jackassistant.util;


import android.content.Context;
import android.widget.Toast;


public class ToastUtil {

    private static Toast toast = null;
     

    public static void showShortToast(Context context, String content){
        if (context == null) {
            return;
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showLongToast(Context context, String content){
        if (context == null) {
            return;
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortToast(Context context, int resId){
        if (context == null) {
            return;
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showLongToast(Context context, int resId){
        if (context == null) {
            return;
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
        toast.show();
    }


}
