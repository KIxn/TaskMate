package com.example.taskmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class DeleteReminder extends DialogFragment {


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.ConfirmAssDelete))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete reminder
                        DBReminders db = new DBReminders(requireContext());
                        boolean flag = db.deleteReminder(getTag());
                        if(flag){
                            Toast.makeText(requireContext(), "Reminder Deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(requireContext(), "FATAL INTERNAL DATABASE ERROR", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(requireContext(), "Reminder Unaffected", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

}
