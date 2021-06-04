package com.example.taskmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    //
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "USER_ID";
    private static final String ARG_PARAM2 = "PERM";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DashboardAdapter dashboardAdapter;
    private FragmentManager childFragmentManager;
    private FloatingActionButton addAssignmentbtn;

    //
    private String USER_ID;
    private String PERM;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    //
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
            USER_ID = getArguments().getString(ARG_PARAM1);
            PERM = getArguments().getString(ARG_PARAM2);
        }
    }

    //method to populate recyclyer
    public void populateCards(){
        //get Assignments as JSON
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getAssignments.php")
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        PhpReq phpReq = new PhpReq();
        phpReq.sendRequest(getActivity(), request, new RequestHandler() {
            @Override
            public void processResponse(String resp) {
                //TODO populate fragment: (set cardviews to => android:stateListAnimator="@null" => so cards dont peak through fragments)
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(resp);
                    //set up recyclerview so that it displays assignment cards
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    //pass along array to adapter
                    dashboardAdapter = new DashboardAdapter(getContext(),jsonArray,childFragmentManager);
                    recyclerView.setAdapter(dashboardAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        //use view.fvb
        //initialize
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dashboard_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.dash_recycler);
        addAssignmentbtn = (FloatingActionButton) view.findViewById(R.id.add_assignment);//TODO add assignment if lect
        //set child frag manager for dialogs
        childFragmentManager = getChildFragmentManager();
        //method to populate recyclerview
        populateCards();
        //set refresh for dashboard => repopulate with cardviews
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateCards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }
}