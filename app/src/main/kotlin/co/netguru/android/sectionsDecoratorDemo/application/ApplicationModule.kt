package co.netguru.android.sectionsDecoratorDemo.application

import co.netguru.android.sectionsDecoratorDemo.application.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @AppScope
    @Provides
    fun rxJavaErrorHandler(): RxJavaErrorHandler = RxJavaErrorHandlerImpl()
}
