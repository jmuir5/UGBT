package com.noxapps.ugbt

import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


internal class RecyclerAdapter(data: OrderedRealmCollection<AttackItem2>): RealmRecyclerViewAdapter<AttackItem2, RecyclerAdapter.ViewHolder?>(data, true) {
    lateinit var _parent:ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v:View =  LayoutInflater.from(parent.context).inflate(R.layout.history_cardview, parent, false)

        _parent = parent
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        Log.e("bindviewholder position", "position: $position")
        val obj:AttackItem2? = getItem(position)
        holder.data = obj
        holder.triggerTxt.text = obj?.trigger
        holder.dateTxt.text = obj?.startTime
        holder.intensityTxt.text = obj?.OAIntensity.toString()
        Log.e("OAintensity", obj?.OAIntensity.toString())
        holder.openBtn.setOnClickListener(){
            val intent = Intent(holder.openBtn.context, eventDetailsActivity::class.java).apply {
                putExtra("id", obj?.id2)
            }
            holder.openBtn.context.startActivity(intent)
        }



    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var data: AttackItem2? = null
        var triggerTxt: TextView
        var dateTxt: TextView
        var intensityTxt: TextView
        var openBtn: Button
        var sender:String?
        init {
            triggerTxt = itemView.findViewById(R.id.triggerTxt)
            dateTxt = itemView.findViewById(R.id.dateTxt)
            intensityTxt = itemView.findViewById(R.id.intensityTxt)
            openBtn = itemView.findViewById(R.id.openButton)
            sender = " "
        }
    }

}