package com.example.userstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import com.example.userstory.databinding.ActivityRegisterBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameEditText = binding.edRegisterName
        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword
        registerButton = binding.btnRegister

        binding.cbShowPassword.setOnCheckedChangeListener(this)

        registerButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            showLoading(true)

            if (checkName(name) && isEmailValid(email) && isPasswordValid(password)) {
                val client = ApiConfig.getApiService().register(name, email, password)
                client.enqueue(object: Callback<RegisterResponse>{
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity, "Register account success", Toast.LENGTH_SHORT).show()
                                toLogin()
                                finish()
                            }
                        } else {
                            showLoading(false)
                            try {
                                val data = response.errorBody()?.string()
                                val jsonObject = JSONObject(data!!)
                                Toast.makeText(this@RegisterActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            } else {
                Toast.makeText(this, "Check again your name or email or password!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvToLogin.setOnClickListener {
            toLogin()
        }

    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val edRegisterPassword = binding.edRegisterPassword
        if (isChecked) {
            edRegisterPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            edRegisterPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun checkName(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}