package za.co.castellogovender.android.nfcmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

     var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signin_txt_reg.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        register_btn_reg.setOnClickListener{
            createNewUser()
        }

        profilepic_btn_reg.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode ==0 && resultCode== Activity.RESULT_OK && data!=null){

            val photoURI = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,photoURI)
            val bitmapDrawable =  BitmapDrawable(bitmap)
            profilepic_btn_reg.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun createNewUser(){
        val email = email_edt_reg.text.toString()
        val password = password_edt_reg.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Empty password or email field",Toast.LENGTH_SHORT ).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "Successfully created and added user to Firebase ${it.result.user.uid}")
            }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")
            }


    }
}
