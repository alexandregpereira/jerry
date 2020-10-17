package br.alexandregpereira.jerry.app.animation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import br.alexandregpereira.jerry.app.R
import br.alexandregpereira.jerry.textview.setTextExpandable
import kotlinx.android.synthetic.main.activity_text_expandable_animation.*

class TextExpandableAnimationActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, TextExpandableAnimationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_expandable_animation)

        expandCancelTextButton.setOnClickListener {
            expandCancelTextView.setTextExpandable(
                getString(R.string.expand)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                expandCancelTextView.setTextExpandable(null)
            }, 100)
        }

        collapseCancelTextButton.setOnClickListener {
            collapseCancelTextView.setTextExpandable(null)
            Handler(Looper.getMainLooper()).postDelayed({
                collapseCancelTextView.setTextExpandable(
                    getString(R.string.collapse)
                )
            }, 100)
        }

        expandFadingTextButton.setOnClickListener {
            expandFadingTextView.setTextExpandable(
                getString(R.string.expand_collapse_animation)
            )
        }

        expandCollapseFadingTextButton.setOnClickListener {
            expandFadingTextView.setTextExpandable(null)
        }

        collapseFadingTextButton.setOnClickListener {
            collapseFadingTextView.setTextExpandable(null)
        }

        collapseExpandFadingTextButton.setOnClickListener {
            collapseFadingTextView.setTextExpandable(
                getString(R.string.collapse_expand_animation)
            )
        }

        val list = listOf(
            "Fade text changeasaaaaa 2",
            "Fade text change asda sdas 3",
            "asdasd asdasdasd 4",
            "Fade asdasdasddas change 5",
            "Fade text change 1",
        ).circularIterator()
        textFadeTextButton.setOnClickListener {
            textFadeTextView.setTextExpandable(list.next())
        }
    }
}
