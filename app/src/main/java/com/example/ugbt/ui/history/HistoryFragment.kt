package com.example.ugbt.ui.history

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ugbt.R
import com.example.ugbt.databinding.FragmentHistoryBinding
import com.example.ugbt.eventDetailsAvtivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var toggleState=0
        binding.calendarView.visibility = View.GONE
        binding.viewToggle.setOnClickListener{
            if(toggleState==0){
                binding.calendarView.visibility=View.VISIBLE
                binding.historyScrollView.visibility = View.GONE
                toggleState=1
            } else{
                binding.calendarView.visibility=View.GONE
                binding.historyScrollView.visibility = View.VISIBLE
                toggleState=0
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val eventList = arrayOf(arrayOf("14/04/22", "7", "burrito"), arrayOf("14/03/22", "8", "pizza"),
        arrayOf("22/02/22", "5", "icecream"), arrayOf("02/02/22", "9", "salad"), arrayOf("15/01/22", "8", "toast"))



        for (i in eventList.indices){


            val containerTop = LinearLayout(activity)
            val labelContainer = LinearLayout(activity)
            val labelContainerR = LinearLayout(activity)
            val labelContainerL = LinearLayout(activity)
            val containerBottom = LinearLayout(activity)
            val labelIntensity = TextView(activity)
            val dateTime = TextView(activity)
            val labelTrigger = TextView(activity)
            val editButton = Button(activity)

            dateTime.text = eventList[i][0]
            labelIntensity.text = eventList[i][1]
            labelTrigger.text = eventList[i][2]
            editButton.text = "View/Edit"
            dateTime.textSize = 22f
            labelTrigger.textSize = 22f
            labelIntensity.textSize = 44f

            labelContainerR.gravity = Gravity.RIGHT
            val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.weight = 70f
            val lp2 = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp2.weight = 30f
            labelContainerL.layoutParams = lp
            labelContainerR.layoutParams = lp2
            labelIntensity.layoutParams = lp
            editButton.layoutParams = lp2
            editButton.setOnClickListener {
                val intent = Intent(activity, eventDetailsAvtivity::class.java).apply {

                }
                startActivity(intent)
            }

            labelContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            containerBottom.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            var topParams= LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            topParams.setMargins(20, 20, 20, 20)
            containerTop.orientation = LinearLayout.VERTICAL
            containerTop.setBackgroundResource(R.drawable.rainbow_button)
            containerTop.setPadding(20)

            if(labelTrigger.parent== null) labelContainerL.addView(labelTrigger)
            else Log.d("label","label has a parent, : "+labelContainerL.parent)
            if(dateTime.parent== null) labelContainerR.addView(dateTime)
            else Log.d("label","label has a parent, : "+labelContainerL.parent)

            if(labelContainerL.parent== null) labelContainer.addView(labelContainerL)
            else Log.d("label","label has a parent, : "+labelContainerL.parent)
            if(labelContainerR.parent== null) labelContainer.addView(labelContainerR)
            else Log.d("label","label has a parent, : "+labelContainerR.parent)

            if(labelIntensity.parent== null) containerBottom.addView(labelIntensity)
            else Log.d("label","label has a parent, : "+labelIntensity.parent)
            if(editButton.parent== null) containerBottom.addView(editButton)
            else Log.d("label","label has a parent, : "+editButton.parent)

            if(labelContainer.parent== null) containerTop.addView(labelContainer)
            else Log.d("label","label has a parent, : "+labelContainerR.parent)
            if(containerBottom.parent== null) containerTop.addView(containerBottom)
            else Log.d("label","label has a parent, : "+containerBottom.parent)
            if(containerTop.parent == null) binding.historyLl.addView(containerTop, topParams)
            else Log.d("label","label has a parent, : "+containerTop.parent)


        }
    }
}