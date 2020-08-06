package com.example.t_dal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            this.options = new String[]{"Add a job post","All TAs","TAs applied for job"};
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mytext.setText(options[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return options.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mytext;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mytext = itemView.findViewById(R.id.optionText);
            mainLayout = itemView.findViewById(R.id.rowMainLayout);
        }
    }
}
