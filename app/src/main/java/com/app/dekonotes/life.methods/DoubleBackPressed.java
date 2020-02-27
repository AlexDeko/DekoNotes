package com.app.dekonotes.life.methods;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DoubleBackPressed {

    private static long backPressed;

    public DoubleBackPressed() {
    }

    public static void onBackPressed(Context context, String toast) {

        if (backPressed + 2000 > System.currentTimeMillis()) {
            //эмулируем нажатие на HOME, сворачивая приложение
            Intent endWork = new Intent(Intent.ACTION_MAIN);
            endWork.addCategory(Intent.CATEGORY_HOME);
            endWork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(endWork);
        } else {
            Toast.makeText(context, toast,
                    Toast.LENGTH_SHORT).show();

        }
        backPressed = System.currentTimeMillis();
    }
}
