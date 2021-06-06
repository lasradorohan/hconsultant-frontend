package com.compultra.hcare.ui.doctor.prescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compultra.hcare.databinding.FragmentPrescriptionDoctorBinding
import com.compultra.hcare.ui.doctor.consultation.ConsultationDoctorViewModel

class PrescriptionDoctorFragment : Fragment() {
    private lateinit var binding: FragmentPrescriptionDoctorBinding
    private val viewModel: ConsultationDoctorViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrescriptionDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.recView.adapter = PrescriptionsDoctorAdapter(
                onItemDelete = {
                    viewModel.setPrescription(it.consultationID)
                },
                onItemPrescribe = { consultation, prescription ->
                    viewModel.setPrescription(consultation.consultationID, prescription)
                }
        )
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveConsultations() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.consultations.observe(viewLifecycleOwner) { consultations ->
            (binding.recView.adapter as PrescriptionsDoctorAdapter).submitList(
                    consultations.filter { it.status == "approve" }
            )
        }
        viewModel.showMessage.observe(viewLifecycleOwner) {
            if(it){
                Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}