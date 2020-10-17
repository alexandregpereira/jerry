package br.alexandregpereira.jerry.app.animation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.alexandregpereira.jerry.app.R
import br.alexandregpereira.jerry.expandable.collapseHeight
import br.alexandregpereira.jerry.expandable.collapseWidth
import br.alexandregpereira.jerry.expandable.expandHeight
import br.alexandregpereira.jerry.expandable.expandWidth
import kotlinx.android.synthetic.main.activity_expand_animation.*

class ExpandAnimationActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ExpandAnimationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand_animation)

        expandTextButton.setOnClickListener {
            expandTextView.expandHeight()
        }

        expandCollapseTextButton.setOnClickListener {
            expandTextView.collapseHeight()
        }

        expandWidthTextButton.setOnClickListener {
            expandWidthTextView.expandWidth()
        }

        expandCollapseWidthTextButton.setOnClickListener {
            expandWidthTextView.collapseWidth()
        }

        expandFixedTextButton.setOnClickListener {
            expandFixedTextView.expandHeight()
        }

        expandCollapseFixedTextButton.setOnClickListener {
            expandFixedTextView.collapseHeight()
        }

        expandWidthTextButton.setOnClickListener {
            expandWidthTextView.expandWidth()
        }

        expandCollapseWidthTextButton.setOnClickListener {
            expandWidthTextView.collapseWidth()
        }

        expandMatchWidthButton.setOnClickListener {
            expandMatchWidthView.expandWidth()
        }

        expandCollapseMatchWidthButton.setOnClickListener {
            expandMatchWidthView.collapseWidth()
        }

        expand0dpWidthButton.setOnClickListener {
            expand0dpWidthView.expandWidth()
        }

        expandCollapse0dpWidthButton.setOnClickListener {
            expand0dpWidthView.collapseWidth()
        }

        expandHalf0dpWidthButton.setOnClickListener {
            expandHalf0dpWidthView.expandWidth()
        }

        expandCollapseHalf0dpWidthButton.setOnClickListener {
            expandHalf0dpWidthView.collapseWidth()
        }

        expandFixedWidthButton.setOnClickListener {
            expandFixedWidthView.expandWidth()
        }

        expandCollapseFixedWidthButton.setOnClickListener {
            expandFixedWidthView.collapseWidth()
        }
    }
}
