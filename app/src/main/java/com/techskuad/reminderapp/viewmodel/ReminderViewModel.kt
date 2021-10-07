package com.techskuad.reminderapp.viewmodel


import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.ReminderApplication
import com.techskuad.reminderapp.model.ReminderModel
import com.techskuad.reminderapp.repository.ReminderDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.databinding.ObservableBoolean
import com.techskuad.reminderapp.utilities.Constants
import com.techskuad.reminderapp.utilities.TimeUtils
import com.techskuad.reminderapp.utilities.navigateBack
import com.techskuad.reminderapp.utilities.navigateWithId
import com.techskuad.reminderapp.utilities.notification_utils.NotificationUtils
import java.text.SimpleDateFormat

@HiltViewModel
class ReminderViewModel @Inject constructor (
    val context: ReminderApplication,
    val reminderDbRepository: ReminderDbRepository
        ) : BaseViewModel(context) {
    val daily = ObservableBoolean()
    val weekly = ObservableBoolean()
    var setTiming = ""
    var setDate = ""
    var setTitleEditText  = ""
    var setDescEditText= ""
    //this is used for data insert in background
    fun insert(data: ReminderModel) = viewModelScope.launch {
        reminderDbRepository.insert(data)
    }
     val isTimeShow: MutableLiveData<String> =
        MutableLiveData("")

    val isDatePickerShow: MutableLiveData<String> =
        MutableLiveData("")

    val getAllReminderList = reminderDbRepository.getAllReminderList

    private val selectedTime: MutableLiveData<String> =
        MutableLiveData("")


    fun setSelectedTime (selectedTime: String){
        setTiming = selectedTime
        this.selectedTime.postValue(selectedTime)
    }
    fun getSelectedTime(): LiveData<String> {
        return selectedTime
    }
    private val selectedDate: MutableLiveData<String> =
        MutableLiveData("")

    fun setSelectedDate (selectedDate: String){
        setDate = selectedDate
        this.selectedDate.postValue(selectedDate)
    }
    fun getSelectedDate(): LiveData<String> {
        return selectedDate
    }

    private val title: MutableLiveData<String> =
        MutableLiveData("")
    fun setTitle(title:String){
        setTitleEditText = title
        this.title.postValue(title)
    }
    fun getTitle(): LiveData<String>{
        return title
    }
    private val description: MutableLiveData<String> =
        MutableLiveData("")
    fun setDesc(desc:String){
        setDescEditText = desc
        this.description.postValue(desc)
    }
    fun getDesc(): LiveData<String>{
        return description
    }


    val reminderTitle by lazy { ObservableField("") }
    val reminderDescription by lazy { ObservableField("") }

    val reminderTitleError: MutableLiveData<String?> = MutableLiveData(null)
    val reminderDescError: MutableLiveData<String?> = MutableLiveData(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClick(view:View){
        when(view.id){
            R.id.imgBack->{
                view.navigateBack()
            }
            R.id.txtTimeSet->{
               isTimeShow.postValue("Yes")
            }
            R.id.txtDateSet->{
                isDatePickerShow.postValue("Yes")
            }

            R.id.btAddReminder ->{
                if (isValidCredentials()){
                    setReminder(view)
                }
            }
        }
    }

    private fun isValidCredentials(): Boolean {
        var isValid = true
        if (reminderTitle.get().isNullOrBlank()) {
            isValid = false
            setEmptyError(reminderTitleError)
        }
        if (reminderDescription.get().isNullOrBlank()) {
            isValid = false
            setEmptyError(reminderDescError)
        }
        if (!daily.get()&&!weekly.get()){
            isValid=false
            showToast(context.getString(R.string.reminder_type))
        }
        return isValid
    }

    fun setEmptyError(field: MutableLiveData<String?>) {
        field.postValue(context.getString(R.string.required_field))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setReminder(view: View){
        var selectedUserTime=""
        val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT)
        val selectedMonthAndDate =getSelectedDate().value
        val time= getSelectedTime().value
        if (getSelectedDate().value !="") {
            selectedUserTime = selectedMonthAndDate + " " + time
        }
        val date = sdf.parse(selectedUserTime)
        val millis = date.getTime()
        val currentDateAndTime = TimeUtils.currentDateAndTime(Constants.DATE_TIME_FORMAT)
        if (TimeUtils.compareDateAndTime(currentDateAndTime, selectedUserTime,Constants.DATE_TIME_FORMAT)) {
            var type ="weekly"
            if (daily.get()){
                type ="daily"
            }
            val reminderId = System.currentTimeMillis().toInt()
            val reminderData = ReminderModel(reminderTitle.get(),reminderDescription.get(),
                getSelectedTime().value, getSelectedDate().value,type)
            insert(reminderData)

            NotificationUtils().addFutureReminder(context,type,millis,
                reminderDescription.get().toString(), reminderTitle.get().toString(),getSelectedTime().value.toString(),reminderId
            )
//            NotificationUtils().runAt(context,type,millis,
//               reminderDescription.get().toString(), reminderTitle.get().toString(),getSelectedTime().value.toString(),reminderId)
            view.navigateWithId(R.id.action_createReminderFragment_to_homeFragment)
        }else{
            displayError.postValue(context.getString(R.string.reminder_previous_time_error))
        }
    }
    

    
}