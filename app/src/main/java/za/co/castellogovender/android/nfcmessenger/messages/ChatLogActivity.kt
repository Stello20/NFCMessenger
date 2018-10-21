package za.co.castellogovender.android.nfcmessenger.messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.LiveFolders.INTENT
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import za.co.castellogovender.android.nfcmessenger.R
import za.co.castellogovender.android.nfcmessenger.models.User

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        val adapter = GroupAdapter<ViewHolder>()
        recycler_chatlog.adapter = adapter
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        fetchChatItems()
        Toast.makeText(this,"finished fetching", Toast.LENGTH_SHORT).show()
    }
    fun fetchChatItems(){

    }

}

class ChatFromItem: Item<ViewHolder>(){
    override  fun bind(viewHolder: ViewHolder, position: Int){

    }

    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
}

class ChatToItem: Item<ViewHolder>(){
    override  fun bind(viewHolder: ViewHolder, position: Int){

    }

    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
}
