package com.example.devproject.util

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
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
        private var uriList: MutableList<Uri> = mutableListOf()
        var success = false

        fun write(collectionPath: String, documentPath: String, information: Any): Boolean{
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
            collectionPath: String,
            documentPath: String,
            snapshotImage: ImageView,
            imageList: ArrayList<Uri>,
            conference: ConferenceInfo,
        ): Boolean{
            val bitmapDrawable = snapshotImage.drawable

            when(bitmapDrawable == null){
                true -> {
                    when(imageList.isEmpty()){
                        true -> { //지도사진 x, 이미지 x
                            db.collection(collectionPath).document(documentPath).set(conference)
                                .addOnSuccessListener {
                                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                                }
                                .addOnFailureListener {
                                    Log.d("TAG", "Error writing document, $it")
                                }
                        }
                        false -> { //지도사진 x, 이미지 o
                            for(i in imageList){
                                uriList.add(i)
                                Log.d("TAG", "didhstorageWrite: ${i.toString()}")
                            }
                            val uploadPostImageTask = storage.getReference("documentPost").child(documentPath)
                            conference.image?.clear()
                            for(i in uriList){ //이미지 올리기
                                if(i.toString().startsWith("h")){
                                    conference.image?.add(Uri.parse("documentPost/$documentPath/${i.path.toString().substring(i.path.toString().length-4, i.path.toString().length)}"))
                                    uploadPostImageTask.child("${i.path.toString().substring(i.path.toString().length-4, i.path.toString().length)}").putFile(i).addOnSuccessListener {
                                        Log.d("TAG", "storageWrite: ${it.uploadSessionUri}")
                                    }
                                }
                                else{
                                    conference.image?.add(Uri.parse("documentPost/$documentPath/${i.toString().substring(i.toString().length-4, i.toString().length)}"))
                                    uploadPostImageTask.child("${i.toString().substring(i.toString().length-4, i.toString().length)}").putFile(i).addOnSuccessListener {
                                        Log.d("TAG", "storageWrite: ${it.uploadSessionUri}")
                                    }
                                }
                            }
                            conference.image?.sort()
                            db.collection(collectionPath).document(documentPath).set(conference)
                                .addOnSuccessListener {
                                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                                }
                                .addOnFailureListener {
                                    Log.d("TAG", "Error writing document, $it")
                                }
                            uriList.clear()
                        }
                    }
                }
                false -> {
                    val mapSnapShot = (bitmapDrawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    mapSnapShot.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    when(imageList.isEmpty()){
                        true -> { //지도 o, 이미지 x
                            if(conference.offline == false){
                                storage.getReference("documentPost").child("$documentPath/Map").child("MapSnapShot.jpeg").delete()
                                conference.image?.clear()

                                db.collection(collectionPath).document(documentPath).set(conference)
                                    .addOnSuccessListener {
                                        Log.d("TAG", "DocumentSnapshot successfully written! ")
                                    }
                                    .addOnFailureListener {
                                        Log.d("TAG", "Error writing document, $it")
                                    }
                            }
                            else{
                                storage.getReference("documentPost").child("$documentPath/Map").child("MapSnapShot.jpeg").putBytes(data)
                                conference.image?.clear()
                                conference.image?.add(Uri.parse("documentPost/$documentPath/Map/MapSnapShot.jpeg"))
                                db.collection(collectionPath).document(documentPath).set(conference)
                                    .addOnSuccessListener {
                                        Log.d("TAG", "DocumentSnapshot successfully written! ")
                                    }
                                    .addOnFailureListener {
                                        Log.d("TAG", "Error writing document, $it")
                                    }
                            }
                        }
                        false -> { //지도 o, 이미지 o

                            for(i in imageList){
                                uriList.add(i)
                            }

                            conference.image?.clear()
                            val uploadPostImageTask = storage.getReference("documentPost").child("$documentPath")

                            for(i in uriList){ //이미지 올리기
                                if(i.toString().startsWith("h")){
                                    conference.image?.add(Uri.parse("documentPost/$documentPath/${i.path.toString().substring(i.path.toString().length-4, i.path.toString().length)}"))
                                    uploadPostImageTask.child("${i.path.toString().substring(i.path.toString().length-4, i.path.toString().length)}").putFile(i).addOnSuccessListener {
                                        Log.d("TAG", "storageWrite: ${it.uploadSessionUri}")
                                    }
                                }
                                else{
                                    conference.image?.add(Uri.parse("documentPost/$documentPath/${i.toString().substring(i.toString().length-4, i.toString().length)}"))
                                    uploadPostImageTask.child("${i.toString().substring(i.toString().length-4, i.toString().length)}").putFile(i).addOnSuccessListener {
                                        Log.d("TAG", "storageWrite: ${it.uploadSessionUri}")
                                    }
                                }
                            }

                            if(conference.offline == false){
                                storage.getReference("documentPost").child("$documentPath/Map").child("MapSnapShot.jpeg").delete()
                            }
                            else{
                                storage.getReference("documentPost").child("$documentPath/Map").child("MapSnapShot.jpeg").putBytes(data)
                                conference.image?.add(Uri.parse("documentPost/$documentPath/Map/MapSnapShot.jpeg"))
                            }

                            db.collection(collectionPath).document(documentPath).set(conference)

                            conference.image?.sort()

                            db.collection(collectionPath).document(documentPath).set(conference)
                                .addOnSuccessListener {
                                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                                }
                                .addOnFailureListener {
                                    Log.d("TAG", "Error writing document, $it")
                                }
                            uriList.clear()
                        }
                    }

                }

            }
            if(db.collection(collectionPath).document(documentPath).path.isNotEmpty()){
                success = true
            }
            return success
        }

        fun read(collectionPath : String, documentPath : String) : Task<DocumentSnapshot> {
            return db.collection(collectionPath).document(documentPath).get()
        }

        fun readPublic(collectionPath : String) : Task<QuerySnapshot> {
            return FirebaseFirestore.getInstance().collection(collectionPath).get()
        }

        fun readPublic(collectionPath : String, filterSet : MutableList<Any>) : Task<QuerySnapshot> {
            return if (DataHandler.filterList[0] == 0) {
                FirebaseFirestore.getInstance().collection(collectionPath)
                    .whereEqualTo("price", 0).get()
            } else {
                FirebaseFirestore.getInstance().collection(collectionPath)
                    .whereGreaterThanOrEqualTo("price", filterSet[0]).get()
            }
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