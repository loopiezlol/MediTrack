package ro.laflamme.meditrack;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by motan on 17.05.2015.
 */
public class MediLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MediLocation";
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

    private static MediLocation mInstance;
    private Context mContext;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    public static MediLocation getInstance(Context mContext) {
        if (mInstance == null) {
            return new MediLocation(mContext);
        }
        return mInstance;
    }

    public MediLocation(Context mContext) {
        this.mContext = mContext;

        if (checkGooglePlayServices()) {
            buildGoogleApiClient();

            //prepare connection request
            createLocationRequest();
        }

        connectApi();
    }

    protected void connectApi() {
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


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
//            Toast.makeText(mContext, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude());
        }

        startLocationUpdates();

    }

    public double getLatitude() {
        return mLastLocation.getLatitude();
    }

    public double getLongitude() {
        return mLastLocation.getLongitude();
    }




    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    protected void disconnectApi() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
}
