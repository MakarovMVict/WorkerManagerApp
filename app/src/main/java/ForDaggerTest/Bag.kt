package ForDaggerTest

import dagger.Module
import dagger.Provides

@Module
class Bag {//включается как модуль(в данном случае в DaggerComponent)
    @Provides
    open fun sayLoveDagger2(): Info {//содержит функцию,возвращающую объект(?) класса Info
        return Info("Love Dagger 2")
    }
}