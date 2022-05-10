package com.example.bankline_android.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.bankline_android.data.remote.BanklineApi
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception

object BanklineRepository {

    private val TAG = javaClass.simpleName

    private val restApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BanklineApi::class.java)
    }

    fun findBankStatement(accountHolderId: Int) = liveData {
        emit( State.Wait )
        try {
            val data = restApi.findBankStatement(accountHolderId)
            if(data.isEmpty()){
                emit(State.NotFound("Correntista de ID:"+accountHolderId+" não encontrado!"))
            }
            emit(State.Sucess(data))
        }catch (e: Exception){
            Log.e(TAG, e.message, e)
            emit(State.Error(e.message))
        }
    }
}