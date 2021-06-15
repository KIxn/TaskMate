package com.example.taskmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnnouncementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnouncementsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnnouncementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncementsFragment newInstance(String param1, String param2) {
        AnnouncementsFragment fragment = new AnnouncementsFragment();
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

    //views to use
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AnnouncementsAdapter announcementsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.announcements_fragment, container, false);
        swipeRefreshLayout = view.findViewById(R.id.announceswipelt);
        fab = view.findViewById(R.id.add_announcement);
        recyclerView = view.findViewById(R.id.Announcements_recucler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        populateCards();

        if(NavDrawerActivity.PERM.equals("LECT")){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO create new announcement dialogue
                    new AddAnnDialogFrag().show(
                            getChildFragmentManager(),"Making Dialog"
                    );
                }
            });
        }else{
            fab.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateCards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void populateCards() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getAnnouncements.php")
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
                    announcementsAdapter = new AnnouncementsAdapter(requireContext(),jsonArray,getChildFragmentManager());
                    recyclerView.setAdapter(announcementsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}