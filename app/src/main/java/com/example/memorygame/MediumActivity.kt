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

class MediumActivity : AppCompatActivity() {
    private lateinit var cards:List<MemoryCard>
    private lateinit var btns:List<ImageButton>
    private var indesxSingle:Int?=null
    lateinit var konm:KonfettiView
    lateinit var linearm:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medium)

        konm=findViewById(R.id.konm)
        linearm=findViewById(R.id.linearm)
        val button1=findViewById<ImageButton>(R.id.btnm1)
        val button2=findViewById<ImageButton>(R.id.btnm2)
        val button3=findViewById<ImageButton>(R.id.btnm3)
        val button4=findViewById<ImageButton>(R.id.btnm4)
        val button5=findViewById<ImageButton>(R.id.btnm5)
        val button6=findViewById<ImageButton>(R.id.btnm6)
        val button7=findViewById<ImageButton>(R.id.btnm7)
        val button8=findViewById<ImageButton>(R.id.btnm8)
        val button9=findViewById<ImageButton>(R.id.btnm9)
        val button10=findViewById<ImageButton>(R.id.btnm10)
        val button11=findViewById<ImageButton>(R.id.btnm11)
        val button12=findViewById<ImageButton>(R.id.btnm12)
        btns= listOf(button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12)
        val imgs= mutableListOf(
            ic_baseline_flash_on_24, ic_baseline_flight,
            ic_baseline_free_breakfast, ic_baseline_headset_24,
            ic_baseline_architecture_24, ic_baseline_adb
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
            Toast.makeText(this,"Congrats, You won the game!", Toast.LENGTH_SHORT).show()
            linearm.visibility= View.INVISIBLE
            konm.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addSizes(Size(7))
                .setPosition(-50f, konm.width + 50f, -50f, -50f)
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
        indesxSingle = if(indesxSingle==null) {
            restoreCards()
            pos
        } else {
            checkforMatch(indesxSingle!!,pos)
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
                        1 -> recreate()
                        2 -> startActivity(Intent(this,HardActivity::class.java))
                    }
                }
                val alertDialog: AlertDialog =builder.create()
                alertDialog.show()
            }
            R.id.reset->recreate()
            R.id.about-> startActivity(Intent(this,AboutUs::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}