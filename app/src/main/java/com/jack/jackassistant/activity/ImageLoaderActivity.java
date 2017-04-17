package com.jack.jackassistant.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.ImageLoaderGridViewAdapter;
import com.jack.jackassistant.bean.ImageFolder;
import com.jack.jackassistant.util.Constants;
import com.jack.jackassistant.util.ScreenUtil;
import com.jack.jackassistant.view.ImageLoaderPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ImageLoaderActivity extends AppCompatActivity implements ImageLoaderGridViewAdapter.OnImageItemClickListener,
        ImageLoaderPopupWindow.OnImageLoaderPopupWindowSelectedListener,
        EasyPermissions.PermissionCallbacks {

    private static final String TAG = "ImageLoaderActivity";
    private GridView mImageLoaderGridView;
    private ImageLoaderGridViewAdapter mImageLoaderGridViewAdapter;
    private List<String> mGridViewListDatas;

    private RelativeLayout mImageLoaderBottomRelativeLayout;
    private TextView mImageLoaderBottomDirName;
    private TextView mImageLoaderBottomImgCount;

    private ImageLoaderPopupWindow mImageLoaderPopupWindow;
    private List<ImageFolder> mImageFloderDatas = new ArrayList<ImageFolder>();

    private File mCurrentDir;
    private int mCurrentImgCount;

    private List<String> mTotalImgPaths = new ArrayList<String>();
    private int mTotalImgCount;

    private ProgressDialog mProgressDialog;

    private static final int SCAN_OK = 0x01;
    private static final double IMAGE_LOADER_POPUPWINDOW_HEIGHT_PERCENT = 0.7;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_OK:
                    mProgressDialog.dismiss();

                    dataToView(mTotalImgPaths);

                    initImageLoaderPopupWindow();

                    break;
            }
        }
    };

    private void dataToView(File currentDir) {

        if (currentDir == null) {
            Toast.makeText(this, R.string.no_picture_scaned, Toast.LENGTH_SHORT).show();
            return;
        }

        mGridViewListDatas = Arrays.asList(currentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".png")) {
                    return true;
                }
                return false;
            }
        }));
        mImageLoaderGridViewAdapter = new ImageLoaderGridViewAdapter(mGridViewListDatas, currentDir.getAbsolutePath(), this);
        mImageLoaderGridView.setAdapter(mImageLoaderGridViewAdapter);

        mImageLoaderBottomDirName.setText(currentDir.getName());
        showBottomImgCount(ImageLoaderGridViewAdapter.sSelectedImages, mCurrentImgCount);

    }

    private void dataToView(ImageFolder imageFolder) {

        if (imageFolder == null) {
            Toast.makeText(this, R.string.no_picture_scaned, Toast.LENGTH_SHORT).show();
            return;
        }

        File dir = new File(imageFolder.getDirPath());
        mGridViewListDatas = Arrays.asList(dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".png")) {
                    return true;
                }
                return false;
            }
        }));
        mImageLoaderGridViewAdapter = new ImageLoaderGridViewAdapter(mGridViewListDatas, dir.getAbsolutePath(), this);
        mImageLoaderGridView.setAdapter(mImageLoaderGridViewAdapter);

        mImageLoaderBottomDirName.setText(imageFolder.getDirName());
        showBottomImgCount(ImageLoaderGridViewAdapter.sSelectedImages, imageFolder.getDirCount());

    }

    private void dataToView(List<String> datas) {

        if (datas == null) {
            Toast.makeText(this, R.string.no_picture_scaned, Toast.LENGTH_SHORT).show();
            return;
        }


        mImageLoaderGridViewAdapter = new ImageLoaderGridViewAdapter(datas, ImageLoaderGridViewAdapter.FILE_SEPARATOR, this);
        mImageLoaderGridView.setAdapter(mImageLoaderGridViewAdapter);

        mImageLoaderBottomDirName.setText(getString(R.string.image_and_video));
        showBottomImgCount(ImageLoaderGridViewAdapter.sSelectedImages, mTotalImgCount);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);

        initView();
        initData();
        initEvent();
    }


    private void initImageLoaderPopupWindow() {

        View conventView = LayoutInflater.from(this).inflate(R.layout.image_loader_popup_window, null);
        int width = ScreenUtil.getWindowsWidth(this);
        int height = (int) (ScreenUtil.getWindowsHeight(this) * IMAGE_LOADER_POPUPWINDOW_HEIGHT_PERCENT);
        mImageLoaderPopupWindow = new ImageLoaderPopupWindow(conventView, width, height, mImageFloderDatas);

        mImageLoaderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundlightOn();
            }
        });

        mImageLoaderPopupWindow.setOnImageLoaderPopupWindowSelectedListener(this);
    }

    private void initEvent() {
        mImageLoaderBottomRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                mImageLoaderPopupWindow.setAnimationStyle(R.style.imageLoaderPopupWindowDisplayAnim);
                mImageLoaderPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                mImageLoaderPopupWindow.showAsDropDown(mImageLoaderBottomRelativeLayout, 0, 0);
                backgroundlightOff();
            }
        });
    }

    @AfterPermissionGranted(Constants.REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE)
    private void initData() {
        if (EasyPermissions.hasPermissions(this, Constants.PERMISSIONS_READ_EXTERNAL_STORAGE)) {

            //通过ContentProvider扫描所有图片
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, R.string.sdcard_unmounted, Toast.LENGTH_SHORT).show();
            }

            mProgressDialog = ProgressDialog.show(this, null, getString(R.string.image_loading));

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver cr = ImageLoaderActivity.this.getContentResolver();

                    Cursor cursor = cr.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                    if (cursor == null) {
                        return;
                    }

                    Set<String> dirPaths = new HashSet<String>();

                    while (cursor.moveToNext()) {
                        //获取图片路径
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                        MyLog.e(TAG, "run-> path:" + path);

                        File parentFile = new File(path).getParentFile();
//                        MyLog.e(TAG, "run-> parentFile:" + parentFile);

                        if (parentFile == null) {
                            continue;
                        }

                        String dirPath = parentFile.getAbsolutePath();
//                        MyLog.e(TAG, "run-> dirPath:" + dirPath);

                        ImageFolder imageFolder;

                        if (dirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            dirPaths.add(dirPath);

                            imageFolder = new ImageFolder();
                            imageFolder.setDirPath(dirPath);
                            imageFolder.setFirstImagePath(path);
                        }

                        if (parentFile.list() == null) {
                            continue;
                        }

                        String[] imgPaths = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg")
                                        || filename.endsWith(".jpeg")
                                        || filename.endsWith(".png")) {
                                    return true;
                                }
                                return false;
                            }
                        });

                        int parentFileSize = imgPaths.length;

                        for (String imgPath : imgPaths) {
                            mTotalImgPaths.add(dirPath + ImageLoaderGridViewAdapter.FILE_SEPARATOR + imgPath);
                        }


                        mTotalImgCount += parentFileSize;

                        imageFolder.setDirCount(parentFileSize);
                        mImageFloderDatas.add(imageFolder);


