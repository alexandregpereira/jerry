package br.alexandregpereira.jerry

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation

data class JerryAnimation(
    val key: Int,
    val view: View,
    val springAnimation: SpringAnimation,
    internal val targetValue: Float? = null,
    internal val jerryAnimations: List<JerryAnimation>? = null
) {
    val isRunning: Boolean
        get() = springAnimation.isRunning
}

data class JerryAnimationSet(
    internal val jerryAnimations: List<JerryAnimation>,
    internal val jerryAfterAnimationSet: JerryAnimationSet ? = null,
)