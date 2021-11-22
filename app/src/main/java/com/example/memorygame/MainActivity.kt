package com.example.memorygame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.memorygame.R.drawable.*
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Size

class MainActivity : AppCompatActivity() {
    private lateinit var cards:List<MemoryCard>
    private lateinit var btns:List<ImageButton>
    private var indexSingle:Int?=null
    lateinit var kon:KonfettiView
    lateinit var l:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kon=findViewById(R.id.kon)
        l=findViewById(R.id.linear)
        val button1=findViewById<ImageButton>(R.id.btn1)
        val button2=findViewById<ImageButton>(R.id.btn2)
        val button3=findViewById<ImageButton>(R.id.btn3)
        val button4=findViewById<ImageButton>(R.id.btn4)
        val button5=findViewById<ImageButton>(R.id.btn5)
        val button6=findViewById<ImageButton>(R.id.btn6)
        val button7=findViewById<ImageButton>(R.id.btn7)
        val button8=findViewById<ImageButton>(R.id.btn8)
        btns= listOf(button1, button2, button3, button4, button5, button6, button7, button8)
        val imgs= mutableListOf(ic_baseline_flash_on_24, ic_baseline_flight,
            ic_baseline_free_breakfast, ic_baseline_headset_24)
        imgs.addAll(imgs)
        imgs.shuffle()
        cards=btns.indices.map {
            MemoryCard(imgs[it])
        }
        btns.forEachIndexed { idx, button ->
            button.setOnClickListener {
                updateModels(idx)
                updateViews()
            }
        }
    }

    private fun updateViews() {
        var c=0
        cards.forEachIndexed { index, card ->
            val button=btns[index]
            if(card.matching)
                button.alpha=.25F
            if (card.upFace)
                button.setImageResource(card.identify)
            else
                button.setImageResource(ic_baseline_memory)
        }
        for (card in cards)
            if(card.matching)
                c++
        if(c==cards.size)
        {
            Toast.makeText(this,"Congrats, You got lucky!",Toast.LENGTH_SHORT).show()
            l.visibility=View.INVISIBLE
            kon.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addSizes(Size(6))
                .setPosition(-50f, kon.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }
    }

    private fun updateModels(pos:Int) {
        val card=cards[pos]
        if(card.upFace)
        {
            Toast.makeText(this,"Invalid move!",Toast.LENGTH_SHORT).show()
            return
        }
        indexSingle = if(indexSingle==null) {
            restoreCards()
            pos
        } else {
            checkforMatch(indexSingle!!,pos)
            null
        }
        card.upFace=!card.upFace

    }

    private fun restoreCards() {
        for (card in cards)
        {
            if(!card.matching)
                card.upFace=false
        }
    }

    private fun checkforMatch(pos1:Int,pos2:Int) {
        if(cards[pos1].identify==cards[pos2].identify)
        {
            Toast.makeText(this,"Match Found!",Toast.LENGTH_SHORT).show()
            cards[pos1].matching=true
            cards[pos2].matching=true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.memory_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.size-> {
                val builder=AlertDialog.Builder(this)
                builder.setTitle("Choose Size")
                val checked=1
                val choice= arrayOf("Easy: 4X2","Medium: 4X3","Hard: 6X3")
                builder.setSingleChoiceItems(choice, checked) { dialog, which ->
                    when (which) {
                        0 -> recreate()
                        1 -> startActivity(Intent(this,MediumActivity::class.java))
                        2 -> startActivity(Intent(this,HardActivity::class.java))
                    }
                }
                val alertDialog:AlertDialog=builder.create()
                alertDialog.show()
            }
            R.id.reset->recreate()
            R.id.about-> startActivity(Intent(this,AboutUs::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}