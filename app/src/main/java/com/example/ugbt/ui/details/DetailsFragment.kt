package com.example.ugbt.ui.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ugbt.*
import com.example.ugbt.databinding.FragmentDetailsBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.RealmLog
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
        user = UGBTApp.currentUser()
        val test = RealmConfiguration.Builder().name("default3")
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        realm = Realm.getDefaultInstance()
        binding.listFreqBtn.setOnClickListener(){
            startActivity(Intent(activity, listFreqActivity::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}