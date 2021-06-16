package com.example.taskmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderHolder> {

    private Context context;
    private List<String> Topics;
    private List<String> Descs;
    private List<String> Dates;
    private FragmentManager fragmentManager;
    DBReminders dbhelper;

    public ReminderAdapter(Context context, List<String> topics, List<String> descs, List<String> dates, FragmentManager fragmentManager) {
        this.context = context;
        Topics = topics;
        Descs = descs;
        Dates = dates;
        this.fragmentManager = fragmentManager;
        //inistialise dbhelper
        dbhelper = new DBReminders(context);
    }

    @NonNull
    @NotNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_card,parent,false);
        return new ReminderHolder(view,fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReminderHolder holder, int position) {
        holder.txtTopic.setText(Topics.get(position));
        holder.txtDate.setText(Dates.get(position));
        holder.txtDesc.setText(Descs.get(position));
    }

    @Override
    public int getItemCount() {
        return Topics.size();
    }

    public static class ReminderHolder extends RecyclerView.ViewHolder{
        TextView txtTopic,txtDesc,txtDate;
        FragmentManager fragmentManager;

        public ReminderHolder(@NonNull @NotNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            this.fragmentManager = fragmentManager;
            txtTopic = itemView.findViewById(R.id.txtReminderTopic);
            txtDesc = itemView.findViewById(R.id.txtReminderDesc);
            txtDate = itemView.findViewById(R.id.txtReminderDate);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new DeleteReminder().show(
                            fragmentManager,txtTopic.getText().toString()
                    );
                    return true;
                }
            });

        }
    }
}
