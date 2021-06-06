package com.compultra.hcare.ui.patient.doctor

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.compultra.hcare.R
import com.compultra.hcare.databinding.DialogDoctorViewBinding
import com.compultra.hcare.databinding.FragmentDoctorPatientBinding
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.ui.chat.ChatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorPatientFragment : Fragment() {
    private lateinit var binding: FragmentDoctorPatientBinding
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.recView.adapter = AllDoctorsAdapter {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupDialog(it.email, it.name, it.area)
            }
        }
//        binding.recView.addItemDecoration(DividerItemDecoration(binding.recView.context, DividerItemDecoration.VERTICAL))
        binding.swipeRefresh.setOnRefreshListener { viewModel.retrieveDoctors() }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.swipeRefresh.isRefreshing = it
        }
        viewModel.allDoctors.observe(viewLifecycleOwner) {
            (binding.recView.adapter as AllDoctorsAdapter).submitList(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDialog(email: String, name: String, area: String) {
        val view = DialogDoctorViewBinding.inflate(layoutInflater).apply {
            title.text = name
            subtitle.text = area
            buttonRequest.setOnClickListener {
                when (requestInput.visibility) {
                    LinearLayout.VISIBLE -> requestInput.visibility = LinearLayout.GONE
                    LinearLayout.GONE -> requestInput.visibility = LinearLayout.VISIBLE
                }
            }
            inputDate.setOnFocusChangeListener { v, hasFocus ->
                when (hasFocus) {
                    true -> datePicker.visibility = DatePicker.VISIBLE
                    false -> datePicker.visibility = DatePicker.GONE
                }
            }
            datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                inputDate.setText(getString(R.string.date, year, monthOfYear, dayOfMonth))
            }
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
//            consultSubmit.setOnClickListener {
//
//
//            }
            buttonChat.setOnClickListener {
                ChatActivity.start(requireContext(), email, name)
            }

        }
        val diag = AlertDialog.Builder(context).setView(view.root).show()
        view.apply {
            consultSubmit.setOnClickListener {
                //TODO: Validate inputs
//                viewModel.requestConsultation(email, "$inputDate $fromTime", "$inputDate $toTime")
                GlobalScope.launch {
                    val message = withContext(Dispatchers.IO) {
                        try {
                            PatientDataApi.retrofitService.postConsultation(
                                    doctorEmail = email,
                                    requestedTimeBegin = "${inputDate.text} ${fromTime.text}",
                                    requestedTimeEnd = "${inputDate.text} ${toTime.text}"
                            ).message
                        } catch (e: Exception) {
                            e.toString()
                        }
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
                diag.dismiss()
            }
        }
    }
}