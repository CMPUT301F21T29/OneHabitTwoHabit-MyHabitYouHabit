package com.example.ohthmhyh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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

import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.entities.HabitEvent;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
        if (habitEventsList.getHabitEvent(position).getLatitude()==null
            || habitEventsList.getHabitEvent(position).getLongitude()==null){
            holder.DisplayLocation.setText("Location: Na");
        } else{
            Geocoder geocoder=new Geocoder(context, Locale.getDefault());
            HabitEvent habitEvent = habitEventsList.getHabitEvent(position);
            try {
                List<Address> addresses=geocoder.getFromLocation(habitEvent.getLatitude(),habitEvent.getLongitude(),1);
                holder.DisplayLocation.setText(Html.fromHtml("<font color='#6200EEE'><b>Address: " +
                        addresses.get(0).getLocality())+" " +addresses.get(0).getCountryName());
            } catch (IOException e) {//Failed to get location from user
                e.printStackTrace();
                holder.DisplayLocation.setText("lat: "+habitEvent.getLatitude()+ "Lon: "+ habitEvent.getLongitude());

            }
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
        habitEventsList.removeHabitEvent(position);
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
