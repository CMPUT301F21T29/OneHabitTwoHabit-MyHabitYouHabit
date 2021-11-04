

        package com.example.ohthmhyh;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.location.Address;
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

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

        /**
         * A simple RecycleviewAdapter .
         *
         */
public class CERecycleviewAdapter extends RecyclerView.Adapter<CERecycleviewAdapter.Myviewholder>
        implements CeitemHelpToucherAdapter{
    ArrayList<HabitEvent> habitEventsList;
    Context context;
    ItemTouchHelper mTouchhelper;
    OntouchListener mOntouchListener;
            /**
             * @param habitEventsList A array of habit event
             * @param context Context from the activity
             * @param mOntouchListener A thing that does touch actions
             * The CERecycleviewAdapter creater Needs and array, context and a touch Listener
             */
    public  CERecycleviewAdapter(ArrayList<HabitEvent> habitEventsList, Context context,OntouchListener mOntouchListener){
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
    //sets the things in display notebook
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null
        holder.Displaycomment.setText("Comment: "+habitEventsList.get(position).getComment());
        holder.DisplayHabit.setText((habitEventsList.get(position).getHabit().toString()));
        holder.ExtraDisplay.setText("Extra: ");
        if (habitEventsList.get(position).getLocatoion()==null){
            holder.DisplayLocation.setText("Location: Na");
        } else{        holder.DisplayLocation.setText("Location: "+(Html.fromHtml(habitEventsList.get(position).getLocatoion().getAddressLine(0))));
        }

        holder.DisplayUserpic.setImageBitmap(habitEventsList.get(position).getBitmapPic());
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
     *This is used for moving items in the Recycleview
     * @peram frompositon When we move a item in the list this is the position
     * @peram topostion This is where me wmove the item to
     */
    @Override
    public void onItemMove(int frompositon, int toposition) {
        HabitEvent fromHabitevent=habitEventsList.get(frompositon);
        habitEventsList.remove(fromHabitevent);
        habitEventsList.add(toposition,fromHabitevent);
        notifyItemMoved(frompositon,toposition);
    }

    /**
     *This is used for deleting items in the Recycleview
     * @peram position the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        habitEventsList.remove(position);
        notifyItemRemoved(position);
    }


    public void setTouchhelper(ItemTouchHelper touchhelper){
        this.mTouchhelper=touchhelper;
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener
    {
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
         *These are all possible montions a user can do
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
        void onItemclicked(int position);
    }
}
