package com.example.abc.app;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by xiaoming on 2015/10/9.
 */
public class SearchPhoneService extends Service {
    MediaPlayer mediaPlayer=null;
    private  boolean stop=false;
    private  boolean play=false;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try {
            mediaPlayer.setDataSource(this, alert);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean isstop=intent.getBooleanExtra("isstop",false);
        stop=intent.getBooleanExtra("stop",true);
        Log.v("xiaoming", "isstop ="+isstop);
            if(!isstop)
            {
                    Log.v("xiaoming", "start play music");
                    mediaPlayer.start();
                    play=true;
                    stop=false;
            }else
            {
                    if(mediaPlayer.isPlaying())
                    {
                        Log.v("xiaoming", "stop:"+stop);
                        if(!stop)
                        {
                            mediaPlayer.pause();
                            stop=true;
                        }
                    }
            }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
