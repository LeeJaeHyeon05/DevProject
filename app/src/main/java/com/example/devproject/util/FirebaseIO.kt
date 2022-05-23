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

        fun write(collectionPath : String, documentPath : String, information : UserInfo) {

            db.collection(collectionPath).document(documentPath).set(information)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error writing document, $it")
                }
        }

        fun write(collectionPath: String, documentPath: String, information: Any): Boolean{
            var success = false
            db.collection(collectionPath).document("document$documentPath").set(information)
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

        fun cloudWrite(documentPath: String, mapSnapShotBitmap: Bitmap, conference: ConferenceInfo): Boolean{
            var success = false
            val bitmap = mapSnapShotBitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            db.collection("conferenceDocument").document("document${documentPath}").set(conference)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error writing document, $it")
                }
            if(db.collection("conferenceDocument").document(documentPath).path.isNotEmpty()){
                success = true
            }
            return success
        }

        fun storageWrite(
            documentPath: String,
            snapshotImage: ImageView,
            imageList: ArrayList<Uri>,
            collectionPath: String,
            docNumText: String,
            conference: ConferenceInfo,
        ): Boolean{
            val bitmapDrawable = snapshotImage.drawable

            when(bitmapDrawable == null){
                true -> {
                    when(imageList.isEmpty()){
                        true -> { //지도사진 x, 이미지 x
                            db.collection(collectionPath).document("document$docNumText").set(conference)
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
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                val uploadPostImageTask = storage.getReference("documentPost").child("document$documentPath")
                                for(i in uriList){ //이미지 올리기
                                    uploadPostImageTask.child("${i.path?.substring(i.path!!.length-4, i.path!!.length)}").putFile(i)
                                }
                                conference.image?.clear()
                                for(i in uriList){
                                    conference.image?.add(Uri.parse("documentPost/document$documentPath/${i.path?.substring(i.path!!.length-4, i.path!!.length)}"))
                                }
                                conference.image?.sort()
                                db.collection(collectionPath).document("document$docNumText").set(conference)
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
                false -> {
                    val mapSnapShot = (bitmapDrawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    mapSnapShot.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    when(imageList.isEmpty()){
                        true -> { //지도 o, 이미지 x
                            storage.getReference("documentPost").child("document$documentPath/Map").child("MapSnapShot.jpeg").putBytes(data)
                            conference.image?.clear()
                            conference.image?.add(Uri.parse("documentPost/document$documentPath/Map/MapSnapShot.jpeg"))
                            db.collection(collectionPath).document("document$docNumText").set(conference)
                                .addOnSuccessListener {
                                    Log.d("TAG", "DocumentSnapshot successfully written! ")
                                }
                                .addOnFailureListener {
                                    Log.d("TAG", "Error writing document, $it")
                                }
                        }
                        false -> { //지도 o, 이미지 o
                            uriList = imageList

                            val uploadPostImageTask = storage.getReference("documentPost").child("document$documentPath")
                            for(i in uriList.indices){ //이미지 올리기
                                uploadPostImageTask.child("${uriList[i].toString().substring(uriList[i].toString().length-4, uriList[i].toString().length)}").putFile(uriList[i]).addOnSuccessListener {
                                    Log.d("TAG", "storageWrite: ${it.uploadSessionUri}")
                                }
                            }
                            storage.getReference("documentPost").child("document$documentPath/Map").child("MapSnapShot.jpeg").putBytes(data)
                            conference.image?.clear()
                            conference.image?.add(Uri.parse("documentPost/document$documentPath/Map/MapSnapShot.jpeg"))
                            for(i in uriList.indices){
                                conference.image?.add(Uri.parse("documentPost/document$documentPath/${uriList[i].toString().substring(uriList[i].toString().length-4, uriList[i].toString().length)}"))
                            }
                            conference.image?.sort()

                            db.collection(collectionPath).document("document$docNumText").set(conference)
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
            if(db.collection(collectionPath).document("document$docNumText").path.isNotEmpty()){
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