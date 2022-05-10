package com.example.bankline_android.data.remote

import com.example.bankline_android.domain.Movimentacao
import com.google.android.material.transition.Hold
import retrofit2.http.GET
import retrofit2.http.Path
import java.sql.RowId

interface BanklineApi {

    @GET("movimentacoes/{id}")
   suspend fun findBankStatement(@Path ("id") accountHolderId: Int)
    : List<Movimentacao>
}