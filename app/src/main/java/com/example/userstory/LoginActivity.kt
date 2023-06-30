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
import com.example.userstory.databinding.ActivityLoginBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var loginButton: Button
    private var userModel: UserModel = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
        loginButton = binding.btnLogin

        binding.cbShowPassword.setOnCheckedChangeListener(this)

        binding.tvSignUp.setOnClickListener {
            toRegister()
        }

        binding.btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            showLoading(true)

            if (isEmailValid(email) && isPasswordValid(password)) {
                val client = ApiConfig.getApiService().login(email, password)
                client.enqueue(object: Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val name = responseBody.loginResult!!.name!!
                                val token = responseBody.loginResult.token!!

                                showLoading(false)

                                saveUser(name, email, password, token)

                                Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                                toMain()
                                finish()
                            }
                        } else {
                            try {
                                val data = response.errorBody()?.string()
                                val jsonObject = JSONObject(data!!)
                                Toast.makeText(this@LoginActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            } else {
                Toast.makeText(this, "Check again your email or password!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toRegister() {
        val toRegis = Intent(this, RegisterActivity::class.java)
        startActivity(toRegis)
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val edLoginPassword = binding.edLoginPassword
        if (isChecked) {
            edLoginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            edLoginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun saveUser(name: String, email: String, password: String, token: String) {
        val userPreferences = UserPreferences(this)
        userModel.name = name
        userModel.email = email
        userModel.password = password
        userModel.isLoggedIn = true
        userModel.token = token
        userPreferences.setUser(userModel)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}