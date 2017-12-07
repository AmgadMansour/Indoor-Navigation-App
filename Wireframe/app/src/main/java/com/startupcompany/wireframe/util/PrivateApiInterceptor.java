package com.startupcompany.wireframe.util;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

import retrofit.RequestInterceptor;

public class PrivateApiInterceptor implements RequestInterceptor {
    private String token;

    public PrivateApiInterceptor(String token) {
        this.token = token;
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        request.addHeader("Authorization", token);
    }
}
