package com.noxapps.ugbt.ui.home

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
import com.noxapps.ugbt.AttackItem2
import com.noxapps.ugbt.R
import com.noxapps.ugbt.createAttackActivity
import com.noxapps.ugbt.databinding.FragmentHomeBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    lateinit var realm:Realm
    private var _binding: FragmentHomeBinding? = null
    lateinit var bigButton:Button
    var flip =false

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

        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
        binding.frequencyContainer.setOnClickListener(){
            if (!flip) {
                flip=true
                binding.textTimeLabel.text="Average time\nintense attacks"
            }else{
                flip=false
                binding.textTimeLabel.text="Average time\nbetween attacks"
            }
            binding.textTime.text = timeBetweenAttacks(flip)
        }
        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@HomeFragment.realm = realm
                binding.textIntensity.text = averageIntensity()
                binding.textTime.text = timeBetweenAttacks(false)

            }
        })

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
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@HomeFragment.realm = realm
                binding.textIntensity.text = averageIntensity()
                binding.textTime.text = timeBetweenAttacks(flip)

            }
        })

    }
    fun averageIntensity():String{
        val attacks = realm.where<AttackItem2>().findAll()!!
        var totalIntensity:Float = 0F
        var counter=0

        for (i in attacks.indices){
            totalIntensity += attacks[i]!!.OAIntensity
            counter++
        }
        totalIntensity /= counter
        val roundoff = String.format("%.2f", totalIntensity)
        return roundoff
    }

    fun timeBetweenAttacks(state:Boolean):String {
        val attacks = realm.where<AttackItem2>().findAll()!!
        var ordinalDates = mutableListOf<Int>()
        var counter = 0
        var runningTotal = 0
        var subtractor = 0
        var threshold=0
        if (attacks.size==0) return 0.toString()
        if (state){
            val sharedPref = this.activity?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            threshold=sharedPref!!.getInt("intenseAttackThreshold", 0)
        }

        for (i in attacks.indices) {
            if (attacks[i]?.OAIntensity!!>=threshold) {
                ordinalDates.add(attacks[i]!!.ordinalDate)
                counter++
            }
        }

        for (i in ordinalDates.indices) {
            ordinalDates[i] -= subtractor
            if (i != 0) runningTotal += ordinalDates[i]
            subtractor += ordinalDates[i]
        }
        return (runningTotal / counter).toString()


    }
}