package com.example.memorygame

import android.content.Intent
import android.graphics.Color
import android.net.Uri
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

class HardActivity : AppCompatActivity() {
    private lateinit var cards:List<MemoryCard>
    private lateinit var btns:List<ImageButton>
    private var indexSingle:Int?=null
    lateinit var lh:LinearLayout
    lateinit var konh:KonfettiView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard)

        lh=findViewById(R.id.linearh)
        konh=findViewById(R.id.konh)
        val button1=findViewById<ImageButton>(R.id.btnh1)
        val button2=findViewById<ImageButton>(R.id.btnh2)
        val button3=findViewById<ImageButton>(R.id.btnh3)
        val button4=findViewById<ImageButton>(R.id.btnh4)
        val button5=findViewById<ImageButton>(R.id.btnh5)
        val button6=findViewById<ImageButton>(R.id.btnh6)
        val button7=findViewById<ImageButton>(R.id.btnh7)
        val button8=findViewById<ImageButton>(R.id.btnh8)
        val button9=findViewById<ImageButton>(R.id.btnh9)
        val button10=findViewById<ImageButton>(R.id.btnh10)
        val button11=findViewById<ImageButton>(R.id.btnh11)
        val button12=findViewById<ImageButton>(R.id.btnh12)
        val button13=findViewById<ImageButton>(R.id.btnh13)
        val button14=findViewById<ImageButton>(R.id.btnh14)
        val button15=findViewById<ImageButton>(R.id.btnh15)
        val button16=findViewById<ImageButton>(R.id.btnh16)
        val button17=findViewById<ImageButton>(R.id.btnh17)
        val button18=findViewById<ImageButton>(R.id.btnh18)
        btns= listOf(button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12,
        button13, button14, button15, button16, button17, button18)
        val imgs= mutableListOf(
            ic_baseline_arrow_back, ic_baseline_arrow_downward_24, ic_baseline_arrow_drop_down_24,
            ic_baseline_arrow_drop_up_24, ic_baseline_arrow_forward_24, ic_baseline_arrow_left_24,
            ic_baseline_arrow_right_24, ic_baseline_arrow_upward_24, ic_baseline_arrow_back_ios_new_24
        )
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
            Toast.makeText(this,"Congrats, The day is yours!", Toast.LENGTH_SHORT).show()
            lh.visibility= View.INVISIBLE
            konh.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN,
                    Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addSizes(Size(8))
                .setPosition(-50f, konh.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }
    }

    private fun updateModels(pos:Int) {
        val card=cards[pos]
        if(card.upFace)
        {
            Toast.makeText(this,"Invalid move!", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"Match Found!", Toast.LENGTH_SHORT).show()
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
                val builder= AlertDialog.Builder(this)
                builder.setTitle("Choose Size")
                val checked=1
                val choice= arrayOf("Easy: 4X2","Medium: 4X3","Hard: 6X3")
                builder.setSingleChoiceItems(choice, checked) { dialog, which ->
                    when (which) {
                        0 -> startActivity(Intent(this,MainActivity::class.java))
                        1 -> startActivity(Intent(this,MediumActivity::class.java))
                        2 -> recreate()
                    }
                }
                val alertDialog: AlertDialog =builder.create()
                alertDialog.show()
            }
            R.id.reset->recreate()
            R.id.about-> Toast.makeText(this,"About Us!",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}