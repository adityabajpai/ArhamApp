//package net.yoga.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;
//
//import net.yoga.R;
//import net.yoga.sharedpref.SessionManager;
//import net.yoga.utils.ArcProgress;
//import net.yoga.utils.FBEventLogManager;
//
//public class YogaActivity extends AppCompatActivity {
//
//    FirebaseFirestore db;
//    FirebaseAuth mAuth;
//
////    private VideoView videoView;
//    private ImageButton playButton;
//    private ArcProgress progressView;
//    SessionManager session;
//
//    //ExoPlayer
//    PlayerView videoView;
//    ExoPlayer exoPlayer;
//    Context context;
//
//    private int video_time = 12*60+4;
//    private int currentTime = 0;
//    private int stopPosition;
//    String mobileUser="";
//    private boolean isPlaying;
//    private Handler mHandler = new Handler();
//    private Runnable UpdateProgress = new Runnable() {
//        public void run() {
//            if (isPlaying) {
//                currentTime = currentTime - 1;
//                progressView.setProgress(currentTime);
//            }
//            if (currentTime > 0) {
//                mHandler.postDelayed(this, 1000);
//            } else if (currentTime == 0) {
//                isPlaying = false;
//                progressView.setVisibility(View.INVISIBLE);
//                playButton.setVisibility(View.VISIBLE);
//
//                FBEventLogManager.logArhamSessionCompleted();
//                Log.e("Session count",""+session.getArhamSessions());
//                int countSessions = session.getArhamSessions()+1;
//                session.putNoOfSessions(countSessions);
//                Log.e("Session count",""+session.getArhamSessions());
//                final DocumentReference docRef = db.collection("users").document(mobileUser);
//                docRef.get().addOnSuccessListener(documentSnapshot -> {
//                    if(documentSnapshot.exists()){
////                        Long count = documentSnapshot.getLong("noOfSessionsCompleted");
////                        count++;
//                        Log.e("Session Updated",""+session.getArhamSessions());
//                        docRef.update("noOfSessionsCompleted",countSessions).addOnSuccessListener(aVoid -> Log.d("Arham Session","Completed"));
//                    }
//                }).addOnFailureListener(e -> {
//                    Log.e("Session Shared",""+session.getArhamSessions());
//                    session.putNoOfSessions(countSessions);
//                });
//                Toast.makeText(YogaActivity.this,"Thank you for taking the session...",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//    };
//
//    private void initPlayer(){
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),new DefaultTrackSelector(),new DefaultLoadControl());
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if( savedInstanceState != null ) {
//            stopPosition = savedInstanceState.getInt("position");
//        }
//        setContentView(R.layout.activity_yoga);
//        videoView = findViewById(R.id.videoView);
////        videoView.setOnErrorListener((mp, what, extra) -> {
////            Log.d("video", "setOnErrorListener ");
////            return true;
////        });
//        session = new SessionManager(getApplicationContext());
//        Log.d("onCreate Stop",""+stopPosition);
//        db = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build();
//        db.setFirestoreSettings(settings);
////        videoView.seekTo(stopPosition);
////        videoView.start();
//        mAuth = FirebaseAuth.getInstance();
//        mobileUser = mAuth.getCurrentUser().getPhoneNumber();
//        progressView = findViewById(R.id.circleView);
//        playButton = findViewById(R.id.playView);
//        if(stopPosition==0){
//            restartVideoPlay();
//        }
//
//        progressView.setOnClickListener(view -> {
//            isPlaying = false;
////            videoView.pause();
//            progressView.setVisibility(View.INVISIBLE);
//            playButton.setVisibility(View.VISIBLE);
//        });
//
//        playButton.setOnClickListener(view -> {
//            isPlaying = true;
//            playButton.setVisibility(View.GONE);
//            progressView.setVisibility(View.VISIBLE);
////            videoView.start();
//            if (currentTime == 0) {
//                restartVideoPlay();
//            }
//        });
//    }
//    private void restartVideoPlay() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.yoga;
////        videoView.setVideoURI(Uri.parse(path));
////        videoView.start();
//        isPlaying = true;
//        progressView.setMax(video_time);
//        currentTime = video_time;
//        updateProgressBar();
//
//        FBEventLogManager.logArhamSessionStarted();
//    }
//
//    public void updateProgressBar() {
//        if (mHandler != null) {
//            mHandler.post(UpdateProgress);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("onResume Stop",""+stopPosition);
////        videoView.seekTo(stopPosition);
//        isPlaying = true;
////        videoView.resume(); //Or use resume() if it doesn't work. I'm not sure
//    }
//
//    // This gets called before onPause so pause video here.
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
////        stopPosition = videoView.getCurrentPosition();
////        videoView.pause();
//        isPlaying = false;
//        Log.d("onSaveInstance Stop",""+stopPosition);
//        outState.putInt("position", stopPosition);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(isPlaying){
//            isPlaying = false;
////            videoView.pause();
////            stopPosition = videoView.getCurrentPosition();
//            Log.e("Progress paused", "" + stopPosition);
//        }
//    }
//
//}
package net.yoga.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import net.yoga.R;
import net.yoga.model.User;
import net.yoga.sharedpref.SessionManager;
import net.yoga.utils.ArcProgress;
import net.yoga.utils.FBEventLogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YogaActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton playButton;

    private int currentTime = 0;
    private int stopPosition;
    private Handler mHandler = new Handler();
    private ArcProgress progressView;
    private int video_time = 12*60+4;
    String mobileUser="";

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    SessionManager session;

    private boolean isPlaying;
    private Runnable UpdateProgress = new Runnable() {
        public void run() {
            final DocumentReference docRef = db.collection("users1").document(mobileUser);
            if (isPlaying) {
                currentTime = currentTime - 1;
                progressView.setProgress(currentTime);
                List<Long> timestamp = Arrays.asList(System.currentTimeMillis());
                Map<String,Object> timestamps = new HashMap<>();
                timestamps.put("timestamps",timestamp);
//                docRef.set(timestamps, SetOptions.merge());
            }
            if (currentTime > 0) {
                mHandler.postDelayed(this, 1000);
            } else if (currentTime == 0) {
                isPlaying = false;
                progressView.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);

                FBEventLogManager.logArhamSessionCompleted();
                Log.e("Session count",""+session.getArhamSessions());
                int countSessions = session.getArhamSessions()+1;
                session.putNoOfSessions(countSessions);
                Log.e("Session count",""+session.getArhamSessions());

                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
//                        Long count = documentSnapshot.getLong("noOfSessionsCompleted");
//                        count++;
                        Log.e("Session Updated",""+session.getArhamSessions());
//                        docRef.update("timestamps", FieldValue.arrayUnion(System.currentTimeMillis()));
                        User user = documentSnapshot.toObject(User.class);
                        List<Long> timestamps = user.getTimestamps();
                        if(timestamps==null){
                            timestamps = new ArrayList<>();
                        }
                        timestamps.add(System.currentTimeMillis());
                        docRef.update("timestamps",timestamps);
                        docRef.update("noOfSessionsCompleted",countSessions).addOnSuccessListener(aVoid -> Log.d("Arham Session","Completed"));
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Session Shared",""+session.getArhamSessions());
                    session.putNoOfSessions(countSessions);
                });
                Toast.makeText(YogaActivity.this,"Thank you for taking the session...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if( savedInstanceState != null ) {
//            stopPosition = savedInstanceState.getInt("position");
//        }
        setContentView(R.layout.activity_yoga);
        videoView = findViewById(R.id.videoView);
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.d("video", "setOnErrorListener ");
            return true;
        });
        //wake lock acquire so that screen does not turn off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        session = new SessionManager(getApplicationContext());
        Log.d("onCreate Stop",""+stopPosition);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
        mobileUser = mAuth.getCurrentUser().getPhoneNumber();

        progressView = findViewById(R.id.circleView);
        playButton = findViewById(R.id.playView);

//        if(stopPosition==0){
//            restartVideoPlay();
//        }

        progressView.setOnClickListener(view -> {
            isPlaying = false;
            videoView.pause();
            progressView.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
        });

        playButton.setOnClickListener(view -> {
            isPlaying = true;
            playButton.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            videoView.start();
            if (currentTime == 0) {
                restartVideoPlay();
            }
        });

        restartVideoPlay();
    }

    private void restartVideoPlay() {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.yoga;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
        isPlaying = true;
        progressView.setMax(video_time);
        currentTime = video_time;
        updateProgressBar();

        FBEventLogManager.logArhamSessionStarted();
    }

    public void updateProgressBar() {
        if (mHandler != null) {
            mHandler.post(UpdateProgress);
        }
    }

    // This gets called before onPause so pause video here.
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
////        stopPosition = videoView.getCurrentPosition();
////        videoView.pause();
//        isPlaying = false;
//        Log.d("onSaveInstance Stop",""+stopPosition);
//        outState.putInt("position", stopPosition);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(isPlaying){
//            isPlaying = false;
////            videoView.pause();
////            stopPosition = videoView.getCurrentPosition();
//            Log.e("Progress paused", "" + stopPosition);
//        }
//    }
}