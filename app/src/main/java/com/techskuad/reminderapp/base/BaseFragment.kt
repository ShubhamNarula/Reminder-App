package com.example.woohoo.base



import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.utilities.Utils


abstract  class BaseFragment : Fragment() {
    /**
     * To show progress
     */
    var mProgressDialog: Dialog? = null
    /**
     * Called from onCreateView () Function
     */
    public abstract fun getLayoutID():Int
    /**
     * Called from onViewCreated () Function
     */
    public abstract fun onCreateView();
     var mContent: View? = null
    /**************************************  Fragment Lifecycle Methods  ************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView == null) {
            rootView = inflater.inflate(getLayoutID(), container, false)
        } else{
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }
        return rootView
    }
    var hasInitializedRootView = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContent = view
        if(!hasInitializedRootView) {
            hasInitializedRootView = true
            onCreateView()
        }
    }
    override fun onPause() {
        super.onPause()
        activity?.let { Utils.hideKeyboard(it) }

    }
    override fun onStop() {
        super.onStop()
        activity?.let { Utils.hideKeyboard(it) }
    }



    /**************************************  Fragment Lifecycle Methods  ************************************************************************************/


    /**
     * Immediately go back like click on back arrow icon or click on close button or click on cross icon
     */
    protected fun goBack() {
        activity?.onBackPressed()
    }

    protected fun navigateScreen(id:Int,bundle: Bundle?=null){
        findNavController().navigate(id,bundle)
    }

    fun showToast(message:String){
        Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
    }
    /**************************************** Go Back *******************************************************************************************************/

    /********************************************* Show progress and hide progress ****************************************************************************************************/

    fun showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = requireActivity().let { Dialog(it, android.R.style.Theme_Translucent) }
            mProgressDialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            mProgressDialog?.setContentView(R.layout.loader_half__layout)
            mProgressDialog?.setCancelable(false)
        }

        mProgressDialog?.show()
    }

    fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.dismiss()
        }
    }

}