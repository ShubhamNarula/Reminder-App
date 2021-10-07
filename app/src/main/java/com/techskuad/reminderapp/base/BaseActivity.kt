package com.example.woohoo.base

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.techskuad.reminderapp.R


abstract class BaseActivity : AppCompatActivity() {

    /**
     * Called from onCreate () Function
     */
    public abstract fun getLayoutId(): Int
    public abstract fun onLayoutCreated()

    /**************************************  Activity Lifecycle Methods  ************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.MyAppTheme)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
        onLayoutCreated()


    }
}