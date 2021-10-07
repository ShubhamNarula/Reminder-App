package com.techskuad.reminderapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techskuad.reminderapp.R
import com.techskuad.reminderapp.model.ReminderModel
import com.techskuad.reminderapp.utilities.inflate
import kotlinx.android.synthetic.main.all_reminder_list.view.*

class ReminderAdapter (val onClick : (ReminderModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    var reminderList = emptyList<Any>()
    private var UPCOMING_REMINDER =1
    private var PAST_REMINDER=2
    private var REMINDER_LAYOUT =3;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context=parent.context
        when(viewType){
            UPCOMING_REMINDER -> {
                return  ViewHolderUpcomingPastReminder(parent.inflate(R.layout.upcoming_reminder,false))
            }
            PAST_REMINDER -> {
                return  ViewHolderUpcomingPastReminder(parent.inflate(R.layout.past_reminder,false))
            }
            REMINDER_LAYOUT ->{
                return  ViewHolderReminder(parent.inflate(R.layout.all_reminder_list,false))
            }
        }
        return  ViewHolderReminder(parent.inflate(R.layout.all_reminder_list,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderReminder){
            val data = reminderList[position]
            holder.setData(data as ReminderModel)
        }
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }
    override fun getItemViewType(pos: Int): Int {
        if(reminderList.get(pos) is String){
            if(reminderList.get(pos).equals("UPCOMING")){
                return UPCOMING_REMINDER;
            }else if(reminderList.get(pos).equals("PAST")){
                return PAST_REMINDER;
            }

        }else{
            return  REMINDER_LAYOUT
        }


        return super.getItemViewType(pos)
    }
    fun setData(reminderList: ArrayList<Any>) {
        this.reminderList = reminderList
        notifyDataSetChanged()
    }


    inner class ViewHolderUpcomingPastReminder(val view:View):RecyclerView.ViewHolder(view){

    }
    inner class ViewHolderReminder(val view: View) : RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun setData(data : ReminderModel){
            view.txtTitle.text = context.getString(R.string.title)+":-  "+data.title
            view.txtDesc.text = context.getString(R.string.description)+":-  "+data.description
            view.txtDate.text = context.getString(R.string.date)+":-  "+data.date
            view.txtTime.text = context.getString(R.string.time)+":-  "+data.time
            view.txtType.text = context.getString(R.string.type)+":-  "+data.type

            view.img_edit.setOnClickListener {
                onClick(data)
            }
        }
    }


}