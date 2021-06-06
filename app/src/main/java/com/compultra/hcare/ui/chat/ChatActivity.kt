package com.compultra.hcare.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.compultra.hcare.R
import com.compultra.hcare.databinding.ActivityChatBinding
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.network.model.ChatMessage
import com.compultra.hcare.ui.auth.AuthActivity
import com.compultra.hcare.util.*
import com.stfalcon.chatkit.messages.MessagesListAdapter


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshParams(
            getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE).getString(USER_TYPE, null),
            intent.extras?.getString(RECEPIENT_EMAIL),
            intent.extras?.getString(RECEPIENT_NAME)
        )

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        val adapter = MessagesListAdapter<ChatMessage>(viewModel.userType, null)
        binding.messagesList.setAdapter(adapter)

        binding.messageInput.setInputListener {

            viewModel.sendMessage(it.toString())
//            adapter.addToStart(message, true)
            true
        }
        if (viewModel.isError()) {
            Toast.makeText(this, "init error", Toast.LENGTH_LONG).show()
        }
        viewModel.messages.observe(this) {
            adapter.clear()
            adapter.addToEnd(it, false)
        }
        viewModel.fetchMessages()
//        Toast.makeText(this, viewModel.logStr(), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh -> viewModel.fetchMessages()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun start(context: Context, email: String, name: String) {
            val starter = Intent(context, ChatActivity::class.java)
                .putExtra(RECEPIENT_EMAIL, email)
                .putExtra(RECEPIENT_NAME, name)
            context.startActivity(starter)
        }
    }

}