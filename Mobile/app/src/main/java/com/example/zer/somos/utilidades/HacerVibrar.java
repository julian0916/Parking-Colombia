package com.example.zer.somos.utilidades;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class HacerVibrar {
    private Context contexto;
    private Vibrator vibrator;

    public HacerVibrar(Context contexto) {
        this.contexto = contexto;
        vibrator = (Vibrator) contexto.getSystemService(contexto.VIBRATOR_SERVICE);
    }

    public void vibrar() {
        if (vibrator == null) {
            return;
        }
        if (!vibrator.hasVibrator()) {
            return;
        }
        long[] pattern = {0, //sleep
                600, 500, 800, 400, 1000, 200};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //vibrator.vibrate(VibrationEffect.createOneShot(segundos * 1000, VibrationEffect.DEFAULT_AMPLITUDE));
            vibrator.vibrate(
                    VibrationEffect.createWaveform(pattern, -1)
            );
        } else {
            vibrator.vibrate(pattern, -1);
        }
    }
}
