package com.techskuad.reminderapp.fragment

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.easyeat.utils.shared_pref.LoginSharedPref
import com.example.woohoo.base.BaseFragment
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.databinding.SplashFragBinding
import com.techskuad.reminderapp.utilities.navigateWithId
import com.techskuad.reminderapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    var binding: SplashFragBinding? = null
    @Inject
    lateinit var loginSharedPref: LoginSharedPref
    override fun getLayoutID(): Int {
        return R.layout.splash_frag
    }
    @InternalCoroutinesApi
    override fun onCreateView() {
        binding = mContent?.let { SplashFragBinding.bind(it) }

    }
    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()

        val userLogin = loginSharedPref.getString(loginSharedPref.isUserLoggedIn, null)
        if (userLogin == "Yes"){
            Handler(Looper.getMainLooper()).postDelayed({
                binding?.root?.navigateWithId(R.id.action_splash_to_homeFragment)
            }, 2000)
        }else {
            Handler(Looper.getMainLooper()).postDelayed({
                binding?.root?.navigateWithId(R.id.action_splash_to_login)
            }, 2000)
        }
    }
}


