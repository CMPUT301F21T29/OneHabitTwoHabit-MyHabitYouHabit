package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.interfaces.ItemTransportable;

import java.util.ArrayList;

/**
 * An adapter used for putting HabitEvent objects into elements of a RecyclerView. Each row is
 * populated with views to display the HabitEvent's attributes, and gestures like swiping and moving
 * elements are also supported.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitEventRecyclerViewAdapter extends RecyclerView.Adapter<HabitEventRecyclerViewAdapter.ViewHolder>
        implements ItemTransportable {

    private Context context;
    private ItemTouchHelper itemTouchHelper;
    private OnTouchListener onTouchListener;
    private ArrayList<HabitEvent> content;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

    /**
     * Constructor for an adapter capable of putting habits into a RecyclerView.
     * @param content A array of habit event
     * @param context Context from the activity
     * @param onTouchListener A thing that does touch actions
     */
    public HabitEventRecyclerViewAdapter(ArrayList<HabitEvent> content, Context context,
                                         OnTouchListener onTouchListener){
        this.content=content;
        this.context=context;
        this.onTouchListener = onTouchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit_event,parent,false);
        ViewHolder holder =new ViewHolder(view, onTouchListener);

        return holder;
    }

    /**
     * Sets the items in the display
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because some things might be null
        HabitEvent habitEvent = content.get(position);

        // Find the corresponding Habit name for this HabitEvent.
        String habitName = "";
        for (int i = 0; i < databaseAdapter.numberOfHabits(); i++) {
            if (databaseAdapter.habitAtIndex(i).getUHID() == habitEvent.getHabitUHID()) {
                habitName = databaseAdapter.habitAtIndex(i).getName();
                break;
            }
        }

        holder.displayComment.setText(Html.fromHtml("<i>Comment:</i> " + habitEvent.getComment()));
        holder.displayHabit.setText(habitName);
        holder.displayLocation.setText(
                Html.fromHtml("<i>Location:</i> " + habitEvent.locationString(holder.itemView.getContext())));

        habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
            @Override
            public void onBMPcallback(Bitmap bitmap) {
                holder.displayUserPic.setImageBitmap(bitmap);
            }
        });
    }

    /**
     *Returns the amount of items in the RecyclerView
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
        openDialog(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.itemTouchHelper =touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView displayComment;
        TextView displayHabit;
        TextView displayLocation;
        ImageView displayUserPic;
        GestureDetector mGestureDetector;
        ConstraintLayout parentLayout;
        OnTouchListener ontouchListener;
        public ViewHolder(@NonNull View itemView, OnTouchListener ontouchListener) {
            super(itemView);
            displayComment =itemView.findViewById(R.id.DisplayCommentCE);
            displayHabit =itemView.findViewById(R.id.DisplayHabitCE);
            displayLocation=itemView.findViewById(R.id.DisplayLocationCE);
            displayUserPic =itemView.findViewById(R.id.DisplayUserpicCE);
            //This is the name of the constraint layout in display HE list
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
            ontouchListener.onItemClicked(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            itemTouchHelper.startDrag(this);
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
    public interface OnTouchListener {
     /**
     *This method is used to goto the edit screen
     * @param position the position of the list we want to edit
     */
        void onItemClicked(int position);
    }

    /**
     *This method is used to open a conformation screen with the user before a delete
     * @param position the position of the item being swiped
     */
    private void openDialog(int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setMessage(
                "Are you sure you want to delete this event? It will not impact your score.");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    // If user wants to delete and has confirmed, run this code with deletes the
                    // habit
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Remove the HabitEvent from the content.
                        content.remove(position);

                        // Remove the HabitEvent from the database.
                        databaseAdapter.removeHabitEvent(position);

                        notifyItemRemoved(position);
                    }
                });
        // If the user hits no they don't want to delete run this code
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        notifyItemChanged(position);
                    }
                });
        // If the user clicks outside the box run this code (same as saying no)
        alertDialogBuilder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        notifyItemChanged(position);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
