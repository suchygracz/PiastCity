package com.example.piastcity
// login screen
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piastcity.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import eventSearch.EventSearchActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
    private lateinit var firebaseAuth: FirebaseAuth;
    private var email: String = ""
    private var password: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        email = binding.loginEmail.text.toString()
        password = binding.loginPassword.text.toString()

    }

    fun Login(view: View) {
        if (isEmailValid(view) and isPasswordValid(view)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
            Toast.makeText(this, "logged in",Toast.LENGTH_LONG).show()
            val appIntent = Intent(this, UI::class.java)
            startActivity(appIntent)
        }

    }

    fun isPasswordValid(view: View): Boolean{
        password = binding.loginPassword.text.toString()
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
        email = binding.loginEmail.text.toString()
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

        return emailRegex.matches(email)
    }

    fun goToRegisterActivity(view: View) {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }
}
