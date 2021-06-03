package com.example.taskmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    //variables
    private Context context;
    private JSONArray jsonArray;

    //constructor
    public DashboardAdapter(@NotNull Context context,@NotNull JSONArray jsonArray){
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @NotNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_card,parent,false);
        return new DashboardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardViewHolder holder, int position) {
        //set data
        try {
            holder.txtAss.setText(jsonArray.getJSONObject(position).getString("ASS_NAME"));
            holder.txtDate.setText(jsonArray.getJSONObject(position).getString("DUE_DATETIME"));
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

        public DashboardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //get needed views
            txtAss = itemView.findViewById(R.id.txtAssignment);
            txtDate = itemView.findViewById(R.id.txtDate);

            //TODO ONCLICKS for Assignment cards
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), txtAss.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
