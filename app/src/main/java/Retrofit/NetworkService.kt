package Retrofit

import Pojo.User
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {

    /*---------------------HTTP METHODS-----------------------------*/
    //TODO поставить рабочие методы!
//
//    // makes http get request with header
//    @GET("dummy/list") // endpoint will be appended to the base url
//    fun doDummyGetRequestCall(
//        @Header("some-header-key") someHeader: String
//    ): Single<String>
//
//    // makes http post request with body and header
//    @POST("dummy/list")
//    fun doDummyPostRequestCall(
//        @Body request: String,
//        @Header("some-header-key") someHeader: String,
//        @Header("some-more-header-key") someMoreHeader: String
//    ): Observable<String>


    @GET("users")
    fun getUsers(): Observable<List<User>>


    // ----- the same for corutines

    @GET("users")
    suspend fun getUsersInCorutine():Response<List<User>>

}