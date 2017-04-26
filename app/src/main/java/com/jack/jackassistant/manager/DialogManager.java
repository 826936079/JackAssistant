package com.jack.jackassistant.manager;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.jackassistant.R;


/**
 * Created by Administrator on 2017/4/19.
 */
public class DialogManager {

    private ImageView mDialogAudioRecorderIcon;
    private ImageView mDialogAudioRecorderVolume;
    private TextView mDialogAudioRecorderLabel;

    private Context mContext;
    private Dialog mDialog;

    private static final String AUDIO_RECORDER_VOLUME_PREFIX = "audio_recorder_v";

    public DialogManager(Context context) {
        this.mContext = context;

    }

    public void showAudioRecorderDialog () {

        mDialog = new Dialog(mContext, R.style.AudioDialogTheme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_audio_recorder, null);
        mDialog.setContentView(view);

        mDialogAudioRecorderIcon = (ImageView) view.findViewById(R.id.iv_dialog_audio_recorder_icon);
        mDialogAudioRecorderVolume = (ImageView) view.findViewById(R.id.iv_dialog_audio_recorder_volume);
        mDialogAudioRecorderLabel = (TextView) view.findViewById(R.id.tv_dialog_audio_recorder_label);

        mDialog.show();
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialogAudioRecorderIcon.setVisibility(View.VISIBLE);
            mDialogAudioRecorderVolume.setVisibility(View.VISIBLE);
            mDialogAudioRecorderLabel.setVisibility(View.VISIBLE);

            mDialogAudioRecorderIcon.setImageResource(R.drawable.audio_recorder_recording);
            mDialogAudioRecorderLabel.setText(R.string.audio_recorder_dialog_normal);
        }
    }

    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialogAudioRecorderIcon.setVisibility(View.VISIBLE);
            mDialogAudioRecorderVolume.setVisibility(View.GONE);
            mDialogAudioRecorderLabel.setVisibility(View.VISIBLE);

            mDialogAudioRecorderIcon.setImageResource(R.drawable.audio_recorder_cancel);
            mDialogAudioRecorderLabel.setText(R.string.audio_recorder_button_want_cancel);
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialogAudioRecorderIcon.setVisibility(View.VISIBLE);
            mDialogAudioRecorderVolume.setVisibility(View.GONE);
            mDialogAudioRecorderLabel.setVisibility(View.VISIBLE);

            mDialogAudioRecorderIcon.setImageResource(R.drawable.audio_recorder_voice_to_short);
            mDialogAudioRecorderLabel.setText(R.string.audio_recorder_dialog_to_short);
        }
    }

    public void updateVolume(int level) {
        if (mDialog != null && mDialog.isShowing()) {

            int resId = mContext.getResources().getIdentifier(AUDIO_RECORDER_VOLUME_PREFIX + level, "drawable", mContext.getPackageName());
            mDialogAudioRecorderVolume.setImageResource(resId);
        }
    }

}
