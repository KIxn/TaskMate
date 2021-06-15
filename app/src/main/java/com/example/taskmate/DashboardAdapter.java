package com.example.taskmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    //variables
    private Context context;
    private JSONArray jsonArray;
    private static FragmentManager subFragmentManager;

    //constructor
    public DashboardAdapter(@NotNull Context context,@NotNull JSONArray jsonArray,FragmentManager subFragmentManager){
        this.context = context;
        this.jsonArray = jsonArray;
        this.subFragmentManager = subFragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_card,parent,false);
        return new DashboardViewHolder(v, subFragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardViewHolder holder, int position) {
        //set data
        try {
            holder.txtAss.setText(jsonArray.getJSONObject(position).getString("ASS_NAME"));
            holder.txtDate.setText(jsonArray.getJSONObject(position).getString("DUE_DATETIME"));
            holder.setViewcreator_ID(String.valueOf(jsonArray.getJSONObject(position).getInt("CREATOR_ID")));
            holder.setAss_ID(jsonArray.getJSONObject(position).getString("ASS_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder{

        TextView txtAss;
        TextView txtDate;
        private String viewcreator_ID;
        private String Ass_ID;

        public void setAss_ID(String ass_ID) {
            Ass_ID = ass_ID;
        }

        public void setViewcreator_ID(String viewcreator_ID) {
            this.viewcreator_ID = viewcreator_ID;//works
        }

        public DashboardViewHolder(@NonNull @NotNull View itemView,FragmentManager dialogFragmentManager) {
            super(itemView);
            //store frag manager for deletes etc
            //get needed views
            txtAss = itemView.findViewById(R.id.txtAssignment);
            txtDate = itemView.findViewById(R.id.txtDate);
            TextView txtusr = new TextView(itemView.getContext());
            //onclicks to open assignments
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO ONCLICKS for Assignment cards
                    if(NavDrawerActivity.PERM.equals("STUD")){
                        //if user is a student display student assignment view
                        //TODO code up STUD assignment page
                        //create frag and pass Ass_ID
                        new StudentAssignmentDialogFrag().show(
                                dialogFragmentManager,Ass_ID
                        );
                    }
                    else{
                        //show lecturer assignment view
                        new LectAssDialogFrag().show(
                                dialogFragmentManager,Ass_ID
                        );
                    }
                }
            });

            //onlongclick for deletion
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //if lect
                    if(viewcreator_ID.equals(NavDrawerActivity.USER_ID)){
                        new DeleteAssignmentDialogFrag().show(
                                dialogFragmentManager,txtAss.getText().toString()
                        );
                    }else{
                        Toast.makeText(v.getContext(), "You do not have Permission to Edit Assignments", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }

}
