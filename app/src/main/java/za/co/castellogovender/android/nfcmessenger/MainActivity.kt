package za.co.castellogovender.android.nfcmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = email_edt_reg.text.toString()
        val password = password_edt_reg.text.toString()

        Log.d("MainActivity", "Email is:"+email)
        Log.d("MainActivity", "Password is:$password")

        signin_txt_reg.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
