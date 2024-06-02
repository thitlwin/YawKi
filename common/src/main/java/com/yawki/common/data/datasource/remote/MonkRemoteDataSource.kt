package com.yawki.common.data.datasource.remote

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot

const val TAG = "-MonkRemoteDataSource-"

class MonkRemoteDataSource(private val monkCollection: CollectionReference) {
    fun getAllMonk(): Task<QuerySnapshot> = monkCollection.get()
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