package com.example.taskmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BoardRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardRoomFragment newInstance(String param1, String param2) {
        BoardRoomFragment fragment = new BoardRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.board_room_fragment, container, false);
        refreshLayout = view.findViewById(R.id.boardrefresh);
        recyclerView = view.findViewById(R.id.boardroomrecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        populateCards();
        //on refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateCards();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void populateCards() {
        if(NavDrawerActivity.PERM.equals("STUD")){
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getBoardRoomItems.php")
                    .addQueryParameter("stud_id",NavDrawerActivity.USER_ID)
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
                        JSONArray jsonArray = new JSONArray(resp);
                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<String> ass = new ArrayList<>();

                        for(int i = 0;i < jsonArray.length();i++){
                            names.add(jsonArray.getJSONObject(i).getString("GROUP_NAME"));
                            ass.add(jsonArray.getJSONObject(i).getString("ASS_NAME"));
                        }
                        BoardRoomAdapter boardRoomAdapter = new BoardRoomAdapter(requireContext(),names,ass);
                        recyclerView.setAdapter(boardRoomAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            //TODO lecturer board room
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getLects.php")
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
                        JSONArray jsonArray = new JSONArray(resp);
                        ArrayList<String> ems = new ArrayList<>();
                        ArrayList<String> perms = new ArrayList<>();

                        for(int i = 0; i < jsonArray.length();i++){
                            ems.add(jsonArray.getJSONObject(i).getString("EMAIL"));
                            perms.add(jsonArray.getJSONObject(i).getString("PERM"));
                        }

                        BoardRoomAdapter boardRoomAdapter = new BoardRoomAdapter(requireContext(),ems,perms);
                        recyclerView.setAdapter(boardRoomAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}