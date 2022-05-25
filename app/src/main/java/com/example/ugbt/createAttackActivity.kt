package com.example.ugbt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_attack)
        //prep containers
        container1=findViewById(R.id.symptomContainer1)
        container2=findViewById(R.id.symptomContainer2)
        container3=findViewById(R.id.symptomContainer3)
        container4=findViewById(R.id.symptomContainer4)
        container5=findViewById(R.id.symptomContainer5)
        container6=findViewById(R.id.symptomContainer6)
        container7=findViewById(R.id.symptomContainer7)
        container8=findViewById(R.id.symptomContainer8)

        //prep symptom spinners
        sSpin1=findViewById(R.id.symptomSpinner1)
        sSpin2=findViewById(R.id.symptomSpinner2)
        sSpin3=findViewById(R.id.symptomSpinner3)
        sSpin4=findViewById(R.id.symptomSpinner4)
        sSpin5=findViewById(R.id.symptomSpinner5)
        sSpin6=findViewById(R.id.symptomSpinner6)
        sSpin7=findViewById(R.id.symptomSpinner7)
        sSpin8=findViewById(R.id.symptomSpinner8)
        
        //prep intensity spinners
        iSpin1=findViewById(R.id.intensitySpinner1)
        iSpin2=findViewById(R.id.intensitySpinner2)
        iSpin3=findViewById(R.id.intensitySpinner3)
        iSpin4=findViewById(R.id.intensitySpinner4)
        iSpin5=findViewById(R.id.intensitySpinner5)
        iSpin6=findViewById(R.id.intensitySpinner6)
        iSpin7=findViewById(R.id.intensitySpinner7)
        iSpin8=findViewById(R.id.intensitySpinner8)

        //prep symptom text areas
        TA1=findViewById(R.id.symptomTA1)
        TA2=findViewById(R.id.symptomTA2)
        TA3=findViewById(R.id.symptomTA3)
        TA4=findViewById(R.id.symptomTA4)
        TA5=findViewById(R.id.symptomTA5)
        TA6=findViewById(R.id.symptomTA6)
        TA7=findViewById(R.id.symptomTA7)
        TA8=findViewById(R.id.symptomTA8)

        //prep arrays
        symptomcontainer = arrayOf(container1,container2,container3,container4,container5,container6,container7,container8)
        sSpinners = arrayOf(sSpin1, sSpin2, sSpin3, sSpin4, sSpin5, sSpin6, sSpin7, sSpin8)
        iSpinners = arrayOf(iSpin1, iSpin2, iSpin3, iSpin4, iSpin5, iSpin6, iSpin7, iSpin8)
        symptomTA = arrayOf(TA1,TA2,TA3,TA4,TA5,TA6,TA7,TA8)

        val symptomList = resources.getStringArray(R.array.symptoms)
        for (i in sSpinners.indices) {
            if (sSpinners[i] != null) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, symptomList
                )
                sSpinners[i].adapter = adapter
                sSpinners[i].setOnItemSelectedListener() {

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

        }
        val finalizeBtn = findViewById<Button>(R.id.finalizeButton)
        finalizeBtn.setOnClickListener(){

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