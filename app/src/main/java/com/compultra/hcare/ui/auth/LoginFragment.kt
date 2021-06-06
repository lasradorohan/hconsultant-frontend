package com.compultra.hcare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.compultra.hcare.databinding.FragmentLoginBinding
import com.compultra.hcare.network.AuthApi
import com.compultra.hcare.ui.doctor.BrowseDoctorActivity
import com.compultra.hcare.ui.patient.BrowsePatientActivity
import com.compultra.hcare.util.AUTH_TOKEN
import com.compultra.hcare.util.MY_SHARED_PREFS
import com.compultra.hcare.util.USER_TYPE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginSubmit.setOnClickListener {
            val intent = Intent(context, BrowsePatientActivity::class.java)
            context?.startActivity(intent)
        }
//        arguments?.getString("userType")
        setUpUI()

    }

    private fun setUpUI() {
        binding.loginSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        GlobalScope.launch {
            val userIsPatient = binding.radioPatient.isChecked
            if (userIsPatient) {
                val tokenResponse = AuthApi.retrofitService.loginPatient(
                    binding.loginEmail.editText?.text.toString(),
                    binding.loginPassword.editText?.text.toString()
                )
                activity?.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)?.edit(true) {
                    putString(AUTH_TOKEN, tokenResponse.data)
                    putString(USER_TYPE, "patient")
                }
                BrowsePatientActivity.startAlone(requireContext())
            } else {
                val tokenResponse = AuthApi.retrofitService.loginDoctor(
                    binding.loginEmail.editText?.text.toString(),
                    binding.loginPassword.editText?.text.toString()
                )
                activity?.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)?.edit(true) {
                    putString(AUTH_TOKEN, tokenResponse.data)
                    putString(USER_TYPE, "doctor")
                }
                BrowseDoctorActivity.startAlone(requireContext())
            }
//            Log.d("compultra-log", tokenResponse.toString())
        }
    }

}