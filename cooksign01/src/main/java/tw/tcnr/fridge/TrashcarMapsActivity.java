package tw.tcnr.fridge;

import static tw.tcnr.fridge.utils.GetTrashData.setDataToGoogleMap;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import tw.tcnr.fridge.databinding.ActivityTrashcarMapsBinding;
import tw.tcnr.fridge.utils.GetTrashData;

public class TrashcarMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityTrashcarMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrashcarMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        String location = getIntent().getStringExtra("location");//檢驗
        if (null == location) {
            Toast.makeText(this, "Something went wrong ! Location not found !", Toast.LENGTH_SHORT).show();
            return;//early return 就不會往下跑
        }

        Log.e("location", location);//格式

        GetTrashData.LOCATION focusLocation = GetTrashData.LOCATION.valueOf(location);
        LatLng locationLatLng;
        switch (focusLocation) {
            case HSINCHU:
                locationLatLng = new LatLng(24.840168872835232, 121.00953756776431);
                break;
            case PENGHU:
                locationLatLng = new LatLng(23.584269524454367, 119.58198096937105);
                break;
            default:
                locationLatLng = new LatLng(24.15470473208519, 120.6536969352455);
                break;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 14)); //移動位置上
        setDataToGoogleMap(this, mMap, focusLocation);
    }
}