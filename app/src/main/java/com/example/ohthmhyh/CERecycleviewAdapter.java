package com.example.ohthmhyh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.entities.HabitEvent;

/**
 * A simple Recycler view Adapter used to populate the recycler view on the habit event screen
 */
public class CERecycleviewAdapter extends RecyclerView.Adapter<CERecycleviewAdapter.Myviewholder>
        implements CeitemHelpToucherAdapter{
    HabitEventList habitEventsList;
    Context context;
    ItemTouchHelper mTouchhelper;
    OntouchListener mOntouchListener;

    /**
     * @param habitEventsList A array of habit event
     * @param context Context from the activity
     * @param mOntouchListener A thing that does touch actions
     * The CERecycleviewAdapter creater Needs and array, context and a touch Listener
     */
    public  CERecycleviewAdapter(HabitEventList habitEventsList, Context context, OntouchListener mOntouchListener){
        this.habitEventsList=habitEventsList;
        this.context=context;
        this.mOntouchListener=mOntouchListener;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_habit_event_list,parent,false);
        Myviewholder holder =new Myviewholder(view, mOntouchListener);

        return holder;
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null
        holder.Displaycomment.setText("Comment: "+habitEventsList.getHabitEvent(position).getComment());
        holder.DisplayHabit.setText(String.valueOf(habitEventsList.getHabitEvent(position).getHabitUHID()));
        holder.ExtraDisplay.setText("Extra: ");
        if (habitEventsList.getHabitEvent(position).getLatitude()==null
            || habitEventsList.getHabitEvent(position).getLongitude()==null){
            holder.DisplayLocation.setText("Location: Na");
        } else{
            HabitEvent habitEvent = habitEventsList.getHabitEvent(position);
            holder.DisplayLocation.setText("lat: "+habitEvent.getLatitude()+ "Lon: "+ habitEvent.getLongitude());
        }


        habitEventsList.getHabitEvent(position).getBitmapPic(new HabitEvent.BMPcallback() {
            @Override
            public void onBMPcallback(Bitmap bitmap) {
                holder.DisplayUserpic.setImageBitmap(bitmap);
            }
        });

        //holder.DisplayUserpic.Picasso.with(this).load(resultUri).into(pick);
    }

    /**
    *Returns the amount of items in the Recycleview
     * @return habitEventsList.size()
     */
    @Override
    public int getItemCount() {
        return habitEventsList.size();
    }


    /**
     * This is used for moving items in the RecyclerView
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where we move the item to
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        habitEventsList.moveHabit(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * This is used for deleting items in the RecyclerView
     * @param position the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        openDialog(position);
    }


    public void setTouchhelper(ItemTouchHelper touchhelper){
        this.mTouchhelper=touchhelper;
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView Displaycomment;
        TextView DisplayHabit;
        TextView ExtraDisplay;
        TextView DisplayLocation;
        ImageView DisplayUserpic;
        GestureDetector mGestureDetector;
        ConstraintLayout parentLayout;
        OntouchListener ontouchListener;
        public Myviewholder(@NonNull View itemView,OntouchListener ontouchListener) {
            super(itemView);
            Displaycomment=itemView.findViewById(R.id.DisplayCommentCE);
            DisplayHabit=itemView.findViewById(R.id.DisplayHabitCE);
            ExtraDisplay=itemView.findViewById(R.id.ExtraDisplayCE);
            DisplayLocation=itemView.findViewById(R.id.DisplayLocationCE);
            DisplayUserpic=itemView.findViewById(R.id.DisplayUserpicCE);
            //This is the name of the contrant layout in display HE list
            parentLayout=itemView.findViewById(R.id.Displayed_HabitEvent_list);
            mGestureDetector=new GestureDetector(itemView.getContext(),this);

            this.ontouchListener= ontouchListener;

            itemView.setOnTouchListener(this);

        }
        /**
         *These are all possible motions a user can do
         * With the corresponding actions
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            ontouchListener.onItemclicked(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchhelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }


    }
    public interface OntouchListener{
     /**
     *This method is used to goto the edit screen
     * @param position the position of the list we want to edit
     */
        void onItemclicked(int position);
    }
    /**
     *This method is used to open a conformation screen with the user before a delete
     * @param position the position of the item being swiped
     * The reason why it is like this is because dialogs are asynchronous so if the delete method is outside
     * it will be ran before the user input, this way makes it so the user input does something
     */
    public void openDialog(int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("This will change the score of the associated Habit. Continue?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    //If user wants to delete and has confirmed run this code with deletes the habit
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        habitEventsList.removeHabitEvent(position);
                        // TODO: Update the score of the associated Habit.
                        notifyItemRemoved(position);

                    }
                });
        //If the user hits no they dont want to delete run this code
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        notifyItemChanged(position);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
