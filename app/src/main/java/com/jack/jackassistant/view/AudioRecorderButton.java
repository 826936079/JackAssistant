package com.jack.jackassistant.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.jack.jackassistant.R;
import com.jack.jackassistant.manager.AudioManager;
import com.jack.jackassistant.manager.DialogManager;

import java.io.File;

/**
 * Created by Administrator on 2017/4/19.
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStatusListener {

    private static final String TAG = "AudioRecorderButton";

    private static final int STATUS_NORMAL = 1;
    private static final int STATUS_RECORDING = 2;
    private static final int STATUS_WANT_TO_CANCEL = 3;

    private static final int MSG_AUDIO_PREPARED = 100;
    private static final int MSG_VOICE_CHANGE = 101;
    private static final int MSG_DIALOG_DISMISS = 102;

    private static final int DISTANCE_CANCEL_Y = 50;
    private static final int MAX_VOLUME = 7;
    private static final int THREAD_SLEEP_TIME = 100;  //单位ms
    private static final int TOO_SHORT_DIALOG_DISMISS_TIME = 1000;  //单位ms
    private static final float AUDIO_RECORDING_MIN_TIME = 0.6f; //单位s

    private int mCurrentStatus = STATUS_NORMAL;
    private boolean mIsRecording = false;
    private boolean mIsReady = false;

    private float mRecordTime = 0;

    private static final String AUDIO_DIR_NAME = "record_audio";

    private DialogManager mDialogManager;
    private AudioManager mAudioManager;

    private OnAudioFinishRecorderListener mOnAudioFinishRecorderListener;

    public interface OnAudioFinishRecorderListener {
        void audioFinishRecorder(float time, String filePath);
    }

    public void setOnAudioFinishRecorderListener(OnAudioFinishRecorderListener listener) {
        mOnAudioFinishRecorderListener = listener;
    }

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDialogManager = new DialogManager(getContext());
        File dir = getContext().getExternalFilesDir(AUDIO_DIR_NAME);
        mAudioManager = AudioManager.getInstance(dir.getAbsolutePath());

        mAudioManager.setAudioStatusListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mIsReady = true;
                mAudioManager.prepare();
                return false;
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    mIsRecording = true;
                    mDialogManager.showAudioRecorderDialog();

                    new Thread(mGetVolumeRunnable).start();
                    break;
                case MSG_VOICE_CHANGE:
                    mDialogManager.updateVolume(mAudioManager.getVolume(MAX_VOLUME));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismiss();
                    break;
            }
        }
    };

    private Runnable mGetVolumeRunnable = new Runnable() {
        @Override
        public void run() {
            while (mIsReady) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                    mRecordTime += THREAD_SLEEP_TIME / 1000f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeButtonStatus(STATUS_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:
                if (wantCancel(x, y)) {
                    changeButtonStatus(STATUS_WANT_TO_CANCEL);
                    mDialogManager.wantToCancel();
                } else {
                    changeButtonStatus(STATUS_RECORDING);
                    mDialogManager.recording();
                }

                break;

            case MotionEvent.ACTION_UP:
                if (!mIsReady) {
                    reset();
                    return super.onTouchEvent(event);
                } else if (!mIsRecording || mRecordTime < AUDIO_RECORDING_MIN_TIME) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, TOO_SHORT_DIALOG_DISMISS_TIME);
                } else if (mCurrentStatus == STATUS_RECORDING) {
                    mDialogManager.dismiss();
                    mAudioManager.release();
                    if (mOnAudioFinishRecorderListener != null) {
                        mOnAudioFinishRecorderListener.audioFinishRecorder(mRecordTime, mAudioManager.getRecorderFilePath());
                    }
                } else if (mCurrentStatus == STATUS_WANT_TO_CANCEL) {
                    mDialogManager.dismiss();
                    mAudioManager.cancel();
                }
                reset();
                break;

        }


        return super.onTouchEvent(event);
    }

    private void reset() {
        mIsRecording = false;
        mIsReady = false;
        mRecordTime = 0;
        changeButtonStatus(STATUS_NORMAL);
    }

    private boolean wantCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }

        if (y < -DISTANCE_CANCEL_Y || y > getHeight() + DISTANCE_CANCEL_Y) {
            return true;
        }

        return false;
    }

    private void changeButtonStatus(int status) {
        if (mCurrentStatus != status) {
            mCurrentStatus = status;
            switch (status) {
                case STATUS_NORMAL:
                    setBackgroundResource(R.drawable.audio_recorder_button_normal);
                    setText(R.string.audio_recorder_button_normal);
                    break;

                case STATUS_RECORDING:
                    setBackgroundResource(R.drawable.audio_recorder_button_recording);
                    setText(R.string.audio_recorder_button_recording);
                    break;

                case STATUS_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.audio_recorder_button_recording);
                    setText(R.string.audio_recorder_button_want_cancel);
                    break;

            }
        }
    }
}
