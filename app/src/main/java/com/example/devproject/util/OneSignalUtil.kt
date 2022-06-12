package com.example.devproject.util

import android.util.Log
import com.example.devproject.activity.MainActivity
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject

class OneSignalUtil {
    companion object{
        private const val ONESIGNAL_APP_ID = "75c55497-72ce-473d-98ba-a2d796996de7"

        fun initialize(mainActivity: MainActivity) {
            OneSignal.initWithContext(mainActivity)
            OneSignal.setAppId(ONESIGNAL_APP_ID)
        }

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