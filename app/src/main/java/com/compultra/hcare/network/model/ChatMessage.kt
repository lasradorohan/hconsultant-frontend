package com.compultra.hcare.network.model

import com.google.gson.annotations.SerializedName
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*


data class ChatMessage(
    @SerializedName("timestamp") val timestamp: Date,
    @SerializedName("sender") val sender: String,
    @SerializedName("content") val content: String
) : IMessage {
    override fun getId() = sender + timestamp
    override fun getText() = content
    override fun getUser() = Author(sender, sender)
    override fun getCreatedAt() = timestamp
}

class Author(
    private val id: String,
    private val name: String
) : IUser {
    override fun getId() = id
    override fun getName() = name
    override fun getAvatar() = null
}