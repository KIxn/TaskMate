package com.example.taskmate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddAssDialogFrag extends DialogFragment {

    private EditText txtAssName;
    private EditText txtAssDesc;
    private TextView mActionOk;
    private TextView mActionCancel;
    private Button selectDate;
    private String date = "";
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        //create dialogue layout
        View view = inflater.inflate(R.layout.dialogaddass,container,false);
        //assign text views
        txtAssName =  view.findViewById(R.id.txtassName);
        txtAssDesc = view.findViewById(R.id.txtassDesc);
        mActionOk = view.findViewById(R.id.txtassOk);
        mActionCancel = view.findViewById(R.id.txtassCancel);
        selectDate = view.findViewById(R.id.btnsetDueDate);
        //save context for reference
        Context context = getContext();

        //set datepickkerlistener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
                date = String.valueOf(dayOfMonth) + " " + smonth + " " + String.valueOf(year);
            }
        };

        //setdate
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get default dates
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        dateSetListener,
                        year,month,day);

                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Assignment Creation Cancelled", Toast.LENGTH_SHORT).show();
                try {
                    getDialog().dismiss();
                }catch (Exception e ){
                    e.printStackTrace();
                }
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post data to server
                String Ass_Name = txtAssName.getText().toString();
                String Ass_Desc = txtAssDesc.getText().toString();
                String Creator_ID = NavDrawerActivity.USER_ID;
                //check if form is complete
                if((Ass_Name.length() != 0) && (Ass_Desc.length() != 0) && (date.length() != 0)){
                    //post assignment
                    PhpReq phpReq = new PhpReq();
                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("lamp.ms.wits.ac.za")
                            .addPathSegment("home").addPathSegment("s2307935").addPathSegment("createAssignment.php")
                            .build();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("ass_name",Ass_Name).add("ass_desc",Ass_Desc).add("creator_id",Creator_ID).add("due_datetime",date)
                            .build();
                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .post(requestBody)
                            .build();

                    //perform http request
                    phpReq.sendRequest(getActivity(), request,
                            new RequestHandler() {
                                @Override
                                public void processResponse(String resp) {
                                    Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    //close dialogue
                    try {
                        getDialog().dismiss();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }//form not complete
                else{
                    Toast.makeText(context, "Please Complete Assignment Creation Form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
