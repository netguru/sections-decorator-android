package co.netguru.android.sectionsDecoratorDemo.application

import android.content.Context
import co.netguru.android.sectionsDecoratorDemo.application.scope.AppScope
import com.facebook.stetho.Stetho
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import javax.inject.Inject

/**
 * Helper class that initializes a set of debugging tools
 * for the debug build type and register crash manager for release type.
 * ## Debug type tools:
 * - AndroidDevMetrics
 * - Stetho
 * - StrictMode
 * - LeakCanary
 * - Timber
 *
 * ## Release type tools:
 * - CrashManager
 */
@AppScope
class DebugMetricsHelper @Inject constructor() {

    internal fun init(context: Context) {
        // LeakCanary
        if (LeakCanary.isInAnalyzerProcess(context.applicationContext as App)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(context.applicationContext as App)

        // Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build()
        )

        //Timber
        Timber.plant(Timber.DebugTree())

        //BlockCanary
        BlockCanary.install(context, BlockCanaryContext()).start()
    }
}
