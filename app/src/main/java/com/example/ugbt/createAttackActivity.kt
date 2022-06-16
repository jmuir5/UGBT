package com.example.ugbt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import io.realm.Realm
import io.realm.RealmConfiguration
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

    lateinit var OASpinner: Spinner

    lateinit var TA1:EditText
    lateinit var TA2:EditText
    lateinit var TA3:EditText
    lateinit var TA4:EditText
    lateinit var TA5:EditText
    lateinit var TA6:EditText
    lateinit var TA7:EditText
    lateinit var TA8:EditText


    lateinit var cancelBtn:Button
    lateinit var addSymptomBtn:Button
    lateinit var removeSymptomBtn:Button
    
    var activeSymptoms=0

    lateinit var symptomcontainer:Array<ConstraintLayout>
    lateinit var sSpinners:Array<Spinner>
    lateinit var iSpinners:Array<Spinner>
    lateinit var symptomTA:Array<EditText>

    lateinit var dateTime:String
    lateinit var note:EditText
    lateinit var trigger:EditText

    private var user: User? = null
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_attack)

        //prep realm
        user = UGBTApp.currentUser()
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@createAttackActivity.realm = realm
                //setUpRecyclerView(realm)
            }
        })
        //PREP PREFERENCES
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

        OASpinner = findViewById(R.id.OAintensitySpinner)

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


        //prep other things
        val timeLabel = findViewById<TextView>(R.id.timeLabel2)
        dateTime= intent.getStringExtra("Time").toString()
        timeLabel.text = dateTime

        trigger = findViewById(R.id.triggerTxt)


        //populate spinners
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
                OASpinner.adapter = adapter

            }
        }


        //prepare buttons and atach listeners


        addSymptomBtn=findViewById<Button>(R.id.addSymptomButton)
        addSymptomBtn.setOnClickListener(){
            addSymptom()
        }
        removeSymptomBtn=findViewById<Button>(R.id.removeSymptomButton)
        removeSymptomBtn.setOnClickListener(){
            removeSymptom()
        }
        note=findViewById<EditText>(R.id.notesText)

        cancelBtn=findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener(){
            sharedPref.edit{
                putInt("pausedAttack", 0)
                putInt("sCount", 0)
                apply()

            }
            finish()
        }

        val saveBtn = findViewById<Button>(R.id.suspendButton)
        saveBtn.setOnClickListener(){
            note=findViewById<EditText>(R.id.notesText)

            var trueCount = 0
            var symptomList = RealmList<String>()
            var intensityList = RealmList<Int>()
            for(i in 0..activeSymptoms) {
                if (sSpinners[i].selectedItemPosition != 0) {
                    Log.e("tag", "selectItemPosition = ${sSpinners[i].selectedItemPosition}")

                    trueCount++
                    if (sSpinners[i].selectedItemPosition == 7) {
                        symptomList.add(symptomTA[i].text.toString())
                    } else symptomList.add(sSpinners[i].selectedItem.toString())
                    intensityList.add(iSpinners[i].selectedItemPosition)
                }
            }
            if(trueCount==0){
                sharedPref.edit{
                    putInt("pausedAttack", 0)
                    putInt("sCount", 0)
                    apply()

                }
                finish()
            }
            else {
                sharedPref.edit {
                    putInt("pausedAttack", 1)
                    putString("startTime", dateTime)
                    putString("trigger", trigger.text.toString())
                    val symptom = "symptom"
                    val intensity = "intensity"
                    if (symptomList.size > 0) {
                        Log.e("symptomList", symptomList.toString())
                        Log.e("activeSymptoms", trueCount.toString())

                        for (i in 0..trueCount - 1) {
                            putString(symptom + i.toString(), symptomList[i])
                            if (symptomList[i] == "Select Symptom") Log.e(
                                "tag",
                                "select symptom added"
                            )
                            if (symptomList[i] == "select symptom") Log.e(
                                "tag",
                                "select symptom added"
                            )

                            putInt(intensity + i.toString(), intensityList[i]!!)
                        }
                        putString("note", note.text.toString())
                        putInt("OAintensity", OASpinner.selectedItemPosition)
                        putInt("sCount", trueCount)
                        apply()
                    }
                }

                activeSymptoms = 0
                finish()
            }
        }
        val finalizeBtn = findViewById<Button>(R.id.finalizeButton)
        finalizeBtn.setOnClickListener(){

            var trueCount = 0
            var symptomList = RealmList<String>()
            var intensityList = RealmList<Int>()
            for(i in 0..activeSymptoms) {
                if (sSpinners[i].selectedItemPosition != 0) {
                    trueCount++
                    if (sSpinners[i].selectedItemPosition == 7) {
                        symptomList.add(symptomTA[i].text.toString())
                    } else symptomList.add(sSpinners[i].selectedItem.toString())
                    intensityList.add(iSpinners[i].selectedItemPosition)
                }
            }
            val timeRaw = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
            val formatted = timeRaw.format(formatter)
            val formatter2 = DateTimeFormatter.ofPattern("D")
            val ordinal: Int = timeRaw.format(formatter2).toInt()
            val food = trigger.text.toString()

            val toInsert = AttackItem2(food, OASpinner.selectedItemPosition, symptomList, intensityList, dateTime, formatted, ordinal,  note.text.toString())
            realm.executeTransactionAsync { realm ->
                realm.insert(toInsert)
            }
            activeSymptoms = 0
            sharedPref.edit{
                putInt("pausedAttack", 0)
                putInt("sCount", 0)
                apply()
            }

            finish()
        }

        //populate EVERYTHING if there is an incomplete attack
        if (sharedPref.getInt("pausedAttack", 0)==1){
            activeSymptoms = 0
            timeLabel.text = sharedPref.getString("startTime", "none found")
            dateTime = timeLabel.text.toString()
            trigger.setText(sharedPref.getString("trigger", "none"))
            note.setText(sharedPref.getString("note", "none"))
            var sympcount = sharedPref.getInt("sCount", 0)
            OASpinner.setSelection(sharedPref.getInt("OAintensity", 0))
            for(i in 0..sympcount-1) {
                if(activeSymptoms==7)activeSymptoms--
                if(i!=0)addSymptom()
                when (sharedPref.getString("symptom$i", "default")) {
                    "Select Symptom"->sSpinners[i].setSelection(0)
                    "Pain" -> sSpinners[i].setSelection(1)
                    "Nausea" -> sSpinners[i].setSelection(2)
                    "Vomiting" -> sSpinners[i].setSelection(3)
                    "Diarrhea" -> sSpinners[i].setSelection(4)
                    "Fever" -> sSpinners[i].setSelection(5)
                    "Chills" -> sSpinners[i].setSelection(6)
                    else -> {
                        sSpinners[i].setSelection(7)
                        symptomTA[i].setText(sharedPref.getString("symptom$i", "select symptom"))
                    }
                }
                iSpinners[i].setSelection(sharedPref.getInt("intensity$i", 0))

            }

        }



    }

    /*<item>Pain</item>
    <item>Nausea</item>
    <item>Vomiting</item>
    <item>Diarrhea</item>
    <item>Fever</item>
    <item>Chills</item>
    <item>Other</item>*/


    
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