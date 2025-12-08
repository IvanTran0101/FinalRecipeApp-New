package vn.edu.tdtu.anhminh.myapplication.UI.Map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

// Use the internal class
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Map.DisplayNearbyStoreUseCase;
import vn.edu.tdtu.anhminh.myapplication.R;

public class MapFragment extends Fragment {

    private MapView map;
    private MyLocationNewOverlay locationOverlay;
    private DisplayNearbyStoreUseCase useCase;
    private ProgressBar progressBar;
    private boolean isDataLoaded = false;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(ctx.getPackageName());

        map = view.findViewById(R.id.map_view);
        progressBar = view.findViewById(R.id.loading_progress);
        useCase = new DisplayNearbyStoreUseCase();

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(16.0);
        map.getController().setCenter(new GeoPoint(10.7324, 106.6992)); // Default

        checkPermissionsAndStart();
    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationAndSearch();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableMyLocationAndSearch() {
        GpsMyLocationProvider provider = new GpsMyLocationProvider(requireContext());
        locationOverlay = new MyLocationNewOverlay(provider, map);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        map.getOverlays().add(locationOverlay);

        locationOverlay.runOnFirstFix(() -> {
            Location myLocation = locationOverlay.getLastFix();
            if (myLocation != null && !isDataLoaded) {
                isDataLoaded = true;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        map.getController().animateTo(new GeoPoint(myLocation));
                        fetchStores(myLocation.getLatitude(), myLocation.getLongitude());
                    });
                }
            }
        });
    }

    private void fetchStores(double lat, double lon) {
        progressBar.setVisibility(View.VISIBLE);

        useCase.execute(requireContext(), lat, lon, new DisplayNearbyStoreUseCase.Callback() {
            @Override
            public void onStoresLoaded(List<DisplayNearbyStoreUseCase.Store> stores) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                for (DisplayNearbyStoreUseCase.Store store : stores) {
                    Marker marker = new Marker(map);
                    marker.setPosition(new GeoPoint(store.lat, store.lon));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    marker.setTitle(store.name);
                    marker.setSnippet(store.address);
                    map.getOverlays().add(marker);
                }
                map.invalidate();
                Toast.makeText(getContext(), "Found " + stores.size() + " stores!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) enableMyLocationAndSearch();
            });

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}