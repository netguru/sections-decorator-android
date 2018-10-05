package co.netguru.android.sectionsDecoratorDemo.common.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.replaceFragment(
    @IdRes container: Int,
    tag: String,
    createFragment: () -> Fragment
) {
    val foundFragment = findFragmentByTag(tag) ?: createFragment()
    val transaction = beginTransaction()
    transaction.replace(container, foundFragment)
    transaction.commit()
}
