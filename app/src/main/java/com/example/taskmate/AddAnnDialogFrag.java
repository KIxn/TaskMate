package com.example.taskmate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AddAnnDialogFrag extends DialogFragment {

    EditText edtTopic,edtDesc;
    TextView mCancel,mOk;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addann_layout,container,false);
        edtDesc = view.findViewById(R.id.edtannDesc);
        edtTopic = view.findViewById(R.id.edtannTopic);
        mCancel = view.findViewById(R.id.txtannCancel);
        mOk = view.findViewById(R.id.txtannOk);
        Context context = getContext();

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = edtTopic.getText().toString();
                String desc = edtDesc.getText().toString();

                if((topic.length() > 0) && (desc.length() > 0)){
                    //request to post data
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("https")
                            .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("addAnn.php")
                            .build();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("creator_id",NavDrawerActivity.USER_ID).add("topic",topic).add("desc",desc).add("date",getDate())
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    PhpReq phpReq = new PhpReq();
                    phpReq.sendRequest(getActivity(), request, new RequestHandler() {
                        @Override
                        public void processResponse(String resp) {
                            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    });
                }else{
                    Toast.makeText(context, "Please Enter an Assignment Topic as well as its Description", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Announcement Creation Cancelled", Toast.LENGTH_SHORT).show();
                try {
                    getDialog().dismiss();
                }catch (Exception e ){
                    e.printStackTrace();
                }
            }
        });

        return view;
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
