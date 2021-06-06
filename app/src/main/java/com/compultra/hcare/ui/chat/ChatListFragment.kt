package com.compultra.hcare.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compultra.hcare.databinding.FragmentChatListBinding


class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private val viewModel: ChatListViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.recView.adapter = ChatListAdapter {
            ChatActivity.start(requireContext(), it.email, it.name)
        }
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveChats() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.chats.observe(viewLifecycleOwner) {
            (binding.recView.adapter as ChatListAdapter).submitList(it)
        }
    }
}