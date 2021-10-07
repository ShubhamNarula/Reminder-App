package com.techskuad.reminderapp.fragment

import androidx.fragment.app.viewModels
import com.example.woohoo.base.BaseFragment
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.databinding.EditReminderBinding
import com.techskuad.reminderapp.model.ReminderModel
import com.techskuad.reminderapp.utilities.Constants
import com.techskuad.reminderapp.utilities.TimeUtils
import com.techskuad.reminderapp.viewmodel.ReminderViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.techskuad.reminderapp.repository.ReminderDbRepository
import com.techskuad.reminderapp.utilities.notification_utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.all_reminder_list.*
import kotlinx.android.synthetic.main.edit_reminder.*
import kotlinx.android.synthetic.main.edit_reminder.etReminderDesc
import kotlinx.android.synthetic.main.edit_reminder.etReminderTitle
import kotlinx.android.synthetic.main.edit_reminder.txtDateSet
import kotlinx.android.synthetic.main.edit_reminder.txtTimeSet
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class EditReminderFragment : BaseFragment(),
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var binding: EditReminderBinding

    @Inject
    lateinit var reminderDbRepository: ReminderDbRepository
    private var day = 0
    private var month = 0
    private var year = 0
    lateinit var reminderModel: ReminderModel
    var type = "weekly"
    private val args: EditReminderFragmentArgs by navArgs()
    override fun getLayoutID(): Int {
        return R.layout.edit_reminder
    }

    override fun onCreateView() {
        reminderModel = args.reminderData!!
        binding = mContent?.let { EditReminderBinding.bind(it) }!!
        binding.vm = viewModel
        initObserver()
        onClick()
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
                val time = TimeUtils.getDayMonthYearFromString(txtDateSet.text.toString())
                day = time.day
                month = time.month
                year = time.year
                val datePickerDialog = DatePickerDialog(
                    requireActivity(), this, year, month,
                    day
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        })

        reminderModel.time?.let { viewModel.setSelectedTime(it) }
        reminderModel.date?.let { viewModel.setSelectedDate(it) }
        reminderModel.title?.let { viewModel.setTitle(it) }
        reminderModel.description?.let { viewModel.setDesc(it) }

        if (reminderModel.type == "daily") {
            myRadioGroup.check(myRadioGroup.getChildAt(0).getId())
        } else {
            myRadioGroup.check(myRadioGroup.getChildAt(1).getId())
        }


    }

    fun onClick() {

        btUpdateReminder.setOnClickListener {
            if (dailyRadio.isChecked) {
                type = "daily"
            }
            if (edtReminderDescription.text.toString() == "") {
                etReminderDesc.error = getString(R.string.required_field)
            }
            if (edtReminderTitle.text.toString() == "") {
                etReminderTitle.error = getString(R.string.required_field)
            }

            if (edtReminderDescription.text.toString() != "" && edtReminderTitle.text.toString() != "") {
                reminderModel.id.let { it1 ->
                    reminderDbRepository.reminderDao.updateReminder(
                        edtReminderTitle.text.toString(), edtReminderDescription.text.toString(),
                        txtTimeSet.text.toString(), txtDateSet.text.toString(), type,
                        it1
                    )
                }
                setReminder()
            }
        }
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

   private fun setReminder() {
        val selectedUserTime : String
        val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT)
        val selectedMonthAndDate = txtDateSet.text.toString()
        val time = txtTitle.text.toString()

        selectedUserTime = selectedMonthAndDate + " " + time

        val date = sdf.parse(selectedUserTime)
        val millis = date.getTime()
        val currentDateAndTime = TimeUtils.currentDateAndTime(Constants.DATE_TIME_FORMAT)
        if (TimeUtils.compareDateAndTime(
                currentDateAndTime,
                selectedUserTime,
                Constants.DATE_TIME_FORMAT
            )
        ) {
            var type = "weekly"
            if (this.type == "daily") {
                type = "daily"
            }

            NotificationUtils().addReminder(
                requireActivity(),
                type,
                millis,
                edtReminderDescription.text.toString(),
                edtReminderTitle.text.toString(),
                txtTimeSet.text.toString(),
                reminderId = reminderModel.id
            )
            navigateScreen(R.id.action_editReminderFragment_to_homeFragment)
        } else {
            showToast(getString(R.string.reminder_previous_time_error))
        }
    }
}