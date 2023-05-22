package com.example.mobilityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mobilityhelper.databinding.ActivityLoginBinding
import com.example.mobilityhelper.models.User
import com.example.mobilityhelper.ui_components.CustomUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private val customUI = CustomUI()
    var sharedPreference: SharedPreferences? = null

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            saveUserToSharedPreferences(currentUser)
            val goToHomePageIntent = Intent(this, HomeActivity::class.java)
            startActivity(goToHomePageIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = getSharedPreferences(
            resources.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE
        )

        auth = Firebase.auth
        binding.cvLoginLoader.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            logUserIn(binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString())
        }

        binding.tvLoginRegister.setOnClickListener {
            val goToRegistrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(goToRegistrationIntent)
        }
    }

    fun logUserIn(email: String, password: String) {
        binding.cvLoginLoader.visibility = View.VISIBLE
        if (email.isEmpty() || password.isEmpty()) {
            binding.cvLoginLoader.visibility = View.INVISIBLE
            customUI.createDialog(
                binding.btnLogin,
                "Error",
                "Text fields cannot be empty",
                R.drawable.icon_error_red_24
            )
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    if (user != null) {
                        saveUserToSharedPreferences(user)

                    }
                } else {
                    binding.cvLoginLoader.visibility = View.INVISIBLE
                    customUI.createDialog(
                        binding.btnLogin,
                        "Error",
                        task.exception?.message.toString(),
                        R.drawable.icon_error_red_24
                    )
                }
            }
        }
    }

    fun saveUserToSharedPreferences(user: FirebaseUser?) {
        db.collection(resources.getString(R.string.usersCollectionName))
            .whereEqualTo("id", user?.uid)
            .limit(1)
            .get().addOnSuccessListener { result ->
//                saveUserToSharedPreferences()

                val userFound = User(result.documents.first().data as Map<String, String>)

                var editor: SharedPreferences.Editor = sharedPreference!!.edit()
                editor.putString("id", userFound.id)
                editor.putString("email", userFound.email)
                editor.putString("firstName", userFound.firstName)
                editor.putString("gender", userFound.gender)
                editor.putString("role", userFound.role)
                editor.putString("phoneNumber", userFound.phoneNumber)
                editor.putString("username", userFound.username)
                editor.putString("lastName", userFound.lastName)
                editor.commit()

                binding.cvLoginLoader.visibility = View.INVISIBLE
                val goToHomePageIntent = Intent(this, HomeActivity::class.java)
                startActivity(goToHomePageIntent)
                finish()
            }.addOnFailureListener { databaseException ->
                binding.cvLoginLoader.visibility = View.INVISIBLE
                customUI.createDialog(
                    binding.btnLogin,
                    "Error",
                    " ${databaseException.message}",
                    R.drawable.icon_error_red_24
                )

            }

    }
}