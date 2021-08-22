package tw.tcnr01.mytrashcar.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import tw.tcnr01.mytrashcar.Garsign01;
import tw.tcnr01.mytrashcar.R;

public class SelectLocationListener implements AdapterView.OnItemSelectedListener {
    private Activity activity;

    public SelectLocationListener(Activity activity) {
        this.activity = activity;//存起來

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemSelected: ", String.valueOf(id));
        String[] taichungLocationList = activity.getResources().getStringArray(R.array.area_a001);
        Log.d("Location", taichungLocationList[position]); // 北屯區、中區...

        GetTrashData.getData(activity, howmanydata, gslist001, GetTrashData.LOCATION.HSINCHU, taichungLocationList[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}