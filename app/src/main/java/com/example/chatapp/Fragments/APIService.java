package com.example.chatapp.Fragments;

import com.example.chatapp.Notifications.MyResponse;
import com.example.chatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAApCZHt_c:APA91bEL4Vj6xMT4iJ0rBA69-rxYhIhMN8fYGa76lFdEjkiR4Yjl8G6DUThj0UW5yaOFM6-vurskCNsh7giikphWdchrHDPXUqpKAi1qg43-MUFtz7QDSsvs0_nUUeardv_2xjfK1SzT"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
