package com.example.ohthmhyh.fragments;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ohthmhyh.ReminderBoradcast;
import com.example.ohthmhyh.adapters.HabitTodayRecyclerViewAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitsTodayFragment extends Fragment {

    private HabitTodayRecyclerViewAdapter adapter;
    private DatabaseAdapter databaseAdapter;

    public HabitsTodayFragment() {/* Required empty public constructor*/}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * this creates the fragment
     * this also sets the recyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Create a new alert dialog when Add a Habit button is clicked

        // Inflate the layout and get views for this fragment
        View view = inflater.inflate(R.layout.fragment_habits_today, container, false);
        createNotificationChannel();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.Alarm_Activate_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ReminderBoradcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                        0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long tenSecs = 1000 * 10;
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecs, pendingIntent);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_htf);

        // Get the HabitList from the database.
        databaseAdapter = DatabaseAdapter.getInstance();
        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                //make smaller list of habits to put into the recycler view
                ArrayList<Habit> habitsToday = new ArrayList<>();

                for (int i = 0; i < hList.size(); i++) {
                    Habit habit = hList.getHabit(i);
                    if (habit.isDueToday()) {
                        habitsToday.add(habit);
                    }
                }

                adapter = new HabitTodayRecyclerViewAdapter(getContext(), hList, habitsToday);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
            }
        });

        return view;
    }

    private void createNotificationChannel()
    {
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)

    {
        Uri soundUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://"+getActivity().
                getApplicationContext().getPackageName()+
                "/"+R.raw.audio);
        System.out.println("asasdasdd"+soundUri.toString());
        CharSequence name = "AlarmChannel";
        String description = "Channel for Alarm";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("Make_Alarm", name, importance);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(soundUri,audioAttributes);
        channel.setDescription(description);

        NotificationManager notificationManager=getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}

}
