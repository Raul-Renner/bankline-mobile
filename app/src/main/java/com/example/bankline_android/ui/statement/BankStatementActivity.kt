package com.example.bankline_android.ui.statement

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankline_android.R
import com.example.bankline_android.data.State
import com.example.bankline_android.databinding.ActivityBankStatementBinding
import com.example.bankline_android.domain.Correntista
import com.example.bankline_android.domain.Movimentacao
import com.example.bankline_android.domain.TipoMovimentacao
import com.example.bankline_android.ui.statement.Adapter.BankStatementAdapter
import com.example.bankline_android.ui.welcome.WelcomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.withTimeout
import java.lang.IllegalArgumentException

class BankStatementActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ACCOUNT_HOLDER = "com.example.bankline_android.ui.statement.EXTRA_ACCOUNT_HOLDER"
    }

    private val binding by lazy{
        ActivityBankStatementBinding.inflate(layoutInflater);
    }

    private val accountHolder by lazy{
        intent.getParcelableExtra<Correntista>(EXTRA_ACCOUNT_HOLDER) ?: throw IllegalArgumentException()
    }

    private val viewModel by viewModels<BankStatementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bar: androidx.appcompat.app.ActionBar? = supportActionBar
        if (bar != null) {
            bar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        }

        binding.rvBankStatement.layoutManager = LinearLayoutManager(this)

        findBankStatement()

        binding.srlBankStatement.setOnRefreshListener { findBankStatement() }


    }

    private fun findBankStatement(){
        viewModel.findBankStatement(accountHolder.id).observe(this){ state ->
            when(state){
                is State.Sucess -> {
                    binding.rvBankStatement.adapter = state.data?.let { BankStatementAdapter(it) };
                    binding.srlBankStatement.isRefreshing = false
                }
                is State.Error -> {
                    state.message?.let {
                        Snackbar.make(binding.rvBankStatement, it,
                            Snackbar.LENGTH_LONG).show()
                    }
                    binding.srlBankStatement.isRefreshing = false
                } is State.NotFound ->{
                    state.message?.let{
                        Snackbar.make(binding.rvBankStatement, it, Snackbar.LENGTH_LONG).show()
                    }
                    val handler = Handler()
                    handler.postDelayed({
                        val intent = Intent(this, WelcomeActivity::class.java);
                        startActivity(intent);
                        // do something after 1000ms
                    }, 1000)

                    binding.srlBankStatement.isRefreshing = false

                }
                State.Wait -> binding.srlBankStatement.isRefreshing = true
            }
        }
    }

}