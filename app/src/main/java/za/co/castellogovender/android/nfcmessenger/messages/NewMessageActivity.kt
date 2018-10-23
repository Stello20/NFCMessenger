package za.co.castellogovender.android.nfcmessenger.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import za.co.castellogovender.android.nfcmessenger.R
import za.co.castellogovender.android.nfcmessenger.models.User
import android.preference.PreferenceManager
import android.content.SharedPreferences
import za.co.castellogovender.android.nfcmessenger.nfc.HostCardEmulatorService
import za.co.castellogovender.android.nfcmessenger.nfc.KeyExchangeSec


class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
        val adapter = GroupAdapter<ViewHolder>()
        recyclerview_newmessage.adapter = adapter
        fetchUsers()
        Toast.makeText(this,"finished fetching", Toast.LENGTH_SHORT).show()

    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    fun storeSharedKey(uid: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString(uid,preferences.getString("NextSharedKey",KeyExchangeSec.bytesToHex(HostCardEmulatorService.myDevice.sharedSecret) ))
        editor.apply()
    }

    /*fun readSharedKey(uid: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var name = preferences.getString("Name", "")
    }*/

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener{item, view->
                    val userItem = item as UserItem
                    val intent = Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)

                    storeSharedKey(userItem.user.uid)

                    startActivity(intent)
                    finish()
                }
                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}

class UserItem(val user: User): Item<ViewHolder>(){
    override  fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.username_txt_userrow.text = user.username
        viewHolder.itemView.key_txt_userrow.text = user.uid
    }

    override fun getLayout():Int{
        return R.layout.user_row_new_message
    }
}