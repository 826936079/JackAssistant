package com.jack.jackassistant.util;

import android.Manifest;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class Constants {

    public static final int REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE = 100;
    public static final int REQUEST_CODE_SELECTED_IMAGE = 200;


    public static final int RESULT_CODE_SELECTED_IMAGE = 250;

    public static final String KEY_SELECTED_IMAGE = "key_selected_image";

    public static final String TYPE_DRAWABLE = "drawable";

    public static final String[] PERMISSIONS_READ_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String FILE_SEPARATOR = "/";
    public static final int EXIT_INTERVAL_TIME = 2000;


}
