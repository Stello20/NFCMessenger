package za.co.castellogovender.android.nfcmessenger.messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import za.co.castellogovender.android.nfcmessenger.R
import za.co.castellogovender.android.nfcmessenger.messages.MessagesActivity.Companion.currentUser
import za.co.castellogovender.android.nfcmessenger.models.ChatMessage
import za.co.castellogovender.android.nfcmessenger.models.User

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycler_chatlog.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username


        listenForMessages()
        sendbtn_chatlog.setOnClickListener{
            performSendMessage()
        }

        Toast.makeText(this,"finished fetching", Toast.LENGTH_SHORT).show()
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage!= null) {
                    if(chatMessage.fromId==FirebaseAuth.getInstance().uid){
                        val currentUser = MessagesActivity.currentUser ?:return
                        adapter.add(ChatFromItem(chatMessage.message, currentUser))
                    }
                    else {
                        adapter.add(ChatToItem(chatMessage.message, toUser!!))
                    }
                }
                recycler_chatlog.scrollToPosition(adapter.itemCount-1)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun performSendMessage(){
        val message = edt_chatlog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId==null)return
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage = ChatMessage(reference.key!!, message, fromId, toId, System.currentTimeMillis()/1000 )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edt_chatlog.text.clear()
                recycler_chatlog.scrollToPosition(adapter.itemCount-1)
            }
        toReference.setValue(chatMessage)
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }
}

class ChatFromItem(val fromText:String, val user:User): Item<ViewHolder>(){
    override  fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.message_from_chatlog.text = fromText
        viewHolder.itemView.username_from_chatlog.text = user.username
    }

    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
}

class ChatToItem(val toText:String, val user:User): Item<ViewHolder>(){
    override  fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.message_to_chatlog.text = toText
        viewHolder.itemView.username_to_chatlog.text = user.username

    }

    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
}
