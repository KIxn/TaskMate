package com.example.taskmate;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemindersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RemindersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemindersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemindersFragment newInstance(String param1, String param2) {
        RemindersFragment fragment = new RemindersFragment();
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
    FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;
    DBReminders dbhelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reminders_fragment, container, false);
        recyclerView = view.findViewById(R.id.reminderrecycler);
        floatingActionButton = view.findViewById(R.id.add_reminder);
        swipeRefreshLayout = view.findViewById(R.id.reminderrefresh);
        dbhelper = new DBReminders(requireContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        populateCards();

        //ADD REMINDER BUTTON
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddRemDialogFrag().show(
                        getChildFragmentManager(),"Adding Reminder"
                );
            }
        });

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

        Cursor result = dbhelper.getReminders();
        if(result.getCount() == 0){
            Toast.makeText(requireContext(), "No Reminders to show :)", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ArrayList<String> topics = new ArrayList<>();
            ArrayList<String> desc = new ArrayList<>();
            ArrayList<String> dates = new ArrayList<>();

            while (result.moveToNext()){
                topics.add(result.getString(0));
                desc.add(result.getString(1));
                dates.add(result.getString(2));
            }

            ReminderAdapter reminderAdapter = new ReminderAdapter(requireContext(),topics,desc,dates,getChildFragmentManager());
            recyclerView.setAdapter(reminderAdapter);
        }

    }
}