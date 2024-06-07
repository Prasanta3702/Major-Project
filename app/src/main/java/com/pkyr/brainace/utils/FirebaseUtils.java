package com.pkyr.brainace.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkyr.brainace.Subject;
import com.pkyr.brainace.model.UserModel;

public class FirebaseUtils {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionRef() {
        return FirebaseFirestore.getInstance().collection("users");
    }




    public static void createSubject(Context context, String course, String batch, String sem, Subject subject) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference subjectRef = databaseReference.child("courses").child(course)
                .child(batch).child(sem).child("subjects").child(subject.getSubjectName());
        subjectRef.setValue(subject).addOnCompleteListener(task-> {
            if(task.isSuccessful()) {
                Toast.makeText(context, "Subject created success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Subject created failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void createUser(Context context, UserModel userModel) {
        currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(context, "User created success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "User created failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
