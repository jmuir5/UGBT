package com.example.ugbt.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugbt.AttackItem2
import com.example.ugbt.RecyclerAdapter
import com.example.ugbt.UGBTApp
import com.example.ugbt.databinding.FragmentHistoryBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import io.realm.mongodb.User

class HistoryFragment : Fragment() {
    private lateinit var realm: Realm
    private var user: User? = null
    private lateinit var adapter: RecyclerAdapter
    private lateinit var recyclerView: RecyclerView
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


        user = UGBTApp.currentUser()
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@HistoryFragment.realm = realm
                recyclerView = binding.historyRecyclerView
                setUpRecyclerView(realm)
            }
        })


        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
    private fun setUpRecyclerView(realm: Realm) {
        // a recyclerview requires an adapter, which feeds it items to display.
        // Realm provides RealmRecyclerViewAdapter, which you can extend to customize for your application
        // pass the adapter a collection of Tasks from the realm
        // we sort this collection so that the displayed order of Tasks remains stable across updates
        adapter = RecyclerAdapter(realm.where<AttackItem2>().sort("_id").findAll())
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager=linearLayoutManager
        //recyclerView.scrollToPosition(adapter.itemCount-1)
        //recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}