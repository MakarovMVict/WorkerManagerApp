package com.mapswithme.workermanagerapp

import ForDaggerTest.DaggerDaggerComponent
import ForDaggerTest.Info
import Pojo.User
import Retrofit.NetworkService
import Retrofit.RetrofitCalss
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.View.inflate
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mapswithme.workermanagerapp.databinding.ActivityMainBinding
import com.mapswithme.workermanagerapp.databinding.ActivityMainBinding.inflate
import io.reactivex.Flowable.just
import io.reactivex.rxkotlin.Observables
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.HttpException
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.Flow
import javax.inject.Inject
import kotlin.collections.ArrayList




class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    //ui
//    lateinit var bn_background_task:Button
//    lateinit var tv_background_result:TextView


    var isBackgroundStrted:Boolean=false
    lateinit var createWorkRequest: PeriodicWorkRequest
    var myTimer = Timer()
    var counter:Int=0
    lateinit var mainHandler:Handler

    lateinit var myObservable: Observable<String>
    lateinit var mySubject:Subject<String>
    lateinit var subscriptionOne:Disposable
    lateinit var subscriptionTwo:Disposable


    lateinit var networkKlient: NetworkService

    @Inject
    lateinit var info: Info


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //-----for dagger----->
//        var component

      DaggerDaggerComponent.create().inject(this)
        println("***Dagger demonstrate info text ${info.text}")
        println("***Dagger demonstrate single member of module(Bag)  ${info.text}")



        //-----for dagger ends----/>




        Log.d("MainActivity", "**onCreate: mainactivity! ")
        //retrofit
        initRetrofit()

        myObservable  = Observable.create {
            subscriber ->
            subscriber.onNext("**Hello, World!")
            subscriber.onComplete()
        }
        val mySubscriber = object: Observer<String>{
            override fun onSubscribe(d: Disposable) {
                println("**subscriber onSubscribe")

            }

            override fun onNext(t: String) {
                println("**subscriber onNext! $t")
            }

            override fun onError(e: Throwable) {
                println("**subscriber onError!")
            }

            override fun onComplete() {
                println("**subscriber onComplete!")

            }
        }

        myObservable.subscribe(object :Observer<String> {
            override fun onSubscribe(d: Disposable) {
                println("**subscriber onSubscribe")

            }

            override fun onNext(t: String) {
                println("**subscriber onNext! $t")

             //   mySubject.onNext(t)
            }

            override fun onError(e: Throwable) {
                println("**subscriber onError!")
            }

            override fun onComplete() {
                println("**subscriber onComplete!")

            }
        })


        mySubject = PublishSubject.create<String>()

//                .doOnComplete()


        //подписываюсь с определением того что делает сабжект
         subscriptionOne = mySubject.subscribeBy (

                    onNext = { binding.tvBackgroundResultId.setText(it)/*println("** 1)$it")*/ },
                    onComplete = { println("** 1) complete") }
        )

        subscriptionTwo= mySubject.subscribeBy (

                onNext = { println("** 2)$it") },
                onComplete = { println("** 2) complete") }
        )

//        mySubject.subscribe { string ->
//            println("**Subject onNext $string")
//
//        }



//        val updateHandler = Handler()
//
//        val runnable = Runnable {
//            Log.d("**", "**dowork!!!!! ") // some action(s)
//        }
//
//        updateHandler.postDelayed(runnable, 500)


      //  val myTimer = Timer()
//        println("**corut Start")
//
//// Start a coroutine
//        GlobalScope.launch {
////            suspend {
//                delay(3000)
//                println("**corut Hello")
////            }
////            println("**corut Hello")
//        }
//
//       // Thread.sleep(5000) // wait for 2 seconds
//        println("**corut Stop")
      //  zipRx()
        //alternativeObserverWay()
        //createStringObservable().subscribe()
