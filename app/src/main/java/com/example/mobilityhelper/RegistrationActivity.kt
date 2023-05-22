package com.example.mobilityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import com.example.mobilityhelper.databinding.ActivityRegistrationBinding
import com.example.mobilityhelper.ui_components.CustomUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    private val spinnerItems = arrayOf("User", "Driver")
    lateinit var spinnerAdapter: ArrayAdapter<String>

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private val customUI = CustomUI()

    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        binding.spinnerUsertype.adapter = spinnerAdapter

        auth = Firebase.auth
        binding.cvRegistrationLoader.visibility = View.GONE

        binding.btnRegistration.setOnClickListener {
            registerUser(
                binding.etRegistrationUsername.text.toString(),
                binding.etRegistrationEmail.text.toString(),
                binding.etRegistrationPassword.text.toString(),
                binding.etRegistrationConfirmPassword.text.toString(),
                binding.etRegistrationFirstname.text.toString(),
                binding.etRegistrationLastname.text.toString(),
                binding.etRegistrationPhoneNumber.text.toString()
            )
        }
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ) {
        binding.cvRegistrationLoader.visibility = View.VISIBLE
        val selectedOption: Int = binding.rgGender!!.checkedRadioButtonId
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
            binding.cvRegistrationLoader.visibility = View.INVISIBLE
            customUI.createDialog(
                binding.btnRegistration,
                "Error",
                "Text fields cannot be empty",
                R.drawable.icon_error_red_24
            )
        } else if (password != confirmPassword) {
            binding.cvRegistrationLoader.visibility = View.INVISIBLE
            customUI.createDialog(
                binding.btnRegistration,
                "Error",
                "Passwords do not match",
                R.drawable.icon_error_red_24
            )
        } else if (selectedOption == -1) {
            binding.cvRegistrationLoader.visibility = View.INVISIBLE
            customUI.createDialog(
                binding.btnRegistration,
                "Error",
                "Gender not selected",
                R.drawable.icon_error_red_24
            )

        } else {
            radioButton = findViewById(selectedOption)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        val userToSave = hashMapOf(
                            "id" to user?.uid.toString(),
                            "email" to email,
                            "firstname" to firstName,
                            "lastname" to lastName,
                            "phonenumber" to phoneNumber,
                            "username" to username,
                            "gender" to radioButton.text.toString(),
                            "role" to binding.spinnerUsertype.selectedItem.toString().lowercase()
                        )

                        if (user != null) {
                            db.collection(resources.getString(R.string.usersCollectionName))
                                .document(user.uid).set(userToSave)
                            binding.cvRegistrationLoader.visibility = View.INVISIBLE

                            customUI.createDialog(
                                binding.btnRegistration,
                                "Success",
                                "Account Registered Successfully",
                                R.drawable.icon_check_circle_green_24
                            )

                            Handler().postDelayed({
                                val loginIntent = Intent(this, LoginActivity::class.java)
                                startActivity(loginIntent)
                                finish()
                            }, 1000)

                        }

                    } else {
                        binding.cvRegistrationLoader.visibility = View.INVISIBLE
                        customUI.createDialog(
                            binding.btnRegistration,
                            "Error",
                            task.exception?.message.toString(),
                            R.drawable.icon_error_red_24
                        )

                    }
                }
        }
    }
}