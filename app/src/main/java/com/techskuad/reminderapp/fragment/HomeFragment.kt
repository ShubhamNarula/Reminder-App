package com.techskuad.reminderapp.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woohoo.base.BaseFragment
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.adapter.ReminderAdapter
import com.techskuad.reminderapp.model.ReminderModel
import com.techskuad.reminderapp.utilities.Constants
import com.techskuad.reminderapp.utilities.TimeUtils
import com.techskuad.reminderapp.viewmodel.ReminderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var adapter: ReminderAdapter

    override fun getLayoutID(): Int {
        return R.layout.home_fragment
    }
    override fun onCreateView() {
        floating_action_button.setOnClickListener {
            navigateScreen(R.id.action_homeFragment_to_createReminderFragment)
        }
        getDataFromDb()
        setAdapter()
    }
   private fun getDataFromDb() {
        viewModel.getAllReminderList.observe(viewLifecycleOwner, Observer { data ->
            val allReminderList = getReminderList(data)
            adapter.setData(allReminderList)
        })

    }
    @SuppressLint("WrongConstant")
    fun setAdapter() {
        adapter = ReminderAdapter() {
         val action = HomeFragmentDirections.actionHomeFragmentToEditReminderFragment(it)
            findNavController().navigate(action)
        }
        rvReminder.adapter = adapter
        rvReminder.layoutManager = LinearLayoutManager(context)
    }

   private fun getReminderList(reminderList:List<ReminderModel>) : ArrayList<Any>{
        val listAllReminder: ArrayList<Any> = ArrayList()
        val upComingReminderList: ArrayList<ReminderModel> = ArrayList()
        val pastReminderList: ArrayList<ReminderModel> = ArrayList()
        for (i in reminderList.indices) {
            val currenDate = (TimeUtils.currentDateAndTime(Constants.DATE_TIME_FORMAT))
            val selectedUserTime = "${reminderList[i].date}  ${reminderList[i].time} "
            if (TimeUtils.compareDateAndTime(currenDate, selectedUserTime,Constants.DATE_TIME_FORMAT)) {
                upComingReminderList.add(reminderList[i])
            } else {
                pastReminderList.add(reminderList[i])
            }
        }
        listAllReminder.clear()
        if (upComingReminderList.size > 0) {
            listAllReminder.add("UPCOMING")
            listAllReminder.addAll(upComingReminderList)
        }
        if (pastReminderList.size > 0) {
            listAllReminder.add("PAST")
            listAllReminder.addAll(pastReminderList)
        }
        return listAllReminder
    }

}