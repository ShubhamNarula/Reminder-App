package com.techskuad.reminderapp.fragment

import androidx.fragment.app.viewModels
import com.example.woohoo.base.BaseFragment
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.databinding.LoginBinding
import com.techskuad.reminderapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login.*

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginBinding
    override fun getLayoutID(): Int {
        return R.layout.login
    }

    override fun onCreateView() {
        binding = mContent?.let { LoginBinding.bind(it) }!!
        binding.vm = viewModel
        initObserver()
    }

    private fun initObserver(){
        viewModel.emailError.observe(viewLifecycleOwner, {
            etEmail.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, {
            etPassword.error = it
        })
        viewModel.getIsLoadingLiveData().observe(viewLifecycleOwner, {
            if(it){
                showProgress()
            }else{
                hideProgress()
            }
        })
    }
}