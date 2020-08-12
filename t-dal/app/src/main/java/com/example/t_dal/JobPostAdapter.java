package com.example.t_dal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.MyViewHolder> {

    Context context;
    List<JobPost> list;
    String click;
    public JobPostAdapter (Context context, List<JobPost> list,String click){
        this.context = context;
        this.list=list;
        this.click = click;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from((context));
        View view = inflater.inflate(R.layout.job_post_row,parent,false);

        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.courseName.setText(list.get(position).getCoursename());
        holder.title.setText(list.get(position).getJobtitle());
        holder.profName.setText(list.get(position).getFullname());
        holder.date.setText(list.get(position).getDate());
        holder.desc.setText(list.get(position).getDescription());

        if(!click.equals("NO")) {
            holder.layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, JobPostPageActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("courseName", list.get(position).getCoursename());
                    intent.putExtra("description", list.get(position).getDescription());
                    intent.putExtra("title", list.get(position).getJobtitle());
                    intent.putExtra("profID", list.get(position).getUserid());
                    intent.putExtra("date", list.get(position).getDate());
                    intent.putExtra("profName", list.get(position).getFullname());

                    context.startActivity(intent);


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView courseName,title, profName,desc,date;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName =itemView.findViewById(R.id.rowCourseName);
            title = itemView.findViewById(R.id.rowJobTitle);
            profName = itemView.findViewById(R.id.rowProfessorName);
            desc = itemView.findViewById(R.id.rowDescription);
            date = itemView.findViewById(R.id.rowDate);
            layout = itemView.findViewById(R.id.job_post_row_layout);
        }
    }

}
