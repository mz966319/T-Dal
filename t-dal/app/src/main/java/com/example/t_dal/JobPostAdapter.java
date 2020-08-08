package com.example.t_dal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.MyViewHolder> {

    Context context;
    List<JobPost> list;
    public JobPostAdapter (Context context, List<JobPost> list){
        this.context = context;
        this.list=list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from((context));
        View view = inflater.inflate(R.layout.job_post_row,parent,false);

        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.courseName.setText(list.get(position).getCoursename());
        holder.title.setText(list.get(position).getJobtitle());
        holder.profName.setText(list.get(position).getFullname());
        holder.date.setText(list.get(position).getDate());
        holder.desc.setText(list.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView courseName,title, profName,desc,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName =itemView.findViewById(R.id.rowCourseName);
            title = itemView.findViewById(R.id.rowJobTitle);
            profName = itemView.findViewById(R.id.rowProfessorName);
            desc = itemView.findViewById(R.id.rowDescription);
            date = itemView.findViewById(R.id.rowDate);
        }
    }

}
