package com.example.taskmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoardRoomAdapter extends RecyclerView.Adapter<BoardRoomAdapter.BoardRoomViewHolder> {

    Context context;
    List<String> group_names;
    List<String> ass_names;

    public BoardRoomAdapter(Context context, List<String> group_names, List<String> ass_names) {
        this.context = context;
        this.group_names = group_names;
        this.ass_names = ass_names;
    }

    @NonNull
    @NotNull
    @Override
    public BoardRoomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_room_card,parent,false);
        return new BoardRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BoardRoomViewHolder holder, int position) {
        holder.txtGroup.setText(group_names.get(position));
        holder.txtAss.setText(ass_names.get(position));
    }

    @Override
    public int getItemCount() {
        return group_names.size();
    }

    public static class BoardRoomViewHolder extends RecyclerView.ViewHolder{

        TextView txtGroup,txtAss;

        public BoardRoomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtGroup = itemView.findViewById(R.id.txt_board_grp_name);
            txtAss = itemView.findViewById(R.id.txt_board_ass);
        }
    }

}
