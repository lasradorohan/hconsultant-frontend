package com.compultra.hcare.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.databinding.ListViewTwoBinding
import com.compultra.hcare.network.model.EmailName

class ChatListAdapter(private val onItemClicked: (EmailName) -> Unit) :
    ListAdapter<EmailName, ChatListAdapter.ChatListViewHolder>(DiffCallback) {

    class ChatListViewHolder(
        private var binding: ListViewTwoBinding,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: EmailName) {
            binding.title.text = user.name
            binding.subtitle.text = user.email
        }

        init {
            binding.root.setOnClickListener { onItemClicked(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(ListViewTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
            onItemClicked(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EmailName>() {
        override fun areItemsTheSame(oldItem: EmailName, newItem: EmailName): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: EmailName, newItem: EmailName): Boolean {
            return (oldItem.name == newItem.name && oldItem.email == newItem.email)
        }

    }
}