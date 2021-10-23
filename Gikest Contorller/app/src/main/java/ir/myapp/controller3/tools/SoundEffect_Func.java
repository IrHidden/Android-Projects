package ir.myapp.controller3.tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import ir.myapp.controller3.R;

public class SoundEffect_Func {
    private static SoundPool soundPool;
    private static int pressSound;


    public SoundEffect_Func(Context cont)
    {
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        pressSound= soundPool.load(cont, R.raw.press_sound_wav, 1);
    }
    public void PlayPressSound()
    {
        soundPool.play(pressSound,1.0f,1.0f,1,0,1.0f);
    }
}
