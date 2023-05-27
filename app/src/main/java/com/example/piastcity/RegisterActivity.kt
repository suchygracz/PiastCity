package com.example.piastcity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piastcity.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore
    private var user: String = ""
    private var email: String = ""
    private var password: String = ""
    private var passwordConfirm: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = binding.registerEmail.text.toString()
        email = binding.registerEmail.text.toString()
        password = binding.registerPassword.text.toString()
        passwordConfirm = binding.register2Password.text.toString()
    }

    fun Signup(view: View) {
        if (arePasswordsMaching(view) and isEmailValid(view) and isPasswordValid(view)){
            Toast.makeText(this, "you have been registered succesfully", Toast.LENGTH_LONG).show()
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                firestore.collection("users").document(user)
            }
            Thread.sleep(5000)
            loginUser(email, password)
            goToCreateUserActivity(view)
        }
        else{
            if (!arePasswordsMaching(view)){
                Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_LONG).show()
            }
            else if (!isEmailValid(view)){
                Toast.makeText(this, "make sure that e-mail follows this shema _..._@_..._._..._", Toast.LENGTH_LONG).show()
            }
            else if(!isPasswordValid(view)){
                Toast.makeText(this, "Password need to be at least 8 char long and contain a-z and A-Z and 0-9 and Special char", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Login successful
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    // You can perform additional operations here, such as retrieving user data

                } else {
                    // Login failed
                    Toast.makeText(
                        applicationContext,
                        "Login failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    fun arePasswordsMaching(view: View): Boolean{
        password = binding.registerPassword.text.toString()
        passwordConfirm = binding.register2Password.text.toString()

        return password==passwordConfirm
    }

    fun isPasswordValid(view: View): Boolean{
        password = binding.registerPassword.text.toString()
        val lowercaseRegex = Regex("[a-z]")
        val uppercaseRegex = Regex("[A-Z]")
        val numberRegex = Regex("[0-9]")
        val specialCharRegex = Regex("[^A-Za-z0-9]")

        val hasLowercase = lowercaseRegex.containsMatchIn(password)
        val hasUppercase = uppercaseRegex.containsMatchIn(password)
        val hasNumber = numberRegex.containsMatchIn(password)
        val hasSpecialChar = specialCharRegex.containsMatchIn(password)
        val isLongerThan8char = password.length >= 8

        return hasLowercase && hasUppercase && hasNumber && hasSpecialChar && isLongerThan8char
    }

    fun isEmailValid(view: View): Boolean{
        email = binding.registerEmail.text.toString()
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)
    }

    fun goToLoginActivity(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        // zabij aktywność po przejściu dalej
        finish()
    }

    fun goToCreateUserActivity(view: View){
        val userCreateIntent = Intent(this, UserCreate::class.java)
        startActivity(userCreateIntent)
        // zabij aktywność po przejściu dalej

    }
}