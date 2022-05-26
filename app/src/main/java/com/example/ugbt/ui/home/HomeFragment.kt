package com.example.ugbt.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ugbt.R
import com.example.ugbt.createAttackActivity
import com.example.ugbt.databinding.FragmentHomeBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var bigButton:Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bigButton = Button(activity)
        bigButton.layoutParams = LinearLayout.LayoutParams(
            750,
            750
        )
        bigButton.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.buttonHolder.addView(bigButton)
        bigButton.height = bigButton.width
        bigButton.text = "Start Recording \nAttack"
        bigButton.setOnClickListener {
            val timeRaw = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
            val formatted = timeRaw.format(formatter)
            val intent = Intent(activity, createAttackActivity::class.java).apply {
                putExtra("Time", formatted)
            }
            startActivity(intent)

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if(sharedPref?.getInt("pausedAttack", 0)==1)bigButton.text = "Resume\nRecording\nAttack"


    }
}