package com.gb.yatrasuraksha.chatbot

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gb.yatrasuraksha.R
import com.gb.yatrasuraksha.adapter.MessageAdapter
import com.gb.yatrasuraksha.databinding.ActivityChatBotBinding
import com.gb.yatrasuraksha.models.Message
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var messageList: ArrayList<Message>
    private lateinit var userMessageList: ArrayList<String>
    private lateinit var botMessageList: ArrayList<String>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var generativeModel: GenerativeModel
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private var chat: Chat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageList = arrayListOf()
        userMessageList = arrayListOf("Hii")
        botMessageList = arrayListOf("Hello")

        //setting up recycler view
        setUpRecyclerView()

        //set up gemini api
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyDb2-FLda57E_SaD8mbYgTPf0m3yEI9Als"
        )

        //change color of status bar
        window.statusBarColor = resources.getColor(R.color.bg, theme)

        //on clicking to send button
        binding.sendButton.setOnClickListener {
            binding.welcome.visibility = View.GONE
            val query = binding.queryEditText.text.toString().trim()
            addToChat(query, "me")
            binding.queryEditText.text.clear()
            callAPI(query)
        }
    }

    private fun setUpRecyclerView() {
        messageAdapter = MessageAdapter(this, messageList)
        binding.chatRecyclerview.adapter = messageAdapter
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.chatRecyclerview.layoutManager = llm
    }

    private fun addToChat(message: String, sentBy: String) {
        runOnUiThread {
            run {
                messageList.add(Message(message, sentBy))
                messageAdapter.notifyDataSetChanged()
                binding.chatRecyclerview.smoothScrollToPosition(messageAdapter.itemCount)
            }
        }
    }

    private fun addResponse(response: String) {
        messageList.removeAt(messageList.size - 1)
        botMessageList.add(response)
        addToChat(response, "bot")
    }

    private fun callAPI(query: String) {

        userMessageList.add(query)
        messageList.add(Message("Typing...", "bot"))

        viewModelScope.launch {
            try {
                if (chat == null) {
                    // Start a new chat session if not already started
                    val history = listOf(
                        content(role = "user") { text("Hello") },
                        content(role = "model") { text("Hi there! How can I assist you?") }
                    )
                    chat = generativeModel.startChat(history)
                }

                // Send message and update UI with response
                val response = chat?.sendMessage(query)
                response?.let { displayResult(it.text) }
            } catch (e: Exception) {
                displayError(e.message ?: "Unknown error")
            }
        }
    }

    private fun displayError(error: String) {
        addResponse("Error")
        Log.d("Gaurav", error)
    }

    private fun displayResult(result: String?) {
        if (result != null) {
            addResponse(result.trim())
        }
        Log.d("Gaurav", result.toString())

    }

}