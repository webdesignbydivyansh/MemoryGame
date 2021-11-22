package com.example.memorygame

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val constraint=findViewById<LinearLayout>(R.id.mainLayout)
        val anime:AnimationDrawable= constraint.background as AnimationDrawable
        anime.setEnterFadeDuration(500)
        anime.setExitFadeDuration(3500)
        anime.start()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val txtView=findViewById<TextView>(R.id.txtView)
        val anim=AnimationUtils.loadAnimation(this,R.anim.fade_out)
        txtView.startAnimation(anim)
        Handler().postDelayed({
            txtView.visibility=View.GONE
            val i= Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        },3500)
    }
}