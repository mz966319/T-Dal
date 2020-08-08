package com.example.t_dal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

    String options[],currUserType;
    Context context;


    public OptionsAdapter(Context context, String currUserType){
        this.context=context;
        if(currUserType.equals("Instructor"))
            this.options = new String[]{"Add a job post","Show my job posts","All TAs","TAs applied for job"};
        else if(currUserType.equals("Student"))
            this.options = new String[]{"Available courses","Make appointment","Create TA profile","List of TAs"};
        else
            this.options = new String[]{"Available courses","Make appointment","Create TA profile","List of TAs","Edit TA profile","Show my TA profile","View job posts"};
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.options_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //holder.mytext.setText(options[position]);
        holder.button.setText(options[position]);

//        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent;
                if(position==0 && options[0].equals("Add a job post")){
                    intent = new Intent(context, AddPostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    //context.finish();
                }
                if(position==1 && options[1].equals("Show my job posts")){
                    intent = new Intent(context, JobPostsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return options.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mytext;
        Button button;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.optionsButton);
            mytext = itemView.findViewById(R.id.optionText);
            mainLayout = itemView.findViewById(R.id.rowMainLayout);
        }
    }
}
