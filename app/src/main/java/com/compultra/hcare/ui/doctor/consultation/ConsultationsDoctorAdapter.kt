package com.compultra.hcare.ui.doctor.consultation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.R
import com.compultra.hcare.databinding.ConsultDoctorListViewBinding

import com.compultra.hcare.network.model.Consultation

class ConsultationsDoctorAdapter(
        private val onItemCancel: (Consultation) -> Unit,
        private val onItemChat: (Consultation) -> Unit,
        private val onItemSchedule: (Consultation) -> Unit
) : ListAdapter<Consultation, ConsultationsDoctorAdapter.ConsultationsViewHolder>(DiffCallback) {
    class ConsultationsViewHolder(
            private var binding: ConsultDoctorListViewBinding,
            private val onCancel: (Int) -> Unit,
            private val onChat: (Int) -> Unit,
            private val onSchedule: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    consultAction.visibility = when (consultAction.visibility) {
                        LinearLayout.VISIBLE -> LinearLayout.GONE
                        LinearLayout.GONE -> LinearLayout.VISIBLE
                        else -> LinearLayout.VISIBLE
                    }
                }
                cancelButton.setOnClickListener { onCancel(adapterPosition) }
                chatButton.setOnClickListener { onChat(adapterPosition) }
                scheduleButton.setOnClickListener { onSchedule(adapterPosition) }
            }
        }

        fun bind(consultation: Consultation) {
            binding.apply {
                textConsultationId.text = root.context.getString(R.string.consultation_id, consultation.consultationID)
                title.text = consultation.name
                subtitle.text = consultation.email
                textStatus.text = root.context.getString(R.string.consultation_status, consultation.status)
                textRequestedTime.text = root.context.getString(
                        R.string.consultation_period,
                        consultation.requestedTimeBegin.toString(),
                        consultation.requestedTimeEnd.toString()
                )
                textScheduledTime.text = root.context.getString(
                        R.string.consultation_period,
                        consultation.scheduledTimeBegin?.toString() ?: "-",
                        consultation.scheduledTimeEnd?.toString() ?: "-"
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationsViewHolder {
        val binding = ConsultDoctorListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsultationsViewHolder(
                binding,
                { onItemCancel(getItem(it)) },
                { onItemChat(getItem(it)) },
                { onItemSchedule(getItem(it)) }
        )
    }

    override fun onBindViewHolder(holder: ConsultationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Consultation>() {
        override fun areItemsTheSame(oldItem: Consultation, newItem: Consultation): Boolean {
            return oldItem.consultationID == newItem.consultationID
        }

        override fun areContentsTheSame(oldItem: Consultation, newItem: Consultation): Boolean {
            return oldItem.consultationID == newItem.consultationID
        }

    }
}
