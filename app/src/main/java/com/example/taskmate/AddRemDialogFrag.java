package com.example.taskmate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddRemDialogFrag extends DialogFragment {

    EditText edtTopic,edtDesc;
    TextView mCancel,mOk;
    DBReminders dbhelper;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addrem_layt,container,false);
        edtTopic = view.findViewById(R.id.edtremTopic);
        edtDesc = view.findViewById(R.id.edtremDesc);
        mOk = view.findViewById(R.id.txtaddremOk);
        mCancel = view.findViewById(R.id.txtaddremCancel);
        Context context = requireContext();
        dbhelper = new DBReminders(context);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = edtTopic.getText().toString();
                String desc = edtDesc.getText().toString();

                if((topic.length() > 0) && (desc.length() > 0)){
                    //add to reminders.db
                    boolean flag = dbhelper.insertReminder(topic,desc,getDate());
                    if(flag){
                        Toast.makeText(context, "Reminder Added,refresh page if needed", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }else{
                        Toast.makeText(context, "FATAL INTERNAL DATABASE ERROR", Toast.LENGTH_LONG).show();
                        getDialog().dismiss();
                    }
                }else{
                    shaker(view);
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancelled Operation :(", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        return view;
    }

    //for errors on sign-up/log-in
    public void shaker(View v){
        Animation animShake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);
        v.startAnimation(animShake);
    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String smonth = null;

        switch(month){
            case 0:
                smonth = "JAN";
                break;
            case 1:
                smonth = "FEB";
                break;
            case 2:
                smonth = "MAR";
                break;
            case 3:
                smonth = "APR";
                break;
            case 4:
                smonth = "MAY";
                break;
            case 5:
                smonth = "JUN";
                break;
            case 6:
                smonth = "JUL";
                break;
            case 7:
                smonth = "AUG";
                break;
            case 8:
                smonth = "SEP";
                break;
            case 9:
                smonth = "OCT";
                break;
            case 10:
                smonth = "NOV";
                break;
            case 11:
                smonth = "DEC";
                break;
            default:
                break;
        }
        return String.valueOf(day) + " " + smonth + " " + String.valueOf(year);
    }
}
