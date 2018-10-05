package co.netguru.sectionsDecorator

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup

fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {

    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0

        override fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }

        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun <R,T> Map<R, List<T>>.addToValueList(key: R, element: T): Map<R, List<T>> {
    val mutable = this.toMutableMap()
    mutable[key] = (this[key] ?: emptyList()) + element
    return mutable
}

inline fun <A : Any, B : Any> nullCheck2(item1: A?, item2: B?, f: (A, B) -> Unit) {
    if (item1 != null && item2 != null) {
        f(item1, item2)
    }
}
