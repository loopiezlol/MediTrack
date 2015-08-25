package ro.laflamme.meditrack;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ro.laflamme.meditrack.exception.NoGpsException;

/**
 * Created by motan on 17.05.2015.
 */
public class MediLocation implements  GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MediLocation";
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

    private static MediLocation mInstance;
    private Context mContext;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    public static MediLocation getInstance(Context mContext, GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        if (mInstance == null) {
            return new MediLocation(mContext, connectionCallbacks);
        }
        return mInstance;
    }

    public MediLocation(Context mContext, GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        this.mContext = mContext;

        if (checkGooglePlayServices()) {
            buildGoogleApiClient(connectionCallbacks);
            createLocationRequest();
        }

        connectApi();
    }

    public void connectApi() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
              /*
               * google play services is missing or update is required
               *  return code could be
               * SUCCESS,
               * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
               * SERVICE_DISABLED, SERVICE_INVALID.
               */
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    (Activity) mContext, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }
        return true;
    }


    protected synchronized void buildGoogleApiClient(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public double getLatitude(){
        return mLastLocation.getLatitude();
    }

    public double getLongitude(){
        return mLastLocation.getLongitude();
    }


    public void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    public void disconnectApi() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    public void onLocationChanged(android.location.Location location) {
        mLastLocation = location;
//        Toast.makeText(mContext, "Update -> Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Update -> Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void syncLastLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }
}
