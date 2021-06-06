package com.compultra.hcare.ui.patient.consultation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.R
import com.compultra.hcare.databinding.ConsultPatientListViewBinding
import com.compultra.hcare.network.model.Consultation

class ConsultationsPatientAdapter : ListAdapter<Consultation, ConsultationsPatientAdapter.ConsultationsViewHolder>(DiffCallback) {
    class ConsultationsViewHolder(
            private var binding: ConsultPatientListViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
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
        return ConsultationsViewHolder(
                ConsultPatientListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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