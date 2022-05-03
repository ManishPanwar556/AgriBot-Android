package com.example.agribot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.GridView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainActivity : AppCompatActivity() {
    private val list= arrayListOf<String>("Humidity 0","Soil Moisture 0","Temprature 0","Water Level")
    private val db by lazy{
        FirebaseDatabase.getInstance()
    }
    var humid=""
    var temper=""
    var water=""
    var soil =""
   lateinit var textToSpeech:TextToSpeech
    val adapter=GridAdapter(this,list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val grid=findViewById<GridView>(R.id.grid)
        val cropBtn=findViewById<MaterialButton>(R.id.cropPredBtn)
        val micBtn=findViewById<FloatingActionButton>(R.id.mic)
        textToSpeech=
            TextToSpeech(applicationContext) {

            }
        micBtn.setOnClickListener {
            textToSpeech.setLanguage(Locale.forLanguageTag("hin"));
            textToSpeech.speak("आर्द्रता है ${humid}, वर्तमान तापमान ${temper} है,वर्तमान मिट्टी की नमी ${soil} है वर्तमान जल स्तर ${water} है",TextToSpeech.QUEUE_FLUSH,null,null)
        }


        cropBtn.setOnClickListener {
            val url = "https://agribot-project.herokuapp.com/";
            val intent =  Intent(Intent.ACTION_VIEW);
            intent.data=Uri.parse(url)
            startActivity(intent)
        }
        grid.adapter=adapter
        addListener()


    }

    fun addListener(){
        val humidity=db.getReference("Humidity")
        val temp=db.getReference("Temprature")
        val soilMoisture=db.getReference("SoilMoisture")
        val waterLevel=db.getReference("WaterLevel")


        humidity.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(String::class.java)
                data?.let{
                    humid=it
                    adapter.modifyData(0,"Humidity $data")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        soilMoisture.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(String::class.java)
                data?.let{
                    val res=(it.toInt()/1024)*100
                    val percent=if(res==100){
                        0
                    }
                    else{
                        res
                    }
                    soil="$percent %"
                    adapter.modifyData(1,"Soil Moisture $soil")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        temp.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(String::class.java)
                data?.let{
                    temper=it
                    adapter.modifyData(2,"Temperature $data")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        waterLevel.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(String::class.java)
                data?.let{
                    water=it
                    adapter.modifyData(3,"Water Level $data")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    override fun onPause() {
        super.onPause()
    }
}