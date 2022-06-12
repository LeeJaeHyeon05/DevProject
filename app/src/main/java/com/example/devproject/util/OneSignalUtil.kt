package com.example.devproject.util

import android.util.Log
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject

class OneSignalUtil {
    companion object{
        fun post(headings : String, contents : String, deviceIDs : String){
            try {
                OneSignal.postNotification("{'headings' : {'en' : '${headings}'}, 'contents': {'en':'${contents}'}, 'include_player_ids': [${deviceIDs}]}",
                    object : OneSignal.PostNotificationResponseHandler {
                        override fun onSuccess(response: JSONObject) {
                            Log.i("OneSignalExample", "postNotification Success: $response")
                        }

                        override fun onFailure(response: JSONObject) {
                            Log.e("OneSignalExample", "postNotification Failure: $response")
                        }
                    })
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }
}