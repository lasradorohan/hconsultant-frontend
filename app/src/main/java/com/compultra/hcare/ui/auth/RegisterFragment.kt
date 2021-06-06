package com.compultra.hcare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.compultra.hcare.R
import com.compultra.hcare.databinding.FragmentRegisterBinding
import com.compultra.hcare.network.AuthApi
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var gender: String? = null
    private var blood: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {
            radioUserType.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    radioDoctor.id -> {
                        registerWeightBlood.visibility = LinearLayout.GONE
                        registerArea.visibility = TextInputLayout.VISIBLE
                    }
                    radioPatient.id -> {
                        registerArea.visibility = TextInputLayout.GONE
                        registerWeightBlood.visibility = LinearLayout.VISIBLE
                    }
                }
            }
            (registerGender.editText as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        resources.getStringArray(R.array.gender)
                    )
                )
                setOnItemClickListener { parent, view, position, id ->
                    gender = resources.getStringArray(R.array.gender_short)[position]
                }
            }
            (registerArea.editText as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        resources.getStringArray(R.array.doctor_types)
                    )
                )
            }
            (registerBlood.editText as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        resources.getStringArray(R.array.blood)
                    )
                )
                setOnItemClickListener { parent, view, position, id ->
                    blood = resources.getStringArray(R.array.blood)[position]
                }
            }
            registerSubmit.setOnClickListener { validateAndSubmit() }
        }

    }

    private fun validateAndSubmit() {
        //TODO perform input validation

        val userIsPatient = binding.radioPatient.isChecked
        GlobalScope.launch {
            val message = withContext(Dispatchers.IO) {
                try {
                    if(userIsPatient){
                        AuthApi.retrofitService.registerPatient(
                            email = binding.registerEmail.editText?.text.toString(),
                            password = binding.registerPassword.editText?.text.toString(),
                            name = binding.registerName.editText?.text.toString(),
                            age = binding.registerAge.editText?.text.toString().toInt(),
                            gender = gender!!,
                            weight = binding.registerWeight.editText?.text.toString().toInt(),
                            blood = blood!!,
                        ).message
                    } else {
                        AuthApi.retrofitService.registerDoctor(
                            email = binding.registerEmail.editText?.text.toString(),
                            password = binding.registerPassword.editText?.text.toString(),
                            name = binding.registerName.editText?.text.toString(),
                            age = binding.registerAge.editText?.text.toString().toInt(),
                            gender = gender!!,
                            area = binding.registerArea.editText?.text.toString()
                        ).message
                    }
                } catch (e: Exception) {
                    e.toString()
                }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

}