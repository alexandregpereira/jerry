package br.alexandregpereira.jerry

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

const val ANIMATION_STIFFNESS = 600f

fun View.cancelSpringAnimation(): View {
    getSpringAnimationKeys().forEach {
        cancelSpringAnimation(key = it)
    }
    return this
}

fun View.cancelSpringAnimation(key: Int): View {
    getSpringAnimation(key)?.cancel()
    return this
}

fun View.skipToEndSpringAnimation(): View {
    getSpringAnimationKeys().forEach {
        skipToEndSpringAnimation(key = it)
    }
    return this
}

fun View.skipToEndSpringAnimation(key: Int): View {
    getSpringAnimation(key)?.takeIf { it.canSkipToEnd() }?.skipToEnd()
    return this
}

/**
 *
 */
fun View.spring(
    key: Int,
    property: FloatPropertyCompat<View>,
    targetValue: Float
): JerryAnimation {
    var springAnimation = getSpringAnimation(key)
    if (springAnimation == null) {
        springAnimation = SpringAnimation(this, property).apply {
            spring = SpringForce()
        }
        addSpringAnimationKeyIfNotContains(key)
        setTag(key, SpringAnimationHolder(springAnimation))
    }
    return JerryAnimation(key = key, view = this, springAnimation, targetValue)
}

fun JerryAnimation.spring(
    key: Int,
    property: FloatPropertyCompat<View>,
    targetValue: Float
): JerryAnimationSet {
    return animationSet().spring(
        key = key,
        property = property,
        targetValue = targetValue
    )
}

fun JerryAnimationSet.spring(
    key: Int,
    property: FloatPropertyCompat<View>,
    targetValue: Float
): JerryAnimationSet {
    return this.copy(
        jerryAnimations = jerryAnimations +
                listOf(
                    jerryAnimations.last().view.spring(
                        key, property, targetValue
                    )
                )
    )
}

fun JerryAnimation.force(
    stiffness: Float = ANIMATION_STIFFNESS,
    dampingRatio: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY
): JerryAnimation {
    val springForce = SpringForce().apply {
        this.dampingRatio = dampingRatio
        this.stiffness = stiffness
    }
    return this.copy(
        springForce = springForce
    )
}

fun JerryAnimationSet.force(
    stiffness: Float = ANIMATION_STIFFNESS,
    dampingRatio: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY
): JerryAnimationSet {
    return this.copy(
        jerryAnimations = jerryAnimations.map { it.force(stiffness, dampingRatio) }
    )
}

fun JerryAnimationSet.lastForce(
    stiffness: Float = ANIMATION_STIFFNESS,
    dampingRatio: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY
): JerryAnimationSet {
    return this.copy(
        jerryAnimations = jerryAnimations.subList(0, jerryAnimations.size - 1) +
                jerryAnimations.last().force(stiffness, dampingRatio)
    )
}

private fun JerryAnimation.animationSet(): JerryAnimationSet {
    return JerryAnimationSet(jerryAnimations = listOf(this))
}

fun JerryAnimation.after(jerryAnimationSet: JerryAnimationSet): JerryAnimationSet {
    return animationSet().after(jerryAnimationSet)
}

fun JerryAnimation.after(jerryAnimation: JerryAnimation): JerryAnimationSet {
    return animationSet().after(jerryAnimation)
}

fun JerryAnimationSet.after(jerryAnimationSet: JerryAnimationSet): JerryAnimationSet {
    return this.copy(jerryAfterAnimationSet = jerryAnimationSet)
}

fun JerryAnimationSet.after(jerryAnimation: JerryAnimation): JerryAnimationSet {
    return this.copy(jerryAfterAnimationSet = jerryAnimation.animationSet())
}

fun JerryAnimation.add(
    jerryAnimationSet: JerryAnimationSet
): JerryAnimationSet {
    return animationSet().add(jerryAnimationSet)
}

fun JerryAnimationSet.add(
    jerryAnimationSet: JerryAnimationSet
): JerryAnimationSet {
    return this.copy(jerryAnimations = this.jerryAnimations + jerryAnimationSet.jerryAnimations)
}

fun JerryAnimationSet.start(
    onAnimationEnd: ((canceled: Boolean) -> Unit)? = null
) {
    val onAnimationsEnd: (canceled: Boolean, completed: Boolean) -> Unit = { canceled, completed ->
        if (completed) {
            if (canceled || jerryAfterAnimationSet == null) {
                onAnimationEnd?.invoke(canceled)
            } else {
                jerryAfterAnimationSet.start(onAnimationEnd)
            }
        }
    }
    return jerryAnimations.forEach { animation ->
        val otherAnimations = jerryAnimations.filterNot { it.key == animation.key }
        animation.start { canceled ->
            onAnimationsEnd(
                canceled,
                otherAnimations.isRunning().not()
            )
        }
    }
}

fun JerryAnimation.start(
    onAnimationEnd: ((canceled: Boolean) -> Unit)? = null
) {
    this.addSpringEndListener(
        onAnimationEnd = onAnimationEnd
    )

    springAnimation.spring.apply {
        this.dampingRatio = springForce?.dampingRatio ?: SpringForce.DAMPING_RATIO_NO_BOUNCY
        this.stiffness = springForce?.stiffness ?: ANIMATION_STIFFNESS
    }
    springAnimation.animateToFinalPosition(targetValue)
}

fun List<JerryAnimation>.isRunning(): Boolean {
    return isNotEmpty() && map { it.isRunning }.reduce { acc, isRunning -> acc || isRunning }
}

internal fun JerryAnimation.addSpringEndListener(
    onAnimationEnd: ((canceled: Boolean) -> Unit)?
) {
    view.getSpringEndListener(key = key)?.also { endListener ->
        springAnimation.removeEndListener(endListener)
    }

    if (onAnimationEnd == null) return

    DynamicAnimation.OnAnimationEndListener { animation, canceled, _, _ ->
        view.getSpringEndListener(key)?.let {
            animation.removeEndListener(it)
        }
        onAnimationEnd(canceled)
    }.let { endListener ->
        view.getSpringAnimationHolder(key)?.let { holder ->
            this@addSpringEndListener.springAnimation.addEndListener(endListener)
            view.setTag(key, holder.copy(onAnimationEndListener = endListener))
        } ?: throw IllegalStateException("You should call spring method before")
    }
}

fun View.getSpringAnimation(key: Int): SpringAnimation? {
    return getSpringAnimationHolder(key)?.springAnimation
}

private fun View.getSpringEndListener(key: Int): DynamicAnimation.OnAnimationEndListener? {
    return getSpringAnimationHolder(key)?.onAnimationEndListener
}

private fun View.getSpringAnimationHolder(key: Int): SpringAnimationHolder? {
    return getTag(key).run {
        this as? SpringAnimationHolder
    }
}

private fun View.getSpringAnimationKeys(): List<Int> {
    return getTag(R.string.keys_key).run {
        this as? List<*>
    }?.mapNotNull {
        it as? Int
    } ?: listOf<Int>().apply {
        setTag(R.string.keys_key, this)
    }
}

private fun View.addSpringAnimationKeyIfNotContains(key: Int) {
    val animationKeys = getSpringAnimationKeys()
    if (animationKeys.contains(key).not()) {
        setTag(R.string.keys_key, animationKeys.toMutableList().apply {
            add(key)
        })
    }
}
