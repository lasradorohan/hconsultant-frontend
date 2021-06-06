package com.compultra.hcare.ui.patient.consultation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compultra.hcare.databinding.FragmentConsultationPatientBinding

class ConsultationPatientFragment : Fragment() {
    private lateinit var binding: FragmentConsultationPatientBinding
    private val viewModel: ConsultationPatientViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsultationPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }
    private fun setupUI(){
        binding.recView.adapter = ConsultationsPatientAdapter()
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveConsultations() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.consultations.observe(viewLifecycleOwner){
            (binding.recView.adapter as ConsultationsPatientAdapter).submitList(it)
        }
    }
}