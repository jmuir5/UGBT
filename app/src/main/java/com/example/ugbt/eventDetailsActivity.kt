package com.example.ugbt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.realm.Realm

import io.realm.Realm.init
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import io.realm.mongodb.User
import org.bson.types.ObjectId
import org.w3c.dom.Text

class eventDetailsActivity : AppCompatActivity() {
    lateinit var container1: ConstraintLayout
    lateinit var container2: ConstraintLayout
    lateinit var container3: ConstraintLayout
    lateinit var container4: ConstraintLayout
    lateinit var container5: ConstraintLayout
    lateinit var container6: ConstraintLayout
    lateinit var container7: ConstraintLayout
    lateinit var container8: ConstraintLayout

    lateinit var symptom1: TextView
    lateinit var symptom2: TextView
    lateinit var symptom3: TextView
    lateinit var symptom4: TextView
    lateinit var symptom5: TextView
    lateinit var symptom6: TextView
    lateinit var symptom7: TextView
    lateinit var symptom8: TextView

    lateinit var intensity1: TextView
    lateinit var intensity2: TextView
    lateinit var intensity3: TextView
    lateinit var intensity4: TextView
    lateinit var intensity5: TextView
    lateinit var intensity6: TextView
    lateinit var intensity7: TextView
    lateinit var intensity8: TextView

    lateinit var OAIntensity: TextView
    lateinit var trigger:TextView
    lateinit var startTime:TextView
    lateinit var endTime:TextView
    lateinit var note:EditText

    lateinit var symptoms:Array<TextView>
    lateinit var intensities:Array<TextView>
    lateinit var containers:Array<ConstraintLayout>

    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button

    private var user: User? = null
    lateinit var realm: Realm
    lateinit var currentAttack:AttackItem2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attack)

        user = UGBTApp.currentUser()
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@eventDetailsActivity.realm = realm
                //setUpRecyclerView(realm)
            }
        })

        container1 = findViewById(R.id.symptomContainerView1)
        container2 = findViewById(R.id.symptomContainerView2)
        container3 = findViewById(R.id.symptomContainerView3)
        container4 = findViewById(R.id.symptomContainerView4)
        container5 = findViewById(R.id.symptomContainerView5)
        container6 = findViewById(R.id.symptomContainerView6)
        container7 = findViewById(R.id.symptomContainerView7)
        container8 = findViewById(R.id.symptomContainerView8)

        //prep symptoms
        symptom1 = findViewById(R.id.symptom1)
        symptom2 = findViewById(R.id.symptom2)
        symptom3 = findViewById(R.id.symptom3)
        symptom4 = findViewById(R.id.symptom4)
        symptom5 = findViewById(R.id.symptom5)
        symptom6 = findViewById(R.id.symptom6)
        symptom7 = findViewById(R.id.symptom7)
        symptom8 = findViewById(R.id.symptom8)

        //prep intensity s
        intensity1 = findViewById(R.id.intensity1)
        intensity2 = findViewById(R.id.intensity2)
        intensity3 = findViewById(R.id.intensity3)
        intensity4 = findViewById(R.id.intensity4)
        intensity5 = findViewById(R.id.intensity5)
        intensity6 = findViewById(R.id.intensity6)
        intensity7 = findViewById(R.id.intensity7)
        intensity8 = findViewById(R.id.intensity8)

        OAIntensity = findViewById(R.id.OAintensityLabel2)
        trigger = findViewById(R.id.triggerLabel2)
        startTime = findViewById(R.id.startTimeLabel2)
        endTime = findViewById(R.id.endTimeLabel2)
        note = findViewById(R.id.notesText)

        updateBtn = findViewById(R.id.updateButton)
        deleteBtn = findViewById(R.id.deleteButton)


        symptoms = arrayOf(symptom1, symptom2, symptom3, symptom4, symptom5, symptom6, symptom7, symptom8)
        intensities = arrayOf(intensity1, intensity2, intensity3, intensity4, intensity5, intensity6, intensity7, intensity8)
        containers = arrayOf(container1, container2, container3, container4, container5, container6, container7, container8)

    }

    override fun onStart() {
        super.onStart()
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@eventDetailsActivity.realm = realm
                //setUpRecyclerView(realm)
                val identifier = intent.getStringExtra("id")
                currentAttack = realm.where<AttackItem2>().contains("id2", identifier).findFirst()!!
                OAIntensity.text = currentAttack.OAIntensity.toString()
                trigger.text = currentAttack.trigger
                startTime.text = currentAttack.startTime
                endTime.text = currentAttack.endTime
                note.setText(currentAttack.note)
                Log.e("currentAttack.symptomList.size", currentAttack.symptomList.size.toString())
                for(i in 0..currentAttack.symptomList.size-1){
                    Log.e("currentAttack.symptomList.size", currentAttack.symptomList.size.toString())
                    symptoms[i].text = currentAttack.symptomList[i]
                    intensities[i].text = currentAttack.intensityList[i].toString()
                    containers[i].visibility = View.VISIBLE
                }

            }
        })
        updateBtn.setOnClickListener{


            realm.executeTransaction {
                val identifier = intent.getStringExtra("id")
                var currentAttackUpdate = realm.where<AttackItem2>().contains("id2", identifier).findFirst()!!

                var updateNote = note.text.toString()

                currentAttackUpdate!!.note=updateNote
            }


        }
        deleteBtn.setOnClickListener{
            Log.e("button click", "Deletebutton clicked")
            realm.executeTransaction {
                val identifier = intent.getStringExtra("id")
                var currentAttackUpdate = realm.where<AttackItem2>().contains("id2", identifier).findFirst()!!


                currentAttackUpdate!!.deleteFromRealm()
            }
            finish()
        }
        /*val identifier = intent.getStringExtra("id")
        currentAttack = realm.where<AttackItem2>().contains("id2", identifier).findFirst()!!
        OAIntensity.text = currentAttack.OAIntensity.toString()
        trigger.text = currentAttack.trigger
        startTime.text = currentAttack.startTime
        endTime.text = currentAttack.endTime
        note.text = currentAttack.note
        for(i in 0..currentAttack.symptomList.size){
            symptoms[i].text = currentAttack.symptomList[i]
            intensities[i].text = currentAttack.intensityList[i].toString()
            containers[i].visibility = View.VISIBLE
        }*/
        
    }
}