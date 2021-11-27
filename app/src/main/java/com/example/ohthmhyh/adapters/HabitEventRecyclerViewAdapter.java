package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.interfaces.ItemTransportable;

import java.util.ArrayList;

/**
 * An adapter used for putting habitEvent objects into elements of a RecyclerView.
 */
public class HabitEventRecyclerViewAdapter extends RecyclerView.Adapter<HabitEventRecyclerViewAdapter.Myviewholder>
        implements ItemTransportable {

    private Context context;
    private ItemTouchHelper mTouchhelper;
    private OntouchListener mOntouchListener;
    private ArrayList<HabitEvent> content;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

    /**
     * Constructor for an adapter capable of putting habits into a RecyclerView.
     * @param content A array of habit event
     * @param context Context from the activity
     * @param mOntouchListener A thing that does touch actions
     */
    public HabitEventRecyclerViewAdapter(ArrayList<HabitEvent> content, Context context,
                                         OntouchListener mOntouchListener){
        this.content=content;
        this.context=context;
        this.mOntouchListener=mOntouchListener;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_event,parent,false);
        Myviewholder holder =new Myviewholder(view, mOntouchListener);

        return holder;
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because some things might be null
        HabitEvent habitEvent = content.get(position);
        holder.Displaycomment.setText(Html.fromHtml("<i>Comment:</i> " + habitEvent.getComment()));
        holder.DisplayHabit.setText(String.valueOf(habitEvent.getHabitUHID()));
        holder.DisplayLocation.setText(
                Html.fromHtml("<i>Location:</i> " + habitEvent.locationString(holder.itemView.getContext())));

        habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
            @Override
            public void onBMPcallback(Bitmap bitmap) {
                holder.DisplayUserpic.setImageBitmap(bitmap);
            }
        });


    }

    /**
    *Returns the amount of items in the Recycleview
     * @return habitEventsList.size()
     */
    @Override
    public int getItemCount() {
        return content.size();
    }


    /**
     * This is used for moving items in the RecyclerView
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where we move the item to
     */
    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        // Move the HabitEvent in the content.
        HabitEvent habitEventToMove = content.remove(fromPosition);
        content.add(toPosition, habitEventToMove);

        // Move the HabitEvent in the database.
        databaseAdapter.moveHabitEvent(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * This is used for deleting items in the RecyclerView
     * @param position the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        // Remove the HabitEvent from the content.
        content.remove(position);

        // Remove the HabitEvent from the database.
        databaseAdapter.removeHabitEvent(position);

        notifyItemRemoved(position);
    }


    public void setTouchhelper(ItemTouchHelper touchhelper){
        this.mTouchhelper=touchhelper;
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView Displaycomment;
        TextView DisplayHabit;
        TextView DisplayLocation;
        ImageView DisplayUserpic;
        GestureDetector mGestureDetector;
        ConstraintLayout parentLayout;
        OntouchListener ontouchListener;
        public Myviewholder(@NonNull View itemView,OntouchListener ontouchListener) {
            super(itemView);
            Displaycomment=itemView.findViewById(R.id.DisplayCommentCE);
            DisplayHabit=itemView.findViewById(R.id.DisplayHabitCE);
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
}
