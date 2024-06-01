package com.example.kursovaya;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class PurchaseConfirmationDialog {

    public static void show(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ГОТОВО!")
                .setMessage(message)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Здесь можно добавить код, который нужно выполнить после подтверждения покупки
                        dialog.dismiss(); // Закрываем диалог
                    }
                })
                .show();
    }
}
