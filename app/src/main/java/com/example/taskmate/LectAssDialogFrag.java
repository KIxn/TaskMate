package com.example.taskmate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LectAssDialogFrag extends DialogFragment {

    CircularProgressIndicator loadingIndicator,MarkIndicator;
    TextView txtGroupName,txtGroupMark,txtInfo;
    EditText edtNewMark;
    Button btnSubmitMark;
    ListView listView;
    LinearLayout MarkAdjlt;
    RelativeLayout entrieslt;
    JSONArray jsonArray;
    int currentGroupPosition;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME,R.style.Theme_TaskMate);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        //TODO
        View view = inflater.inflate(R.layout.fragment_lect_assignment,container,false);
        MaterialToolbar toolbar = view.findViewById(R.id.LectAssToolbar);
        //collect views
        loadingIndicator = view.findViewById(R.id.loadingindicator);
        MarkIndicator = view.findViewById(R.id.LectMarkIndicator);
        txtGroupName = view.findViewById(R.id.txtlectgrpnm);
        txtGroupMark = view.findViewById(R.id.txtlectgrpmk);
        txtInfo = view.findViewById(R.id.txtselectinfo);
        edtNewMark = view.findViewById(R.id.edtLectNewMark);
        btnSubmitMark = view.findViewById(R.id.btnSubmitNewMark);
        listView = view.findViewById(R.id.groupslist);
        MarkAdjlt = view.findViewById(R.id.MarkAdjlt);
        entrieslt = view.findViewById(R.id.justtoshake);

        //back button for toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //onclicks for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MarkAdjlt.setVisibility(View.VISIBLE);
                currentGroupPosition = position;
                selectGroup(position);
            }
        });

        //update mark btn
        btnSubmitMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate inputs
                int mark;
                try {
                    mark = Integer.parseInt(edtNewMark.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getContext(), "Please Re-enter Mark in the range 0-100", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((mark<0) || (mark>100)){
                    Toast.makeText(getContext(), "Please Re-enter Mark in the range 0-100", Toast.LENGTH_SHORT).show();
                    shaker(entrieslt);
                    edtNewMark.setText("");
                    return;
                }
                //continue to update mark
                HttpUrl url = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("updateGroupMark.php")
                        .build();
                RequestBody requestBody = null;
                try {
                    requestBody = new FormBody.Builder()
                            .add("mark", String.valueOf(mark)).add("group_id",jsonArray.getJSONObject(currentGroupPosition).getString("GROUP_ID"))
                            .build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                PhpReq phpReq = new PhpReq();
                phpReq.sendRequest(requireActivity(), request, new RequestHandler() {
                    @Override
                    public void processResponse(String resp) {
                        //Toast.makeText(requireActivity(), resp, Toast.LENGTH_SHORT).show();
                        selectGroup(currentGroupPosition);
                        edtNewMark.setText("");
                    }
                });
            }
        });

        //get groups to populate list
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getGroups.php")
                .addQueryParameter("ass_id",getTag())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        PhpReq phpReq = new PhpReq();
        setList setList = new setList();
        phpReq.sendRequest(requireActivity(),request,setList);

        return view;
    }

    //for errors on sign-up/log-in
    public void shaker(View v){
        Animation animShake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);
        v.startAnimation(animShake);
    }

    //method to update selected group
    private void selectGroup(int position) {
        try {
            MarkIndicator.setVisibility(View.GONE);
            MarkIndicator.setIndeterminate(true);
            MarkIndicator.setVisibility(View.VISIBLE);
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getGroup.php")
                    .addQueryParameter("group_id",jsonArray.getJSONObject(position).getString("GROUP_ID"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            PhpReq phpReq = new PhpReq();
            phpReq.sendRequest(requireActivity(), request, new RequestHandler() {
                @Override
                public void processResponse(String resp) {
                    try {
                        JSONArray arr = new JSONArray(resp);
                        JSONObject jsonObject = arr.getJSONObject(0);
                        txtGroupName.setText(jsonObject.getString("GROUP_NAME"));
                        txtGroupMark.setText(jsonObject.getString("MARK") + "/100");
                        MarkIndicator.setProgressCompat(jsonObject.getInt("MARK"),true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //handles population of list
    class setList implements RequestHandler{

        @Override
        public void processResponse(String resp) {
            try {
                JSONArray array = new JSONArray(resp);
                jsonArray = array;
                if(array.length() > 0){
                    ArrayList<String> groupnames = new ArrayList<>();
                    ArrayList<String> groupdescs = new ArrayList<>();
                    for(int i = 0; i < array.length();i++){
                        groupnames.add(array.getJSONObject(i).getString("GROUP_NAME"));
                        groupdescs.add(array.getJSONObject(i).getString("GROUP_DESC"));
                    }
                    //create adapter
                    ListAdapter listAdapter = new ListAdapter(requireActivity(),groupnames,groupdescs);
                    listView.setAdapter(listAdapter);
                    loadingIndicator.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else{
                    txtInfo.setText("No Groups Exist for this Assignment Just Yet :(");
                    loadingIndicator.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //implementation of list adapter
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
