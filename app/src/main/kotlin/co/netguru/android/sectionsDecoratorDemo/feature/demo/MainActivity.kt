package co.netguru.android.sectionsDecoratorDemo.feature.demo


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.netguru.android.sectionsDecoratorDemo.R
import co.netguru.android.sectionsDecoratorDemo.common.extensions.replaceFragment
import co.netguru.android.sectionsDecoratorDemo.feature.demo.HorizontalFragment.Companion.createFragment
import kotlinx.android.synthetic.main.activity_main.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.replaceFragment(
            R.id.content,
            HorizontalFragment.TAG,
            HorizontalFragment.Companion::createFragment
        )

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.vertical -> {
                    supportFragmentManager.replaceFragment(
                        R.id.content,
                        VerticalFragment.TAG,
                        VerticalFragment.Companion::createFragment
                    )
                }
                R.id.horizontal -> {
                    supportFragmentManager.replaceFragment(
                        R.id.content,
                        HorizontalFragment.TAG,
                        HorizontalFragment.Companion::createFragment
                    )
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