//        GlobalScope.launch(Dispatchers.Main){
//            fetchAndShowUser()
//        }
    }
    suspend fun fetchUser(): Int {
        return GlobalScope.async(Dispatchers.IO) {
            // make network call
           // var int:Int=7
            return@async 6

        }.await()
    }
    suspend fun fetchAndShowUser() {
        val user = fetchUser() // fetch on IO thread
        showUser(user) // back on UI thread
    }
    fun showUser(user: Int) {
        // show user
        print("**corutine int is $user")
    }

    override fun onResume(){
        super.onResume()

        createWorkRequest=

                PeriodicWorkRequest.Builder(
                        MySimpleBackgroundTask::class.java,
                        10,
                        java.util.concurrent.TimeUnit.SECONDS
                )
                        .addTag("MySimpleBackgroundTask")
                        //     .setInputData(data)
                        .build()

//        tv_background_result=findViewById(R.id.tv_background_result_id)
//        bn_background_task=findViewById(R.id.bn_background_task_id)
        initHandler()
        setClickListeners()

    }


    fun setClickListeners(){
        binding.bnBackgroundTaskId.setOnClickListener(View.OnClickListener {
//            if(!isBackgroundStrted){
//                Log.i("app", "**back not started!")
//                if(myTimer==null){
//                    myTimer=Timer()
//                }
//                myTimer.schedule(object : TimerTask() {
//                    // Определяем задачу
//                    override fun run() {
//                        counter++
//                        Log.d("MainActivity", "**task mainactivity! $counter ")
//                        mainHandler.post {
//                            Log.d("MainActivity", "**task handler mainactivity! $counter ")
//
//                            tv_background_result.setText("counter is $counter")
//
//                        }
//                        mainHandler.sendEmptyMessage(0)
//
//
////            uiHandler.post(new Runnable() {
////                @Override
////                public void run() {
////                    txtResult.setText(result);
////                }
////            });
//                    }
//                }, 0L, 20L * 100)
//
//               // WorkManager.getInstance().enqueue(createWorkRequest)
//
//            }else{
//                Log.i("app", "**back finished!")
//                myTimer.cancel()
//                counter=0//можно перезапустить таймер - ту же таску!!
//                myTimer=Timer()
//
//             //   WorkManager.getInstance().cancelAllWorkByTag("MySimpleBackgroundTask")
//            }
//            isBackgroundStrted=!isBackgroundStrted
//            myObservable.subscribe(object :Observer<String> {
//                override fun onSubscribe(d: Disposable) {
//                    println("**subscriber onSubscribe")
//
//                }
//
//                override fun onNext(t: String) {
//                    println("**subscriber onNext! $t")
//                    mySubject.onNext(t)
//                }
//
//                override fun onError(e: Throwable) {
//                    println("**subscriber onError!")
//                }
//
//                override fun onComplete() {
//                    println("**subscriber onComplete!")
//                    mySubject.onComplete()
////                    subscriptionOne.dispose()
////                    subscriptionTwo.dispose()
//
//
//                }
//            })

            //click with rx and retrofit
            doRequest()
        })
        binding.bnCorutineId.setOnClickListener(View.OnClickListener {
            doRequestCorutine()
        })



    }

    fun initHandler(){
        mainHandler= object:Handler(mainLooper){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if(msg.what==0){
                    Log.i("app", "**WHAT is 0")
                }
            }
        }

    }
    fun  zipRx(){
        var ints:ArrayList<Int> =ArrayList()
        val numbers = Observable.range(1, 6)

        val strings =Observable.just(1,2,3,4,4,6)

//        var observerStrings = strings.subscribeBy(
//                onNext={println()}
//        )

//        just("One", "Two", "Three",
//
//                "Four", "Five", "Six" )


//        val zipped = Observables.zip(strings,numbers, fun(s: Int, n: Int) {
//            if(s==n){
//               ints.add(s)
//
//            }
//           // return "**observable $s $n"
//        }).doOnComplete { println("**rx complete ${ints.toString()}") }
//        zipped.subscribe()
    }
    fun createStringObservable(): Observable<String> {
        var myObservable: Observable<String> = Observable.create {
            subscriber ->
            subscriber.onNext("**Hello, World!")
            subscriber.onComplete()
        }

        return myObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun createExistingObservable(){

    }

    fun createStringSubscriber(): Subscriber<String> {
        val mySubscriber = object: Subscriber<String> {
            override fun onNext(s: String) {
                println(s)
            }

            override fun onComplete() {
                println("**onComplete")
            }

            override fun onError(e: Throwable) {
                println("**onError")
            }

            override fun onSubscribe(s: Subscription?) {
                println("**onSubscribe")
            }
        }

        return mySubscriber
    }
    fun alternativeObserverWay(){
//        createStringObservable()
//              //  .doOnSubscribe{data->print("**data is $data")}
//                .subscribe {
//                    Consumer<String> {
//
//
//                    }
//                }
//        val mySubscriber = object: Subscriber<String> {
//            override fun onNext(s: String) {
//                println(s)
//            }
//
//            override fun onComplete() {
//                println("**onComplete")
//            }
//
//            override fun onError(e: Throwable) {
//                println("**onError")
//            }
//
//            override fun onSubscribe(s: Subscription?) {
//                println("**onSubscribe")
//            }
//            Observable getObservable() {
//                return Observable.create(subscriber -> {
//                    subscriber.onNext(gettingValue(1));
//                    subscriber.onNext(gettingValue(2));
//
//                    subscriber.add(Subscriptions.create(() -> {
//                    LOGGER.info("Clear resources");
//                }));
//                });
//            }
//        createStringSubscriber()
    }

    fun initRetrofit() {
        networkKlient = RetrofitCalss.getClient().create(NetworkService::class.java)


    }
    fun doRequest(){
        networkKlient.getUsers().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :Observer<List<User>> {
                    override fun onSubscribe(d: Disposable) {
                        println("**observer RX subscribed!")
                    }

                    override fun onNext(t: List<User>) {
                        println("**observer RX onNext! ${t.get(1).name}")
                        mySubject.onNext(t.get(1).name)

                    }

                    override fun onError(e: Throwable) {
                        print("**observer rx onError ${e.message}")
                    }

                    override fun onComplete() {
                        println("**observer rx onCompleted")
                    }

                })

    }
    fun doRequestCorutine(){
        CoroutineScope(Dispatchers.IO).launch {
            var response = networkKlient.getUsersInCorutine()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        //Do something with response e.g show to the UI.
                        binding.tvBackgroundResultId.setText("**from corutine ${response.body()?.get(0)?.name}")
                    } else {
                          println("**Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    println("**Exception ${e.message}")
                } catch (e: Throwable) {
                    println("**Ooops: Something else went wrong")
                }
            }
        }
    }
}