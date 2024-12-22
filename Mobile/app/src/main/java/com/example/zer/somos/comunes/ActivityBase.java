package com.example.zer.somos.comunes;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class ActivityBase extends AppCompatActivity {

    private boolean volverACargarVentanaAlSalir = true;

    protected void permitirSalir() {
        volverACargarVentanaAlSalir = false;
    }

    protected void noPuedeSalir() {
        volverACargarVentanaAlSalir = true;
    }

    protected void salirPantalla() {
        try {
            permitirSalir();
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();

            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            noPuedeSalir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        try {
            if (!volverACargarVentanaAlSalir) {
                return;
            }
            Intent notificationIntent = this.getIntent();//new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* @Override
    protected void onStart() {
        super.onStart();
        // start lock task mode if it's not already active
        ActivityManager am = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        // ActivityManager.getLockTaskModeState api is not available in pre-M.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!am.isInLockTaskMode()) {
                startLockTask();
            }
        } else {
            if (am.getLockTaskModeState() ==
                    ActivityManager.LOCK_TASK_MODE_NONE) {
                startLockTask();
            }
        }
    }*/

}
