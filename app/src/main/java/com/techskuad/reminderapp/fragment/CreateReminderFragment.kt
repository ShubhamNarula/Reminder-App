package com.techskuad.reminderapp.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.woohoo.base.BaseFragment
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.databinding.CreateReminderFragmentBinding
import com.techskuad.reminderapp.utilities.TimeUtils
import com.techskuad.reminderapp.viewmodel.ReminderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_reminder_fragment.*

@AndroidEntryPoint
class CreateReminderFragment : BaseFragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var binding: CreateReminderFragmentBinding
    private var day = 0
    private var month = 0
    private var year = 0
    override fun getLayoutID(): Int {
        return R.layout.create_reminder_fragment
    }

    override fun onCreateView() {
        binding = mContent?.let { CreateReminderFragmentBinding.bind(it) }!!
        binding.vm = viewModel
        initObserver()
    }

    private fun initObserver() {
        viewModel.reminderTitleError.observe(viewLifecycleOwner, {
            etReminderTitle.error = it
        })
        viewModel.reminderDescError.observe(viewLifecycleOwner, {
            etReminderDesc.error = it
        })
        viewModel.isTimeShow.observe(viewLifecycleOwner, {
            if (it == "Yes") {
                val hour = TimeUtils.getTimeCalendar().first
                val minute = TimeUtils.getTimeCalendar().second
                TimePickerDialog(requireActivity(), this, hour, minute, false).show()
            }
        })
        viewModel.isDatePickerShow.observe(viewLifecycleOwner, {
            if (it == "Yes") {
                val time = TimeUtils.getDateCalendar()
                year = time.year
                month = time.month
                day = time.day
                val datePickerDialog = DatePickerDialog(
                    requireActivity(), this, year, month,
                    day
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        })

        viewModel.setSelectedTime(TimeUtils.getCurrentTimeWithFormat())
        viewModel.getSelectedTime().observe(viewLifecycleOwner,{
            txtTimeSet.text=it
        })
        viewModel.setSelectedDate(TimeUtils.getCurrentDateWithFormat())
        viewModel.getSelectedDate().observe(viewLifecycleOwner,{
            txtDateSet.text=it
        })

        viewModel.getDisplayErrorLiveData().observe(viewLifecycleOwner,{
            viewModel.showToast(it)
        })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        val time = TimeUtils.timeConverter(hourOfDay, minute)
        viewModel.setSelectedTime(time)
        txtTimeSet.text = time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val date = TimeUtils.dateConverter(year, month, day)
        viewModel.setSelectedDate(date)
        txtDateSet.text = date
    }
}