package com.noxapps.ugbt.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.noxapps.ugbt.*
import com.noxapps.ugbt.databinding.FragmentDetailsBinding
import io.realm.Realm
import io.realm.mongodb.User

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private lateinit var realm: Realm
    private var user: User? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.listFreqBtn.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.listFreqBtn.setOnClickListener(){
            startActivity(Intent(activity, listFreqActivity::class.java))
        }
        binding.triggerList.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.triggerList.setOnClickListener(){
            startActivity(Intent(activity, triggerListActivity::class.java))
        }
        binding.painStats.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.painStats.setOnClickListener(){
            startActivity(Intent(activity, painStatsActivity::class.java))
        }
        binding.intensityTime.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.intensityTime.setOnClickListener(){
            startActivity(Intent(activity, intensityTimeActivity::class.java))
        }
        binding.optionsBtn.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.optionsBtn.setOnClickListener(){
            startActivity(Intent(activity, optionsActivity::class.java))
        }
        binding.infoBtn.setBackgroundResource(R.drawable.big_rainbow_button)
        binding.infoBtn.setOnClickListener(){
            startActivity(Intent(activity, additionalInfoActivity::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}