//                        MyLog.e(TAG, "run-> parentFileSize:" + parentFileSize);

//                        if (parentFileSize > mCurrentImgCount) {
//                            mCurrentImgCount = parentFileSize;
//                            mCurrentDir = parentFile;
//                        }

                    }
                    mCurrentImgCount = mTotalImgCount;
                    ImageFolder totalImageFolder = new ImageFolder();
                    totalImageFolder.setDirPath("/" + getString(R.string.image_and_video));
                    totalImageFolder.setDirCount(mTotalImgCount);
                    totalImageFolder.setFirstImagePath(mTotalImgPaths.get(0));
                    mImageFloderDatas.add(0, totalImageFolder);


                    handler.sendEmptyMessage(SCAN_OK);
                    cursor.close();

                }
            }).start();
        } else {
            // Ask for permissions
            EasyPermissions.requestPermissions(this, getString(R.string.request_read_storage_load_image),
                    Constants.REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE,
                    Constants.PERMISSIONS_READ_EXTERNAL_STORAGE);
        }

    }

    private void initView() {
        mImageLoaderGridView = (GridView) findViewById(R.id.imageLoaderGridView);
        mImageLoaderBottomRelativeLayout = (RelativeLayout) findViewById(R.id.imageLoaderBottomRelativeLayout);
        mImageLoaderBottomDirName = (TextView) findViewById(R.id.imageLoaderBottomDirName);
        mImageLoaderBottomImgCount = (TextView) findViewById(R.id.imageLoaderBottomImgCount);
    }

    private void showBottomImgCount(Set<String> selectedImages, int currentImgCount) {
        if (selectedImages != null && selectedImages.size() > 0) {
            mImageLoaderBottomImgCount.setText(String.format(getString(R.string.image_selected_sheet), selectedImages.size()));
        } else {
            mImageLoaderBottomImgCount.setText(String.format(getString(R.string.image_total_sheet), currentImgCount));
        }
    }

    private void backgroundlightOff() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        getWindow().setAttributes(layoutParams);
    }

    private void backgroundlightOn() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 1f;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onImageItemClick(Set<String> selectedImages) {
        showBottomImgCount(selectedImages, mCurrentImgCount);
    }

    @Override
    public void onImageLoaderPopupWindowSelected(ImageFolder imageFolder) {
        if (imageFolder.getDirPath().endsWith(getString(R.string.image_and_video))) {
            dataToView(mTotalImgPaths);
        } else {
            dataToView(imageFolder);
        }
        mImageLoaderPopupWindow.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        MyLog.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(ImageLoaderActivity.this, R.string.no_read_storage_permission, Toast.LENGTH_SHORT).show();
//        MyLog.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog dialog = new AppSettingsDialog.Builder(this).build();
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
        }
    }
}
