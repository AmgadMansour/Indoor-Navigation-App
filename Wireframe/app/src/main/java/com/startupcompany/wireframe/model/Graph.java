package com.startupcompany.wireframe.model;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

public class Graph {
    private long id;
    private String json;
    private long mapId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }
}
