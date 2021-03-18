package ForDaggerTest

import com.mapswithme.workermanagerapp.MainActivity
import dagger.Component
import javax.inject.Scope

@Component(modules = [Bag::class])//включает так же и модули (в частности Bag)
interface DaggerComponent {
//    fun getCar(): Car
//    fun getEngine(): Engine
//    fun getFuel(): Fuel
     fun inject(app: MainActivity)
}

