package br.alexandregpereira.jerry.app.animation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.alexandregpereira.jerry.app.R
import br.alexandregpereira.jerry.fadeOut
import br.alexandregpereira.jerry.textview.setTextFade
import br.alexandregpereira.jerry.fadeIn
import kotlinx.android.synthetic.main.activity_fade_animation.*

class FadeAnimationActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, FadeAnimationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fade_animation)

        val list = listOf(
            "Fade text changeasaaaaa 2",
            "Fade text change asda sdas 3",
            "asdasd asdasdasd 4",
            "Fade asdasdasddas change 5",
            "Fade text change 1",
        ).circularIterator()
        fadeTextButton.setOnClickListener {
            fadeTextView.setTextFade(list.next())
        }

        goneTextButton.setOnClickListener {
            goneTextView.fadeOut()
        }

        goneVisibleTextButton.setOnClickListener {
            goneTextView.fadeIn()
        }

        visibleTextButton.setOnClickListener {
            visibleTextView.fadeIn()
        }

        visibleInvisibleTextButton.setOnClickListener {
            visibleTextView.fadeOut()
        }
    }
}
