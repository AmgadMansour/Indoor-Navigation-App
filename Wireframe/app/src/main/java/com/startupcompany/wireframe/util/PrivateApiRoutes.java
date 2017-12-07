package com.startupcompany.wireframe.util;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

import com.startupcompany.wireframe.model.Beacon;
import com.startupcompany.wireframe.model.Category;
import com.startupcompany.wireframe.model.Destination;
import com.startupcompany.wireframe.model.Graph;
import com.startupcompany.wireframe.model.Map;
import com.startupcompany.wireframe.model.Room;
import com.startupcompany.wireframe.model.Transition;
import com.startupcompany.wireframe.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

public interface PrivateApiRoutes {
    // USERS
    @GET("/users/{id}/user_destinations")
    void getUserDestinations(@Path("id") long userId,
                 Callback<List<Destination>> callback);

    @GET("/users/{id}")
    void getUser(@Path("id") long userId,
                 Callback<User> callback);

    @POST("/users/{id}/update_location")
    @FormUrlEncoded
    void updateLocation(@Path("id") long userId,
                        @Field("user[x_coordinate]") float xCoordinate,
                         @Field("user[y_coordinate]") float yCoordinate,
                        @Field("user[room_id]") long roomId,
                         Callback<User> callback);

    // DESTINATIONS
    @GET("/destinations/{id}")
    void getDestination(@Path("id") long destinationID,
                        Callback<Destination> callback);

    @GET("/destinations/map/{map_id}")
    void getDestinations(@Path("map_id") long mapId,
                 Callback<List<Destination>> callback);

    @GET("/destinations/map/{map_id}/{category}")
    void getDestinationsByCategory(@Path("map_id") long mapId,
                                  @Path("category") long category,
                 Callback<List<Destination>> callback);

    // CATEGORIES
    @GET("/categories/{map_id}")
    void getCategories(@Path("map_id") long mapId,
                       Callback<List<Category>> callback);

    // TRANSITIONS
    @GET("/transitions/{map_id}")
    void getTransitionNodes(@Path("map_id") long mapId,
                 Callback<List<Transition>> callback);

    // BEACONS
    @GET("/beacons/{map_id}")
    void getBeacons(@Path("map_id") long mapId,
                 Callback<List<Beacon>> callback);

    // GRAPHS
    @GET("/graphs/{map_id}")
    void getMapGraph(@Path("map_id") long mapId,
                 Callback<Graph> callback);

    // ROOMS
    @GET("/rooms/{map_id}")
    void getMapRooms(@Path("map_id") long mapId,
                     Callback<List<Room>> callback);

    // MAPS
    @GET("/maps/{id}")
    void getMap(@Path("id") long mapId,
                 Callback<Map> callback);
}
