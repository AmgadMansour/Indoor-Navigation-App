package no.uib.ii.algo.st8.model;

/**
 * Created by Raoof on 1/13/2016.
 */
public class BeaconLocation {
    private double x_location;
    private double y_location;
    private double z_location;
    private int id;
    public BeaconLocation() {
        x_location = 0.0;
        y_location = 0.0;
        z_location = 0.0;
        id=0;
    }

    public BeaconLocation(double x_location, double y_location, double z_location,int id) {
        this.x_location = x_location;
        this.y_location = y_location;
        this.z_location = z_location;
        this.id=id;
    }

    public double getX() {
        return x_location;
    }

    public void setX(double x_location) {
        this.x_location = x_location;
    }

    public double getY() {
        return y_location;
    }

    public void setY(double y_location) {
        this.y_location = y_location;
    }

    public double getZ() {
        return z_location;
    }

    public void setZ(double z_location) {
        this.z_location = z_location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Object l) {
        BeaconLocation beaconLocation = (BeaconLocation) l;
        if (x_location == beaconLocation.getX() && y_location == beaconLocation.getY() && z_location == beaconLocation.getZ()) {
            return true;
        }
        return false;

    }
}

