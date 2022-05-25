package com.example.ugbt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import io.realm.Realm
import io.realm.RealmList
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class createAttackActivity : AppCompatActivity() {

    lateinit var container1:ConstraintLayout
    lateinit var container2:ConstraintLayout
    lateinit var container3:ConstraintLayout
    lateinit var container4:ConstraintLayout
    lateinit var container5:ConstraintLayout
    lateinit var container6:ConstraintLayout
    lateinit var container7:ConstraintLayout
    lateinit var container8:ConstraintLayout
    
    lateinit var sSpin1:Spinner
    lateinit var sSpin2:Spinner
    lateinit var sSpin3:Spinner
    lateinit var sSpin4:Spinner
    lateinit var sSpin5:Spinner
    lateinit var sSpin6:Spinner
    lateinit var sSpin7:Spinner
    lateinit var sSpin8:Spinner

    lateinit var iSpin1:Spinner
    lateinit var iSpin2:Spinner
    lateinit var iSpin3:Spinner
    lateinit var iSpin4:Spinner
    lateinit var iSpin5:Spinner
    lateinit var iSpin6:Spinner
    lateinit var iSpin7:Spinner
    lateinit var iSpin8:Spinner

    lateinit var TA1:EditText
    lateinit var TA2:EditText
    lateinit var TA3:EditText
    lateinit var TA4:EditText
    lateinit var TA5:EditText
    lateinit var TA6:EditText
    lateinit var TA7:EditText
    lateinit var TA8:EditText

    lateinit var addSymptomBtn:Button
    lateinit var removeSymptomBtn:Button
    
    var activeSymptoms=1

    lateinit var symptomcontainer:Array<ConstraintLayout>
    lateinit var sSpinners:Array<Spinner>
    lateinit var iSpinners:Array<Spinner>
    lateinit var symptomTA:Array<EditText>

    lateinit var dateTime:String
    lateinit var note:String

    private var user: User? = null
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_attack)

        user = UGBTApp.currentUser()
        var test = SyncConfiguration.Builder(user!!, "test")
            .waitForInitialRemoteData()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@createAttackActivity.realm = realm
                //setUpRecyclerView(realm)
            }
        })

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        //prep containers
        container1 = findViewById(R.id.symptomContainer1)
        container2 = findViewById(R.id.symptomContainer2)
        container3 = findViewById(R.id.symptomContainer3)
        container4 = findViewById(R.id.symptomContainer4)
        container5 = findViewById(R.id.symptomContainer5)
        container6 = findViewById(R.id.symptomContainer6)
        container7 = findViewById(R.id.symptomContainer7)
        container8 = findViewById(R.id.symptomContainer8)

        //prep symptom spinners
        sSpin1 = findViewById(R.id.symptomSpinner1)
        sSpin2 = findViewById(R.id.symptomSpinner2)
        sSpin3 = findViewById(R.id.symptomSpinner3)
        sSpin4 = findViewById(R.id.symptomSpinner4)
        sSpin5 = findViewById(R.id.symptomSpinner5)
        sSpin6 = findViewById(R.id.symptomSpinner6)
        sSpin7 = findViewById(R.id.symptomSpinner7)
        sSpin8 = findViewById(R.id.symptomSpinner8)

        //prep intensity spinners
        iSpin1 = findViewById(R.id.intensitySpinner1)
        iSpin2 = findViewById(R.id.intensitySpinner2)
        iSpin3 = findViewById(R.id.intensitySpinner3)
        iSpin4 = findViewById(R.id.intensitySpinner4)
        iSpin5 = findViewById(R.id.intensitySpinner5)
        iSpin6 = findViewById(R.id.intensitySpinner6)
        iSpin7 = findViewById(R.id.intensitySpinner7)
        iSpin8 = findViewById(R.id.intensitySpinner8)

        //prep symptom text areas
        TA1 = findViewById(R.id.symptomTA1)
        TA2 = findViewById(R.id.symptomTA2)
        TA3 = findViewById(R.id.symptomTA3)
        TA4 = findViewById(R.id.symptomTA4)
        TA5 = findViewById(R.id.symptomTA5)
        TA6 = findViewById(R.id.symptomTA6)
        TA7 = findViewById(R.id.symptomTA7)
        TA8 = findViewById(R.id.symptomTA8)

        //prep arrays
        symptomcontainer = arrayOf(
            container1,
            container2,
            container3,
            container4,
            container5,
            container6,
            container7,
            container8
        )
        sSpinners = arrayOf(sSpin1, sSpin2, sSpin3, sSpin4, sSpin5, sSpin6, sSpin7, sSpin8)
        iSpinners = arrayOf(iSpin1, iSpin2, iSpin3, iSpin4, iSpin5, iSpin6, iSpin7, iSpin8)
        symptomTA = arrayOf(TA1, TA2, TA3, TA4, TA5, TA6, TA7, TA8)

        val timeLabel = findViewById<TextView>(R.id.timeLabel2)
        dateTime= intent.getStringExtra("Time").toString()
        timeLabel.text = dateTime

        note=findViewById<EditText>(R.id.notesText).text.toString()

        val symptomList = resources.getStringArray(R.array.symptoms)
        for (i in sSpinners.indices) {
            if (sSpinners[i] != null) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, symptomList
                )
                sSpinners[i].adapter = adapter
                sSpinners[i]?.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position==7){
                            symptomTA[i].visibility=View.VISIBLE
                        }else symptomTA[i].visibility=View.GONE
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        /* no-op */
                    }
                }
            }
        }

        val intensityList = resources.getStringArray(R.array.intensity)
        for (i in sSpinners.indices) {
            if (iSpinners[i] != null) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, intensityList
                )
                iSpinners[i].adapter = adapter
            }
        }


        addSymptomBtn=findViewById<Button>(R.id.addSymptomButton)
        addSymptomBtn.setOnClickListener(){
            addSymptom()
        }
        removeSymptomBtn=findViewById<Button>(R.id.removeSymptomButton)
        removeSymptomBtn.setOnClickListener(){
            removeSymptom()
        }
        val saveBtn = findViewById<Button>(R.id.suspendButton)
        saveBtn.setOnClickListener(){
            var symptomList = RealmList<String>()
            var intensityList = RealmList<Int>()
            for(i in 0..activeSymptoms){
                if(sSpinners[i].selectedItemPosition==7){
                    symptomList.add(symptomTA[i].text.toString())
                }else symptomList.add(sSpinners[i].selectedItem.toString())
                intensityList.add(iSpinners[i].selectedItemPosition)
            }
            val toInsert = IncompleteAttackItem(symptomList, intensityList, dateTime, note)
            realm.executeTransactionAsync { realm ->
                realm.insert(toInsert)
            }
            sharedPref.edit {
                putInt("pausedAttack", 1)
                putString("timeID", dateTime)
                apply()
            }

            finish()
        }
        val finalizeBtn = findViewById<Button>(R.id.finalizeButton)
        finalizeBtn.setOnClickListener(){
            var symptomList = RealmList<String>()
            var intensityList = RealmList<Int>()
            for(i in 0..activeSymptoms){
                if(sSpinners[i].selectedItemPosition==7){
                    symptomList.add(symptomTA[i].text.toString())
                }else symptomList.add(sSpinners[i].selectedItem.toString())
                intensityList.add(iSpinners[i].selectedItemPosition)
            }
            val timeRaw = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
            val formatted = timeRaw.format(formatter)
            val toInsert = AttackItem(symptomList, intensityList, dateTime, formatted,  note)
            realm.executeTransactionAsync { realm ->
                realm.insert(toInsert)
            }
            finish()
        }
    }
    
    private fun addSymptom(){
        activeSymptoms++
        symptomcontainer[activeSymptoms].visibility= View.VISIBLE
        if (activeSymptoms>0)removeSymptomBtn.visibility=View.VISIBLE
        if (activeSymptoms==7)addSymptomBtn.visibility=View.GONE
    }
    private fun removeSymptom(){
        symptomcontainer[activeSymptoms].visibility= View.GONE
        activeSymptoms--
        if (activeSymptoms<7)addSymptomBtn.visibility=View.VISIBLE
        if (activeSymptoms==0)removeSymptomBtn.visibility=View.GONE
    }
}