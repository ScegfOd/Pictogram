package com.example.pictogram;

import com.example.pictogram.Model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class Pictogram extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cZIebRpz9B2Yn05BR1KBdmTS9IdEWEKfmJqHbqbq")
                .clientKey("g2i9Xnv2DTa5vmN81Ub4KIpn4HYbNuZITFhA9lNg")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}