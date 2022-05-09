package com.example.devproject.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.format.UserInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class FirebaseIO {
    companion object {

        @SuppressLint("StaticFieldLeak")
        var db = FirebaseFirestore.getInstance()
        var storage = FirebaseStorage.getInstance()


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

        fun storageWrite(
            documentPath: String,
            mapSnapShotBitmap: Bitmap,
            imageList: ArrayList<Uri>,
            collectionPath: String,
            docNumText: String,
            conference: ConferenceInfo,
        ): Boolean{
            val uriList: MutableList<Uri> = mutableListOf()
            var success = false
            var bitmap = mapSnapShotBitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            for(i in imageList){
                uriList.add(i)
            }

            val data = baos.toByteArray()
            var count = 1

            CoroutineScope(Dispatchers.Main).launch {
                for(i in uriList){ //이미지 올리기
                    count = imageUpload(documentPath = documentPath, count = count, uri = i, snapshot = data)
                    count++
                }
                db.collection(collectionPath).document(docNumText).set(conference)
                    .addOnSuccessListener {
                        Log.d("TAG", "DocumentSnapshot successfully written! ")
                    }
                    .addOnFailureListener {
                        Log.d("TAG", "Error writing document, $it")
                    }
            }
            if(db.collection(collectionPath).document(docNumText).path.isNotEmpty()){
                success = true
            }
            return success
        }

        private suspend fun imageUpload(documentPath: String, count: Int, uri: Uri, snapshot: ByteArray): Int{
            val uploadMapSnapShotTask = storage.getReference("documentPost").child(documentPath).child("${documentPath}MapSnapShot.jpeg").putBytes(snapshot)
            val uploadPostImageTask = storage.getReference("documentPost").child(documentPath)

            uploadPostImageTask.child("$count Image.jpeg").putFile(uri)

            uploadMapSnapShotTask
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error writing document, $it")
                }
            return count
        }

        fun read(collectionPath : String, documentPath : String) : Task<DocumentSnapshot> {
            return db.collection(collectionPath).document(documentPath).get()
        }

        fun readPublic(collectionPath : String) : Task<QuerySnapshot> {
            return FirebaseFirestore.getInstance().collection(collectionPath).get()
        }

        fun delete(collectionPath : String, documentPath : String){
            db.collection(collectionPath).document(documentPath).delete()
        }

        fun isValidAccount() : Boolean{
            println(FirebaseAuth.getInstance().currentUser?.email)
            return FirebaseAuth.getInstance().currentUser?.email != null
        }
    }

}