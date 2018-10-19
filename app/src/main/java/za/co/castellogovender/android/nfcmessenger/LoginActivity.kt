package za.co.castellogovender.android.nfcmessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        return_txt_signin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        login_btn_signin.setOnClickListener{
            loginWithFirebaseAuth()
        }
    }

    private fun loginWithFirebaseAuth(){
        Toast.makeText(this,"Trying", Toast.LENGTH_SHORT).show()
        val email = email_edt_signin.text.toString()
        val password = password_edt_signin.text.toString()
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Empty password or email field",Toast.LENGTH_SHORT ).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this,"Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }
    }

}