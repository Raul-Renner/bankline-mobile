package com.example.bankline_android.ui.welcome

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bankline_android.databinding.WelcomeMainBinding
import com.example.bankline_android.domain.Correntista
import com.example.bankline_android.ui.statement.BankStatementActivity


class WelcomeActivity : AppCompatActivity() {

    private val binding by lazy{
        WelcomeMainBinding.inflate(layoutInflater);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val bar: androidx.appcompat.app.ActionBar? = supportActionBar
        if (bar != null) {
            bar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        }

        binding.btContinue.setOnClickListener {
            if(binding.editTextAccountHolderId.text.toString().trim().equals("")){
                Toast.makeText(this, "Campo Vazio! Digite o ID de um correntista",
                    Toast.LENGTH_LONG).show();
            }else{
                val accountHolderId = binding.editTextAccountHolderId.text.toString().toInt()
                val accountHolder = Correntista(accountHolderId);

                val intent = Intent(this, BankStatementActivity::class.java).apply {
                    putExtra(BankStatementActivity.EXTRA_ACCOUNT_HOLDER, accountHolder)
                }
                startActivity(intent);
            }
        }
    }
}