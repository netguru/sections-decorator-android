package co.netguru.android.sectionsDecoratorDemo.feature.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.netguru.android.sectionsDecoratorDemo.common.extensions.startActivity
import co.netguru.android.sectionsDecoratorDemo.feature.demo.DemoActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity<DemoActivity>()
    }
}
