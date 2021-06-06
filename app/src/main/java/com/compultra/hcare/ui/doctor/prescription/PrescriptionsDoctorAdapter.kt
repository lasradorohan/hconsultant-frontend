package com.compultra.hcare.ui.doctor.prescription

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.compultra.hcare.R
import com.compultra.hcare.databinding.ListViewPrescriptionDoctorBinding

import com.compultra.hcare.network.model.Consultation
import java.text.SimpleDateFormat

class PrescriptionsDoctorAdapter(
        private val onItemDelete: (Consultation) -> Unit,
        private val onItemPrescribe: (Consultation, String?) -> Unit
) : ListAdapter<Consultation, PrescriptionsDoctorAdapter.PrescriptionsViewHolder>(DiffCallback) {
    class PrescriptionsViewHolder(
            private var binding: ListViewPrescriptionDoctorBinding,
            private val onDelete: (Int) -> Unit,
            private val onPrescribe: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener { prescribeAction.visibility = LinearLayout.VISIBLE }
                cancelButton.setOnClickListener { prescribeAction.visibility = LinearLayout.GONE }
                deleteButton.setOnClickListener {
                    onDelete(adapterPosition)
                    prescribeAction.visibility = LinearLayout.GONE
                }
                prescribeButton.setOnClickListener {
                    onPrescribe(adapterPosition)
                    prescribeAction.visibility = LinearLayout.GONE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionsDoctorAdapter.PrescriptionsViewHolder {
        val binding = ListViewPrescriptionDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PrescriptionsViewHolder(
                binding,
                onDelete = { onItemDelete(getItem(it)) },
                onPrescribe = {
                    onItemPrescribe(
                            getItem(it),
                            binding.inputPrescription.text?.toString()
                    )
                }
        )
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
