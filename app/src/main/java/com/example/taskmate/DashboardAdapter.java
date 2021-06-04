package com.example.taskmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    //variables
    private Context context;
    private JSONArray jsonArray;
    private FragmentManager dialogFragmentManager;

    //constructor
    public DashboardAdapter(@NotNull Context context,@NotNull JSONArray jsonArray,FragmentManager dialogFragmentManager){
        this.context = context;
        this.jsonArray = jsonArray;
        this.dialogFragmentManager = dialogFragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_card,parent,false);
        return new DashboardViewHolder(v,dialogFragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardViewHolder holder, int position) {
        //set data
        try {
            holder.txtAss.setText(jsonArray.getJSONObject(position).getString("ASS_NAME"));
            holder.txtDate.setText(jsonArray.getJSONObject(position).getString("DUE_DATETIME"));
            holder.setViewcreator_ID(String.valueOf(jsonArray.getJSONObject(position).getInt("CREATOR_ID")));
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
                    Toast.makeText(v.getContext(), txtAss.getText().toString(), Toast.LENGTH_SHORT).show();
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
