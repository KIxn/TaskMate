package com.example.taskmate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class StudentAssignmentDialogFrag extends DialogFragment {

    //views
    private CircularProgressIndicator circularProgressIndicator,loadingIndicator;
    private TextView txtAssName,txtGroupName,txtMark,txtDesc,txtDate;
    private EditText edtGrpName,edtGrpDesc;
    private LinearLayout ContentLayout;
    private Button btnCreate,btnLeave,btnJoin,btnAddGroup;
    private ListView listView;
    RelativeLayout AddGrouplt;
    LinearLayout JoinGrouplt;
    View view;
    private String group_id;
    JSONArray groupls;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Theme_TaskMate);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_assignment, container, false);
        circularProgressIndicator = view.findViewById(R.id.markIndicator);
        txtDesc = view.findViewById(R.id.studassdesc);
        txtDate = view.findViewById(R.id.txtdate);
        txtAssName = view.findViewById(R.id.txtStudAssName);
        txtGroupName = view.findViewById(R.id.txtStudGroupName);
        ContentLayout = view.findViewById(R.id.StudAssLt);
        txtMark = view.findViewById(R.id.txtStudAssMark);
        loadingIndicator = view.findViewById(R.id.StudAssLoading);
        MaterialToolbar toolbar = view.findViewById(R.id.StudAssToolbar);
        btnCreate = view.findViewById(R.id.btnCreate);
        btnLeave = view.findViewById(R.id.btnLeave);
        btnJoin = view.findViewById(R.id.btnJoin);
        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        edtGrpDesc = view.findViewById(R.id.txtGroupDesc);
        edtGrpName = view.findViewById(R.id.txtGroupName);
        AddGrouplt = view.findViewById(R.id.newgrplt);
        JoinGrouplt = view.findViewById(R.id.joingrplt);
        listView = view.findViewById(R.id.grouplist);

        //create groups
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGrouplt.setVisibility(View.VISIBLE);
            }
        });
        //onclick to add group
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinGrouplt.setVisibility(View.GONE);
                String grpName = edtGrpName.getText().toString();
                String grpDesc = edtGrpDesc.getText().toString();

                //if valid then insert
                if((grpName.length() > 0) && (grpDesc.length() > 0)){
                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("createGroup.php")
                            .build();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("group_name",grpName).add("group_desc",grpDesc).add("stud_id",NavDrawerActivity.USER_ID).add("ass_id",getTag())
                            .build();
                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .post(requestBody)
                            .build();
                    PhpReq phpReq = new PhpReq();
                    phpReq.sendRequest(getActivity(), request, new RequestHandler() {
                        @Override
                        public void processResponse(String resp) {
                            Toast.makeText(getActivity(), "Group Created! Please re-open Assignment overview page", Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                        }
                    });
                }
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set components for request
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("leaveGroup.php")
                        .addQueryParameter("group_id",group_id).addQueryParameter("stud_id",NavDrawerActivity.USER_ID)
                        .build();
                Request request = new Request.Builder()
                        .url(httpUrl)
                        .get()
                        .build();
                PhpReq phpReq = new PhpReq();
                phpReq.sendRequest(getActivity(), request, new RequestHandler() {
                    @Override
                    public void processResponse(String resp) {
                        Toast.makeText(getActivity(), resp, Toast.LENGTH_LONG).show();
                        getDialog().dismiss();
                    }
                });
            }
        });

        //TODO makebutton reactions
        //set join group --> show list
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGrouplt.setVisibility(View.GONE);
                //get data from server
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getGroups.php")
                        .addQueryParameter("ass_id",getTag())
                        .build();
                Request request = new Request.Builder()
                        .url(httpUrl)
                        .get()
                        .build();
                PhpReq phpReq = new PhpReq();
                phpReq.sendRequest(getActivity(), request, new RequestHandler() {
                    @Override
                    public void processResponse(String resp) {
                        try {
                            groupls = new JSONArray(resp);
                            //populate list
                            if(groupls.length() > 0){
                                ArrayList<String> names = new ArrayList<>();
                                ArrayList<String> desc = new ArrayList<String>();

                                for(int i = 0; i < groupls.length();i++){
                                    names.add(groupls.getJSONObject(i).getString("GROUP_NAME"));
                                    desc.add(groupls.getJSONObject(i).getString("GROUP_DESC"));
                                }
                                //convert to String[]



                                ListAdapter listAdapter = new ListAdapter(requireActivity(),names,desc);//not calling listadapter
                                listView.setAdapter(listAdapter);
                            }
                            else{
                              TextView txtInfo = view.findViewById(R.id.txtgrouplistinfo);
                              txtInfo.setText("No Groups Available To Join -> Please 'Create' a group or ask your group leader to do so...");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                JoinGrouplt.setVisibility(View.VISIBLE);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add user to group
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("addUserToGroup.php")
                        .build();
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("group_id",groupls.getJSONObject(position).getString("GROUP_ID")).add("stud_id",NavDrawerActivity.USER_ID)
                            .build();
                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .post(requestBody)
                            .build();
                    PhpReq phpReq = new PhpReq();
                    phpReq.sendRequest(requireActivity(), request, new RequestHandler() {
                        @Override
                        public void processResponse(String resp) {
                            Toast.makeText(requireActivity(), resp, Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
        //component for internet requests
        PhpReq phpReq = new PhpReq();
        //get group details, check if student is in group
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("checkHasGroup.php")
                .addQueryParameter("ass_id",getTag()).addQueryParameter("stud_id",NavDrawerActivity.USER_ID)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        //send request
        handleUI handleUIobj = new handleUI();
        phpReq.sendRequest(getActivity(),request,handleUIobj);

//        ContentLayout.setVisibility(View.VISIBLE);
//        loadingIndicator.setVisibility(View.GONE);

        return view;
    }
    private class handleUI implements RequestHandler{

        @Override
        public void processResponse(String resp) {

            try {
                JSONArray jsonArray = new JSONArray(resp);
                if(jsonArray.length() > 0){
                    //when user has group
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    txtAssName.setText(jsonObject.getString("ASS_NAME"));
                    txtGroupName.setText(txtGroupName.getText().toString() + jsonObject.getString("GROUP_NAME"));
                    txtMark.setText(txtMark.getText().toString() + jsonObject.getString("MARK") + "/100");
                    txtDesc.setText(jsonObject.getString("ASS_DESC"));
                    txtDate.setText("ASSIGNMENT DUE: "+jsonObject.getString("ASS_DUE"));
                    group_id = jsonObject.getString("GROUP_ID");
                    circularProgressIndicator.setProgressCompat(Integer.parseInt(jsonObject.getString("MARK")),true);

                    //show stuff
                    btnLeave.setVisibility(View.VISIBLE);
                    ContentLayout.setVisibility(View.VISIBLE);
                    loadingIndicator.setVisibility(View.GONE);
                }else{
                    //TODO set up create group/join group
                    ContentLayout.setVisibility(View.VISIBLE);
                    loadingIndicator.setVisibility(View.GONE);
                    btnCreate.setVisibility(View.VISIBLE);
                    btnJoin.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ListAdapter extends ArrayAdapter<String>{
        Activity context;
        ArrayList<String> names;
        ArrayList<String> Desc;

        ListAdapter(Activity activity,ArrayList<String> Names,ArrayList<String> Desc ){
            super(activity,R.layout.join_group_row,Names);
            this.context = activity;
            this.names = Names;
            this.Desc = Desc;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //set up row components
            View row = LayoutInflater.from(context).inflate(R.layout.join_group_row,parent,false);
            TextView txtGrpName = row.findViewById(R.id.txtlistgrpname);
            TextView txtGRpDesc = row.findViewById(R.id.txtlistgrpdesc);
            //set row component info
            txtGrpName.setText(names.get(position));
            txtGRpDesc.setText(Desc.get(position));

            return row;
        }
    }
}

