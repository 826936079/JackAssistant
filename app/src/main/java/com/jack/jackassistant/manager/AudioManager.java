package com.jack.jackassistant.manager;

import android.media.MediaRecorder;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2017/4/24.
 */
public class AudioManager {

    private static AudioManager mInstance;
    private String mDir;
    private String mCurrentFilePath;
    private MediaRecorder mMediaRecorder;
    private AudioStatusListener mAudioStatusListener;
    private boolean isPrepared;

    private static final String MEDIA_RECORDER_SUFFIX = ".amr";

    public interface AudioStatusListener {
        void wellPrepared();
    }

    public void setAudioStatusListener(AudioStatusListener audioStatusListener) {
        mAudioStatusListener = audioStatusListener;
    }

    private AudioManager(String dir) {
        mDir = dir;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public void prepare() {
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, generateFileName());
            mCurrentFilePath = file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(mCurrentFilePath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            isPrepared = true;
            if (mAudioStatusListener != null) {
                mAudioStatusListener.wellPrepared();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString() + MEDIA_RECORDER_SUFFIX;
    }

    public void release() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            if (file.exists()) {
                file.delete();
            }
            mCurrentFilePath = null;
        }
    }

    public int getVolume(int maxLevel) {
        int volume = 1;
        if (isPrepared) {
            try {
                // mMediaRecorder.getMaxAmplitude() 1~32767
                volume = maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return volume;
    }

    public String getRecorderFilePath() {
        return mCurrentFilePath;
    }
}
