package com.compultra.hcare.ui.doctor.consultation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compultra.hcare.R
import com.compultra.hcare.databinding.DialogScheduleDoctorBinding
import com.compultra.hcare.databinding.FragmentConsultationDoctorBinding
import com.compultra.hcare.network.model.Consultation
import com.compultra.hcare.ui.chat.ChatActivity
import java.text.SimpleDateFormat

class ConsultationDoctorFragment : Fragment() {
    private lateinit var binding: FragmentConsultationDoctorBinding
    private val viewModel: ConsultationDoctorViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsultationDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.recView.adapter = ConsultationsDoctorAdapter({
            viewModel.cancelConsult(it.consultationID)
        }, {
            ChatActivity.start(requireContext(), it.email, it.name)
        }, {
            showScheduleDialog(it)
        })
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveConsultations() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.consultations.observe(viewLifecycleOwner) {
            (binding.recView.adapter as ConsultationsDoctorAdapter).submitList(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showScheduleDialog(consultation: Consultation) {
        DialogScheduleDoctorBinding.inflate(layoutInflater).apply {
            val date = SimpleDateFormat("yyyy-MM-dd").format(consultation.requestedTimeBegin)
            requestDate.text = getString(R.string.print_date, date)
            title.text = consultation.name
            subtitle.text = consultation.email

            fromTime.setOnFocusChangeListener { v, hasFocus ->
                when (fromTime.isFocused || toTime.isFocused) {
                    true -> timePicker.visibility = TimePicker.VISIBLE
                    false -> timePicker.visibility = TimePicker.GONE
                }
            }
            toTime.setOnFocusChangeListener { v, hasFocus ->
                when (fromTime.isFocused || toTime.isFocused) {
                    true -> timePicker.visibility = TimePicker.VISIBLE
                    false -> timePicker.visibility = TimePicker.GONE
                }
            }
            timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                if (fromTime.isFocused) {
                    fromTime.setText(getString(R.string.time, hourOfDay, minute))
                } else {
                    toTime.setText(getString(R.string.time, hourOfDay, minute))
                }
            }
            consultSubmit.setOnClickListener {
                //TODO: Validate inputs
                Log.d("consultSubmitLog", "\n${requestDate.text}\n${fromTime.text}\n${toTime.text}")
                viewModel.scheduleConsult(consultation.consultationID,
                        "$date ${fromTime.text}",
                        "$date ${toTime.text}")
            }
            AlertDialog.Builder(context).setView(root).show()
        }
    }
}