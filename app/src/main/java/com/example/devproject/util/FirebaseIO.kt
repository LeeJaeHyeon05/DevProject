package com.example.devproject.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.format.UserInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.suspendCancellableCoroutine

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

        fun write(collectionPath: String, documentPath: String, information: ConferenceInfo): Boolean{
            var success = false
            db.collection(collectionPath).document(documentPath).set(information)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error writing document, $it")
                }

            if(db.collection(collectionPath).document(documentPath).path.isNotEmpty()){
                success = true
            }
            return success
        }

        fun read(collectionPath : String) : Task<QuerySnapshot> {
            return db.collection(collectionPath).get()
        }

        fun read(collectionPath : String, documentPath : String) : Task<DocumentSnapshot> {
            return db.collection(collectionPath).document(documentPath).get()
        }
    }

}