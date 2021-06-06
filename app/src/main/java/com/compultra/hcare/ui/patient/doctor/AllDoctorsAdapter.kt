package com.compultra.hcare.ui.patient.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.databinding.ListViewTwoBinding
import com.compultra.hcare.network.model.EmailNameArea

class AllDoctorsAdapter(private val onItemClicked: (EmailNameArea) -> Unit) : ListAdapter<EmailNameArea, AllDoctorsAdapter.DoctorsViewHolder>(DiffCallback) {
    class DoctorsViewHolder(
        private var binding: ListViewTwoBinding,
        private val onClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: EmailNameArea) {
            binding.title.text = doctor.name
            binding.subtitle.text = doctor.area
        }

        init {
            binding.root.setOnClickListener { onClicked(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsViewHolder {
        return DoctorsViewHolder(
            ListViewTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(getItem(it))
        }

    }

    override fun onBindViewHolder(holder: DoctorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EmailNameArea>() {
        override fun areItemsTheSame(oldItem: EmailNameArea, newItem: EmailNameArea): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: EmailNameArea, newItem: EmailNameArea): Boolean {
            return (oldItem.name == newItem.name && oldItem.area == newItem.area)
        }

    }
}