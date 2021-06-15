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

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnounceViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private FragmentManager fragmentManager;

    public AnnouncementsAdapter(Context context, JSONArray jsonArray, FragmentManager fragmentManager){
        this.context = context;
        this.jsonArray = jsonArray;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public AnnounceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_card,parent,false);
        return new AnnounceViewHolder(view,fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AnnounceViewHolder holder, int position) {
        try {
            holder.txtTopic.setText(jsonArray.getJSONObject(position).getString("TOPIC"));
            holder.txtDesc.setText(jsonArray.getJSONObject(position).getString("ANNOUNCE_DESC"));
            holder.txtDate.setText(jsonArray.getJSONObject(position).getString("ANNOUNCE_DATETIME"));
            holder.setAnnounce_id(jsonArray.getJSONObject(position).getString("ANNOUNCE_ID"));
            holder.setCreator_id(jsonArray.getJSONObject(position).getString("CREATOR_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class AnnounceViewHolder extends RecyclerView.ViewHolder{
        TextView txtTopic,txtDesc,txtDate;
        String announce_id,creator_id;

        public void setAnnounce_id(String announce_id) {
            this.announce_id = announce_id;
        }

        public void setCreator_id(String creator_id) {
            this.creator_id = creator_id;
        }

        public AnnounceViewHolder(@NonNull @NotNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            txtTopic = itemView.findViewById(R.id.txtAnnounceTopic);
            txtDesc = itemView.findViewById(R.id.txtAnnounceDesc);
            txtDate = itemView.findViewById(R.id.txtAnnounceDate);

            //TODO deletion if lecturer
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(NavDrawerActivity.USER_ID.equals(creator_id)){
                        new DeleteAnnounceDialog().show(
                                fragmentManager,announce_id
                        );
                    }else{
                        Toast.makeText(itemView.getContext(), "You do not have Permissions to delete Announcement", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }

}
