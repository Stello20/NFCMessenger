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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import za.co.castellogovender.android.nfcmessenger.R.id.*
import java.util.*

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

        //profilepic_btn_reg.setOnClickListener{
          //  val intent = Intent(Intent.ACTION_PICK)
          //  intent.type = "image/*"
          //  startActivityForResult(intent, 0)
        //}
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode ==0 && resultCode== Activity.RESULT_OK && data!=null){// not required for demo
            val photoURI = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,photoURI)
            val bitmapDrawable =  BitmapDrawable(bitmap)
            profilepic_btn_reg.setBackgroundDrawable(bitmapDrawable)
        }
    }*/

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
                //uploadImageToFirebaseStorage()
                saveToFirebaseDatabase()
                Toast.makeText(this,"Successfully created and added user to Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    /*private fun uploadImageToFirebaseStorage(){//not neccessary for demo
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(photoURI!!)
            .addOnSuccessListener {
                Log.d("FirebaseStorage", "Success image upload")
            }
    }*/

    private fun saveToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,username_edt_reg.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Successful upload to DB", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to upload to DB ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

class User(val uid:String, val username:String){
    constructor(): this("", "")
}