package com.example.ohthmhyh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CERecycleviewAdapter extends RecyclerView.Adapter<CERecycleviewAdapter.Myviewholder> {
    ArrayList<HabitEvent> habitEventsList;
    Context context;

    public  CERecycleviewAdapter(ArrayList<HabitEvent> habitEventsList, Context context){
        this.habitEventsList=habitEventsList;
        this.context=context;
    }
    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_habit_event_list,parent,false);
        Myviewholder holder =new Myviewholder(view);

        return holder;
    }
    //sets the things in display notebook
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null
        holder.Displaycomment.setText("Comment: "+habitEventsList.get(position).getComment());
        holder.DisplayHabit.setText((habitEventsList.get(position).getHabit().toString()));
        holder.ExtraDisplay.setText("Extra: ");

        holder.DisplayLocation.setText("Location: "+(Html.fromHtml(habitEventsList.get(position).getLocatoion().getAddressLine(0))));
        holder.DisplayUserpic.setImageURI(habitEventsList.get(position).getResultUri());

        //holder.DisplayUserpic.Picasso.with(this).load(resultUri).into(pick);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            //Used for editing
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CreateHabitEvent.class);
                intent.putExtra("id",habitEventsList.get(position).getId_number());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habitEventsList.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView Displaycomment;
        TextView DisplayHabit;
        TextView ExtraDisplay;
        TextView DisplayLocation;
        ImageView DisplayUserpic;

        ConstraintLayout parentLayout;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            Displaycomment=itemView.findViewById(R.id.DisplayCommentCE);
            DisplayHabit=itemView.findViewById(R.id.DisplayHabitCE);
            ExtraDisplay=itemView.findViewById(R.id.ExtraDisplayCE);
            DisplayLocation=itemView.findViewById(R.id.DisplayLocationCE);
            DisplayUserpic=itemView.findViewById(R.id.DisplayUserpicCE);
            //This is the name of the contrant layout in display HE list
            parentLayout=itemView.findViewById(R.id.Displayed_HabitEvent_list);
        }
    }

}
