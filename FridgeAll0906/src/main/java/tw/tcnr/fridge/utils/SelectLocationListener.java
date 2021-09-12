package tw.tcnr.fridge.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import tw.tcnr.fridge.Garsign01;
import tw.tcnr.fridge.R;

public class SelectLocationListener implements AdapterView.OnItemSelectedListener {
    private Activity activity;
    private ListView gslist001;
    private tw.tcnr.fridge.utils.GetTrashData.LOCATION location;

    public SelectLocationListener(Activity activity,  ListView gslist001, tw.tcnr.fridge.utils.GetTrashData.LOCATION location) {
        this.activity = activity;//存起來
//        this.howmanydata = howmanydata;
        this.gslist001 = gslist001;
        this.location = location;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("onItemSelected: ", String.valueOf(id));
        String[] taichungLocationList = activity.getResources().getStringArray(R.array.area_a001);
//        Log.d("Location", taichungLocationList[position]); // 北屯區、中區...

        tw.tcnr.fridge.utils.GetTrashData.getData(activity, gslist001,location, taichungLocationList[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}