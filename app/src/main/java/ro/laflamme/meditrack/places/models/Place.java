package ro.laflamme.meditrack.places.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

	private String mId = "";
	private String mName = "";
	private String mAddress = "";
	private double mLatitude = 0;
	private double mLongitude = 0;
	private double mRating = 0;
	private String mReference = "";
	private boolean mOpenNow;

	private PlaceDetails mDetails;
	
	private Place(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mAddress = in.readString();
		mLatitude = in.readDouble();
		mLongitude = in.readDouble();
		mRating = in.readDouble();
		mReference = in.readString();
		mOpenNow = in.readByte() != 0;
		mDetails = in.readParcelable(PlaceDetails.class.getClassLoader());
	}
	
	public Place(JSONObject jsonPlace) {
		try {
			mId = jsonPlace.getString("place_id");
			mName = jsonPlace.getString("name");
			mLatitude = jsonPlace.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
			mLongitude = jsonPlace.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//			mRating = jsonPlace.getDouble("rating");
			mReference = jsonPlace.getString("reference");
			mOpenNow = jsonPlace.getJSONObject("opening_hours").getBoolean("open_now");

			if (jsonPlace.has("vicinity")) {
				mAddress = jsonPlace.getString("vicinity");
			} else {
				mAddress = jsonPlace.getString("formatted_address");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name; 
	}
	public String getAddress() {
		return mAddress;
	}
	
	public void setAddress(String address) {
		mAddress = address;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public boolean isOpenNow() {
		return mOpenNow;
	}

	public void setOpenNow(boolean mOpenNow) {
		this.mOpenNow = mOpenNow;
	}

	public double getDistanceTo(Location location) {
		Location source = new Location("");
		source.setLatitude(mLatitude);
		source.setLongitude(mLongitude);
		
		return source.distanceTo(location);
	}

	public double getRating() {
		return mRating;
	}
	
	public void setRating(float rating) {
		mRating = rating;
	}
	
	public String getReference() {
		return mReference;
	}

	public void setReference(String value) {
		mReference = value;
	}
	
	public void setDetail(PlaceDetails detail) {
		mDetails = detail;
	}
	
	public PlaceDetails getDetail() {
		return mDetails;
	}
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
		out.writeString(mName);
		out.writeString(mAddress);
		out.writeDouble(mLatitude);
		out.writeDouble(mLongitude);
		out.writeDouble(mRating);
		out.writeString(mReference);
		out.writeByte((byte) (mOpenNow ? 1 : 0));
		out.writeParcelable(mDetails, flags);
	}
	
	public static final Creator<Place> CREATOR = new Creator<Place>() {

		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};
}