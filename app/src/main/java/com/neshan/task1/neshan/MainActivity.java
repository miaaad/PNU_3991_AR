package com.neshan.task1.neshan;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carto.graphics.Color;
import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.LineStyle;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.neshan.task1.BuildConfig;
import com.neshan.task1.R;
import com.neshan.task1.domain.model.Leg;
import com.neshan.task1.domain.model.Step;
import com.neshan.task1.domain.model.animate.BeginJourneyEvent;
import com.neshan.task1.domain.model.animate.CurrentJourneyEvent;
import com.neshan.task1.domain.model.animate.EndJourneyEvent;
import com.neshan.task1.domain.model.animate.JourneyEventBus;
import com.neshan.task1.domain.model.searchModel.ItemsItem;
import com.neshan.task1.neshan.adapter.SearchAdapter;
import com.neshan.task1.neshan.presentation.SearchRoutingViewModel;
import com.neshan.task1.utils.polyLine.Point;
import com.neshan.task1.utils.polyLine.PolylineDecoder;
import com.neshan.task1.utils.polyLine.PolylineUtils;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.model.Marker;
import org.neshan.mapsdk.model.Polyline;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

// create by miaad 1399/11/29
public class MainActivity extends DaggerAppCompatActivity implements SearchAdapter.OnSearchItemListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SearchRoutingViewModel viewModel;
    private MapView map;
    private Button navigate;
    private CoordinatorLayout mainView;
    private LinearLayout bottomSheet;
    private EditText searchEditText;
    List<Point> points;
    String originLatlng;
    LatLng mDesLatlng;
    LatLng mSearchDesLatlng;
    LatLng mOriginLatlng;
    String type = "car";
    Marker marker;
    ArrayList<LatLng> decodedStepByStepPath;
    AnimationStyle animSt;
    ArrayList<Marker> markers = new ArrayList<>();
    private Polyline onMapPolyline;
    boolean overview = false;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SearchAdapter mAdapter;
    private ArrayList<ItemsItem> items = new ArrayList<>();
    private boolean searchOrMark = false;
    private float cameraBearing;


    private Handler handler;
    private int index, next;
    LatLng startPosition, endPosition;
    private float v;
    private double lat, lng;

    //---------------------------------------------------------------
    private static final String TAG = MainActivity.class.getName();
    final int REQUEST_CODE = 123;
    final int BASE_MAP_INDEX = 0;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private String lastUpdateTime;
    private Boolean mRequestingLocationUpdates;
    //---------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        getLocationPermission();
        initLocation();
        startReceivingLocationUpdates();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(MainActivity.this, viewModelFactory).get(SearchRoutingViewModel.class);
        initView();
    }

    private void getDataFromNetwork(String originLatlng, String desLatlng) {
        viewModel.loadData(originLatlng, type, desLatlng);
        viewModel.getResponseRoute.observe(this, object -> {
            PolylineDecoder polylineDecoder = new PolylineDecoder();
            points = new ArrayList<>();
            decodedStepByStepPath = new ArrayList<>();

            List<Leg> legs = object.getRoutes().get(0).getLegs();
            List<Step> steps = legs.get(0).getSteps();
            for (int S = 0; S < steps.size(); S++) {
                String poly = steps.get(S).getPolyline();
                List<Point> mPoints = polylineDecoder.decode(steps.get(S).getPolyline());
                points.addAll(mPoints);
            }

            for (int i = 0; i < points.size(); i++) {
                decodedStepByStepPath.add(new LatLng(points.get(i).getLat(), points.get(i).getLng()));
            }

            if (onMapPolyline != null) {
                map.removePolyline(onMapPolyline);
            }
            onMapPolyline = new Polyline(decodedStepByStepPath, getLineStyle());
            map.addPolyline(onMapPolyline);
            map.moveCamera(mOriginLatlng, 0.75f);
            map.setZoom(19, 0.45f);
            Log.i("MIAAAD", String.valueOf(map.getBearing()));
            map.setTilt(35f, 0.45f);
            map.getSettings().setMaxZoomLevel(19);

            double dLon = (mDesLatlng.getLongitude() - mOriginLatlng.getLongitude());
            double y = sin(dLon) * cos(mDesLatlng.getLatitude());
            double x = cos(mOriginLatlng.getLatitude()) * sin(mDesLatlng.getLatitude()) - sin(mOriginLatlng.getLatitude()) * cos(mDesLatlng.getLatitude()) * cos(dLon);
            double brng = Math.toDegrees((Math.atan2(y, x)));
            brng = (360 - ((brng + 360) % 360));


            double desLat = decodedStepByStepPath.get(2).getLatitude();
            double desLng = decodedStepByStepPath.get(2).getLongitude();
            float bearing = (float) SphericalUtil.computeHeading(new com.google.android.gms.maps.model.LatLng(mOriginLatlng.getLatitude(), mOriginLatlng.getLongitude()), new com.google.android.gms.maps.model.LatLng(desLat, desLng));
            map.setBearing(-bearing, 0.45f);

            animateMarker();

            String markers = PolylineUtils.toMarkers(points);
            Log.i("MIAAD", markers);
        });
    }

    private void animateMarker() {
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
//        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                List<LatLng> points = decodedStepByStepPath;
//                int percentValue = (int) valueAnimator.getAnimatedValue();
//                int size = points.size();
//                int newPoints = (int) (size * (percentValue / 100.0f));
//                List<LatLng> p = points.subList(0, newPoints);
//            }
//        });
//        polylineAnimator.start();
        handler = new Handler();
        index = -1;
        next = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < decodedStepByStepPath.size() - 1) {
                    index++;
                    next = index + 1;
                }
                if (index < decodedStepByStepPath.size() - 1) {
                    startPosition = decodedStepByStepPath.get(index);
                    endPosition = decodedStepByStepPath.get(next);
                }
                if (index == 0) {
                    BeginJourneyEvent beginJourneyEvent = new BeginJourneyEvent();
                    beginJourneyEvent.setBeginLatLng(startPosition);
                    JourneyEventBus.getInstance().setOnJourneyBegin(beginJourneyEvent);
                }
                if (index == decodedStepByStepPath.size() - 1) {
                    EndJourneyEvent endJourneyEvent = new EndJourneyEvent();
                    endJourneyEvent.setEndJourneyLatLng(new LatLng(decodedStepByStepPath.get(index).getLatitude(),
                            decodedStepByStepPath.get(index).getLongitude()));
                    JourneyEventBus.getInstance().setOnJourneyEnd(endJourneyEvent);
                }
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(3000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        v = valueAnimator.getAnimatedFraction();
                        lng = v * endPosition.getLongitude() + (1 - v)
                                * startPosition.getLongitude();
                        lat = v * endPosition.getLatitude() + (1 - v)
                                * startPosition.getLatitude();
                        LatLng newPos = new LatLng(lat, lng);
                        CurrentJourneyEvent currentJourneyEvent = new CurrentJourneyEvent();
                        currentJourneyEvent.setCurrentLatLng(newPos);
                        JourneyEventBus.getInstance().setOnJourneyUpdate(currentJourneyEvent);
                        marker.setLatLng(newPos);
                        map.moveCamera(newPos, 0f);
                        float bearing = (float) SphericalUtil.computeHeading(new com.google.android.gms.maps.model.LatLng(startPosition.getLatitude(), startPosition.getLongitude()), new com.google.android.gms.maps.model.LatLng(endPosition.getLatitude(), endPosition.getLongitude()));
                        Log.i("BEARING", bearing + "");
                        if (bearing != 0.0)
                            map.setBearing(-bearing, 0f);
//                        map.setBearing((float) getBearing(startPosition.getLatitude() ,startPosition.getLongitude() ,endPosition.getLatitude() ,endPosition.getLongitude()), 0f);
                    }
                });
                valueAnimator.start();
                if (index != decodedStepByStepPath.size() - 1) {
                    handler.postDelayed(this, 3000);
                }
            }
        }, 3000);
    }

    private static double degreeToRadians(double latLong) {
        return (Math.PI * latLong / 180.0);
    }

    private static double radiansToDegree(double latLong) {
        return (latLong * 180.0 / Math.PI);
    }

    public static double getBearing(double lati1, double lngi1, double lati2, double lngi2) {

        double fLat = degreeToRadians(lati1);
        double fLong = degreeToRadians(lngi1);
        double tLat = degreeToRadians(lati2);
        double tLong = degreeToRadians(lngi2);

        double dLon = (tLong - fLong);

        double degree = radiansToDegree(Math.atan2(sin(dLon) * cos(tLat),
                cos(fLat) * sin(tLat) - sin(fLat) * cos(tLat) * cos(dLon)));

        if (degree >= 0) {
            return degree;
        } else {
            return 360 + degree;
        }
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        map = findViewById(R.id.map);
        bottomSheet = findViewById(R.id.bottom_sheet);
        navigate = findViewById(R.id.navigate);
        mainView = findViewById(R.id.mainView);
        searchEditText = findViewById(R.id.search_editText);
        initRecyclerView();
        navigate.setVisibility(View.GONE);

        map.moveCamera(new LatLng(34.377721, 47.138991), 0);
        map.setZoom(14, 0);

        map.setOnMapLongClickListener(latLng -> {
            map.removeMarker(marker);
            marker = addUserMarker(latLng);
            map.addMarker(marker);
            markers.clear();
            marker = addUserMarker(latLng);
            markers.add(marker);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    searchOrMark = false;
                    navigate.setVisibility(View.VISIBLE);
                }
            });
            mDesLatlng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        if (originLatlng != null) {
                            if (searchOrMark) {
                                getDataFromNetwork(originLatlng, mSearchDesLatlng.getLatitude() + "," + mSearchDesLatlng.getLongitude());
                            } else {
                                getDataFromNetwork(originLatlng, mDesLatlng.getLatitude() + "," + mDesLatlng.getLongitude());
                            }
                        }
                    }
                });
            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        mAdapter = new SearchAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);
        getSearchFromNetWork();
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void getSearchFromNetWork() {
        double lat = 34.323960;
        double lng = 47.073695;
        String searchName = "مسکن";

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                navigate.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchData(s.toString(), lat, lng);
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                closeKeyBoard();
                viewModel.searchData(searchEditText.getText().toString(), lat, lng);
            }
            return false;
        });

        viewModel.getResponseSearch.observe(this, object -> {
            mAdapter.getList(object.getItems());
        });

        viewModel.isSearchLoad.observe(this, isLoaded -> {
            if (isLoaded) {
                bottomSheet.setVisibility(View.VISIBLE);
                navigate.setVisibility(View.GONE);
            } else {
                bottomSheet.setVisibility(View.GONE);
            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                userLocation = locationResult.getLastLocation();
                float miaad = locationResult.getLocations().get(0).bearingTo(locationResult.getLocations().get(0));
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                onLocationChange();
            }
        };

        mRequestingLocationUpdates = false;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();

    }

    private void startLocationUpdates() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        onLocationChange();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        onLocationChange();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startReceivingLocationUpdates() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onLocationChange() {
        if (userLocation != null) {
            if (marker != null)
                map.removeMarker(marker);
            marker = addUserMarker(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
            map.addMarker(marker);
        }
    }

    private Marker addUserMarker(LatLng loc) {
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        animSt = animStBl.buildStyle();

        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));
        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();

        marker = new Marker(loc, markSt);
        return marker;
    }

    private Marker addMarker(LatLng LatLng, float size) {
        Marker marker = new Marker(LatLng, getMarkerStyle(size));
        map.addMarker(marker);
        markers.add(marker);
        return marker;
    }

    private MarkerStyle getMarkerStyle(float size) {
        MarkerStyleBuilder styleCreator = new MarkerStyleBuilder();
        styleCreator.setSize(size);
        styleCreator.setBitmap
                (BitmapUtils.createBitmapFromAndroidBitmap
                        (BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));
        return styleCreator.buildStyle();
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void focusOnUserLocation(View view) {
        if (userLocation != null) {
            map.moveCamera(
                    new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), 0.25f);
            map.setZoom(17, 0.25f);
            mOriginLatlng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            marker = addUserMarker(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
            map.addMarker(marker);
            originLatlng = userLocation.getLatitude() + "," + userLocation.getLongitude();
//            float bearing = (float) SphericalUtil.computeHeading(new com.google.android.gms.maps.model.LatLng(mOriginLatlng.getLatitude(), mOriginLatlng.getLongitude()), new com.google.android.gms.maps.model.LatLng(34.37772135978459, 47.1389910338243));
//            map.setBearing(-bearing, 0.45f);
//            map.setTilt(45f, 0.45f);
        }
    }

    private boolean getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

//    private float getBearing(LatLng begin, LatLng end) {
//        double lat = Math.abs(begin.getLatitude() - end.getLatitude());
//        double lng = Math.abs(begin.getLongitude() - end.getLongitude());
//
//        if (begin.getLatitude() < end.getLatitude() && begin.getLongitude() < end.getLongitude())
//            return (float) (Math.toDegrees(Math.atan(lng / lat)));
//        else if (begin.getLatitude() >= end.getLatitude() && begin.getLatitude() < end.getLongitude())
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
//        else if (begin.getLatitude() >= end.getLatitude() && begin.getLongitude() >= end.getLongitude())
//            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
//        else if (begin.getLatitude() < end.getLatitude() && begin.getLongitude() >= end.getLongitude())
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
//        return -1;
//    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.getLatitude() * PI / 180;
        double long1 = latLng1.getLongitude() * PI / 180;
        double lat2 = latLng2.getLatitude() * PI / 180;
        double long2 = latLng2.getLongitude() * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private float getBearing(LatLng begin, LatLng end) {
        double dLon = (end.getLongitude() - begin.getLongitude());
        double x = Math.sin(Math.toRadians(dLon)) * Math.cos(Math.toRadians(end.getLatitude()));
        double y = Math.cos(Math.toRadians(begin.getLatitude())) * Math.sin(Math.toRadians(end.getLatitude()))
                - Math.sin(Math.toRadians(begin.getLatitude())) * Math.cos(Math.toRadians(end.getLatitude())) * Math.cos(Math.toRadians(dLon));
        double bearing = Math.toDegrees((Math.atan2(x, y)));
        return (float) bearing;
    }

    private static double computeRotation(float fraction, double start, double end) {
        double normalizeEnd = end - start;
        double normalizedEndAbs = (normalizeEnd + 360) % 360;

        double direction = (normalizedEndAbs > 180) ? -1 : 1;
        double rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        double result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    private LineStyle getLineStyle() {
        LineStyleBuilder lineStCr = new LineStyleBuilder();
        lineStCr.setColor(new Color((short) 1, (short) 89, (short) 34, (short) 151));
        lineStCr.setWidth(10f);
        lineStCr.setStretchFactor(0f);
        return lineStCr.buildStyle();
    }

    private void clearMarkers() {
        for (Marker marker : markers) {
            map.removeMarker(marker);
        }
        markers.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.disposable.dispose();
    }

    @Override
    public void onSearchItemClick(LatLng LatLng) {
        closeKeyBoard();
        clearMarkers();
        mAdapter.getList(new ArrayList<ItemsItem>());
        map.moveCamera(LatLng, 0);
        map.setZoom(16f, 0);
        map.addMarker(addMarker(LatLng, 30f));
        navigate.setVisibility(View.VISIBLE);
        searchOrMark = true;
        mSearchDesLatlng = new LatLng(LatLng.getLatitude(), LatLng.getLongitude());
    }
}