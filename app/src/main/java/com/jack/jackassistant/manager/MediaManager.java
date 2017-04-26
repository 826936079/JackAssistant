package com.jack.jackassistant.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by xiaofeng on 2017/4/26.
 */

public class MediaManager {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;

    public static void playSound(String filaPath, MediaPlayer.OnCompletionListener listener) {

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }

        mMediaPlayer.setOnCompletionListener(listener);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mMediaPlayer.reset();
                return false;
            }
        });
        try {
            mMediaPlayer.setDataSource(filaPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
