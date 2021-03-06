package net.yoga.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import net.yoga.R;
import net.yoga.model.User;

import java.util.ArrayList;
import java.util.List;

import static net.yoga.utils.Utils.generateReferralCode;

public class SignUpActivity extends AppCompatActivity {

    String mobNo = "",token="";
    EditText viewName,viewReferralCode,viewCity,viewState,viewRefferalCode;
    Button proceed;
    FirebaseFirestore db;
    ProgressDialog dialog;
    String myReferralCode;
    boolean flagUniqueCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Bundle bundle = getIntent().getExtras();
        mobNo = bundle.getString("mobile");

        proceed = findViewById(R.id.registerBtn);
        viewCity = findViewById(R.id.city);
        viewReferralCode = findViewById(R.id.referenceCode);
        viewState = findViewById(R.id.state);
        viewName = findViewById(R.id.userName);
        dialog = new ProgressDialog(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w( "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    Log.d("FCM token",token);
                });

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        myReferralCode = generateReferralCode();

//        Query query = db.collection("users")
//                        .whereEqualTo("myReferralCode",myReferralCode);
        Query query = db.collection("users")
                .whereEqualTo("myReferralCode", myReferralCode);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                myReferralCode = generateReferralCode();
            } else {
                flagUniqueCode = true;
            }
        });


        proceed.setOnClickListener(v -> {
            final User user = new User();
            String city = viewCity.getText().toString();
            String state = viewState.getText().toString();
            String name = viewName.getText().toString();
            String referralCode = viewReferralCode.getText().toString();
            user.setMyJoinedCode(referralCode);
            if(city.length()!=0&&name.length()!=0&&state.length()!=0) {
                user.setCity(city);
                user.setState(state);
                user.setUserName(name);
                user.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                user.setNoOfSessionsCompleted(0);
                user.setFcmId(token);
                user.setCreatedAt(System.currentTimeMillis());
                user.setMyReferralCode(myReferralCode);
                user.setJoinedReferralCode(new ArrayList<>());
                user.setSpecialSessionVideos(new ArrayList<>());
                dialog.setMessage("Saving...");
                dialog.show();
                dialog.setCancelable(false);
                if(referralCode.length()==0) {
                    referralCode="abc";
                }
                if(referralCode.length()!=0){
                    Query query1 = db.collection("users")
                            .whereEqualTo("myReferralCode",referralCode);
                    query1.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                            DocumentReference docRef = db.collection("users").document(id);
                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    User otherUser = documentSnapshot.toObject(User.class);
                                    List<String> referrals = otherUser.getJoinedReferralCode();
                                    if(referrals==null){
                                        referrals = new ArrayList<>();
                                    }
                                    Log.d("joned",referrals+"");
                                    referrals.add(mobNo);
                                    docRef.update("joinedReferralCode",referrals);
                                }
                            });
                        } else {
                            Log.d("Other referrals","no code exists");
                            user.setMyJoinedCode("");
                        }
                        db.collection("users").document(mobNo).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    dialog.dismiss();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                });
                    });
                }
            } else {
                if(TextUtils.isEmpty(city)){
                    viewCity.setError("Please fill this");
                    viewCity.requestFocus();
                }
                if(TextUtils.isEmpty(state)){
                    viewState.setError("Please fill this");
                    viewState.requestFocus();
                }
                if(TextUtils.isEmpty(name)){
                    viewName.setError("Please fill this");
                    viewName.requestFocus();
                }
            }
        });
    }

}
