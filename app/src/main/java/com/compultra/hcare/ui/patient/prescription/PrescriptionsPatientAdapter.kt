package com.compultra.hcare.ui.patient.prescription

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.R
import com.compultra.hcare.databinding.ListViewPrescriptionPatientBinding
import com.compultra.hcare.network.model.Consultation
import java.text.SimpleDateFormat

class PrescriptionsPatientAdapter() : ListAdapter<Consultation, PrescriptionsPatientAdapter.PrescriptionsViewHolder>(DiffCallback) {
    class PrescriptionsViewHolder(
            private var binding: ListViewPrescriptionPatientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    prescribeAction.visibility = when (prescribeAction.visibility) {
                        LinearLayout.VISIBLE -> LinearLayout.GONE
                        LinearLayout.GONE -> LinearLayout.VISIBLE
                        else -> LinearLayout.GONE
                    }
                }
                inputPrescription.apply{
                    isFocusableInTouchMode = false
                    isFocusable = false
                }

            }
        }

        @SuppressLint("SimpleDateFormat")
        fun bind(consultation: Consultation) {
            binding.apply {
                textConsultationId.text = root.context.getString(R.string.consultation_id, consultation.consultationID)
                title.text = consultation.name
                textConsultationDate.text = root.context.getString(R.string.print_date, SimpleDateFormat("yyyy-MM-dd").format(consultation.scheduledTimeBegin!!))
                inputPrescription.setText(consultation.prescription)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionsPatientAdapter.PrescriptionsViewHolder {
        val binding = ListViewPrescriptionPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PrescriptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrescriptionsViewHolder, position: Int) {
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
