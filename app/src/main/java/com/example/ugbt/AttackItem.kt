package com.example.ugbt

import org.bson.types.ObjectId
import io.realm.MutableRealmInteger
import io.realm.RealmObject
import io.realm.RealmList
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.codecs.pojo.annotations.BsonProperty


open class AttackItem2(OAIntensity:Int = 0,
                      symptomList: RealmList<String> = RealmList("default SL"),//arrayOf("default SL"),
                      intensityList: RealmList<Int> = RealmList(-1),
                      startTime: String = "",
                      endTime: String = "",
                      note:String = "",
                      partition: String= "test"):RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var _partition:String = partition
    var OAIntensity:Int = OAIntensity
    var symptomList: RealmList<String> = symptomList
    var intensityList: RealmList<Int> = intensityList
    var startTime: String = startTime
    var endTime: String = endTime
    var note:String = note

    /*fun getId():ObjectId{
        return _id
    }
    fun setId(newId:ObjectId){
        _id=newId
    }
    fun getPartition(): String {
        return _partition
    }
    fun setPartition(newPartition:String){
        _partition=newPartition
    }
    fun getsymptomList(): Array<String>{
        return symptomList
    }
    fun setsymptomList(newSymptomList: Array<String>){
        symptomList=newSymptomList
    }
    fun getStartTime():String{
        return startTime
    }
    fun setStartTime(newStartTime:String){
        startTime=newStartTime
    }
    fun getId(){
        return _id
    }
    fun setId(newId:ObjectId){
        _id=newId
    }
    fun getId(){
        return _id
    }
    fun setId(newId:ObjectId){
        _id=newId
    }*/

    override fun toString(): String {
        return "Message: [id=$_id, symptomList=$symptomList, intensityList=$intensityList, startTime=$startTime, endTime=$endTime, note =$note partition=$_partition]"
    }
}
