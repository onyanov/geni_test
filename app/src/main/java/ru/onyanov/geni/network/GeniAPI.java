package ru.onyanov.geni.network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import ru.onyanov.geni.models.Photo;

public interface GeniAPI {

    @GET("/api/photo-{id}")
    Call<Photo> getPhoto(@Path("id") int photoId);

}
