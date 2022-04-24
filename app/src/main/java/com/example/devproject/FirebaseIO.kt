package com.example.devproject

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirebaseIO {
    companion object {

        @SuppressLint("StaticFieldLeak")
        var db = FirebaseFirestore.getInstance()

        fun write(collectionPath : String, documentPath : String, information : UserInfo) {

            db.collection(collectionPath).document(documentPath).set(information)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error writing document, $it")
                }
        }

        fun write(collectionPath : String, documentPath : String,  information : ConferenceInfo) {
            db.collection(collectionPath).document(documentPath).set(information)
        }

        fun read(collectionPath : String) : Task<QuerySnapshot> {
            return db.collection(collectionPath).get()
        }

        fun read(collectionPath : String, documentPath : String) : Task<DocumentSnapshot> {
            return db.collection(collectionPath).document(documentPath).get()
        }
    }

}