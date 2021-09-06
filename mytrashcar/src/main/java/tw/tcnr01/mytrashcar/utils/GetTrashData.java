package tw.tcnr01.mytrashcar.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static tw.tcnr01.mytrashcar.utils.CommonUtils.showProgressDialog;

public class GetTrashData {
    public enum LOCATION {
        TAICHUNG, PENGHU, HSINCHU
    }

    /**
     * 不篩選 顯示全部清單
     * @param activity
     * @param gslist001
     * @param location
     */
    public static void getData(Activity activity, ListView gslist001, LOCATION location) {
        getData(activity, gslist001, location, null);
    }

    /**
     * 依據filterLocation 進行資料篩選
     * @param activity
     * @param gslist001
     * @param location
     * @param filterLocation
     */
    public static void getData(Activity activity, ListView gslist001, LOCATION location, String filterLocation) {
        //********設定轉圈圈進度對話盒*****************************
        ProgressDialog pd = showProgressDialog(activity);
        //***************************************************************
        OkHttpClient client = new OkHttpClient();

        String url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
        String[] keys = new String[]{"car", "time", "location", "X", "Y"};
        switch (location) {
            case TAICHUNG:
                url = "https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3";
                keys = new String[]{"car", "time", "location", "X", "Y"};
                break;
            case PENGHU:
                url = "https://loshenghuan.tcnrcloud110a.com/PENGHU.txt";
                keys = new String[]{"路線", "清運區", "清運站", "清運時間", "資源回收車收運時間"};
                break;
            case HSINCHU:
                url = "https://odws.hccg.gov.tw/001/Upload/25/opendata/9059/165/f91f9475-42b8-407c-89d3-f0dd5dc2e2f8.json";
                keys = new String[]{"車號", "預估到達時間", "清潔公車停置地點", "清運日_星期幾", "回收日_星期幾"};
                break;
        }
        Request request = new Request.Builder().url(url).build();

        String[] finalKeys = keys;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    activity.runOnUiThread(() -> {
                        int dataCount = ProcessData.process(activity, gslist001, result, finalKeys, filterLocation);
//                        howmanydata.setText("共" + dataCount + "筆" + ".");
                        pd.cancel();
                    });
                }
            }
        });
    }
}
