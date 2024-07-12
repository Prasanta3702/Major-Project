package com.pkyr.brainace.utils;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkyr.brainace.MainActivity;
import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.model.AssignmentModel;
import com.pkyr.brainace.model.SubjectModel;
import com.pkyr.brainace.model.UserModel;

import java.util.ArrayList;

public class FirebaseUtils {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static DocumentReference currentUserDetails() throws FirebaseException {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference getAllUserDetails() {
        return FirebaseFirestore.getInstance().collection("users");
    }

}
