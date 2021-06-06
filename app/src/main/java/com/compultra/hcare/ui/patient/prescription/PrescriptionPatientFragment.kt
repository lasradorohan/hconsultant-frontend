package com.compultra.hcare.ui.patient.prescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compultra.hcare.databinding.FragmentPrescriptionPatientBinding
import com.compultra.hcare.ui.patient.consultation.ConsultationPatientViewModel

class PrescriptionPatientFragment : Fragment() {
    private lateinit var binding: FragmentPrescriptionPatientBinding
    private val viewModel: ConsultationPatientViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrescriptionPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.recView.adapter = PrescriptionsPatientAdapter()
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveConsultations() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.consultations.observe(viewLifecycleOwner) { consultations ->
            (binding.recView.adapter as PrescriptionsPatientAdapter).submitList(
                    consultations.filter { it.status == "approve" }
            )
        }
    }
}