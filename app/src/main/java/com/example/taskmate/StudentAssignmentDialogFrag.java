package com.example.taskmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.jetbrains.annotations.NotNull;

public class StudentAssignmentDialogFrag extends DialogFragment {

    //views
    private CircularProgressIndicator circularProgressIndicator,loadingIndicator;
    private TextView txtAssName,txtGroupName,txtMark;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Theme_TaskMate);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_assignment, container, false);
        circularProgressIndicator = view.findViewById(R.id.markIndicator);
        txtAssName = view.findViewById(R.id.txtStudAssName);
        txtGroupName = view.findViewById(R.id.txtStudGroupName);
        txtMark = view.findViewById(R.id.txtStudAssMark);
        MaterialToolbar toolbar = view.findViewById(R.id.StudAssToolbar);

        //set back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close dialogue
                try {
                    getDialog().dismiss();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //TODO complete student assignment view
        //component for internet requests
        PhpReq phpReq = new PhpReq();
        //get group details, check if student is in group
        

        return view;
    }
}