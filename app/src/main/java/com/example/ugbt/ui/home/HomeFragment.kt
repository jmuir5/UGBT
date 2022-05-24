package com.example.ugbt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ugbt.R
import com.example.ugbt.databinding.FragmentHomeBinding
import com.example.ugbt.eventDetailsAvtivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        val bigButton = Button(activity)
        bigButton.layoutParams = LinearLayout.LayoutParams(
            750,
            750
        )
        bigButton.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.buttonHolder.addView(bigButton)
        bigButton.height = bigButton.width
        bigButton.text = "Start Recording \nAttack"
        bigButton.setOnClickListener {
            val intent = Intent(activity, eventDetailsAvtivity::class.java).apply {

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


    }
}