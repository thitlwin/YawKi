package com.yawki.common.data.datasource.remote

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot

class SongRemoteDataSource(private val songCollection: CollectionReference) {
    fun getSongByMonk(monkId: Int): Task<QuerySnapshot> = songCollection
        .document("$monkId")
        .collection("mp3")
        .get()
        .addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result) {
                    Log.d(TAG, doc.id + "=>" + doc.data)
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.exception);
            }
        })

    fun getSongFolderByMonk(monkId: Int): Task<QuerySnapshot> = songCollection
        .document("$monkId")
        .collection("mp3_folder")
        .get()
        .addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result) {
                    Log.d(TAG, doc.id + "=>" + doc.data)
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.exception);
            }
        })

}