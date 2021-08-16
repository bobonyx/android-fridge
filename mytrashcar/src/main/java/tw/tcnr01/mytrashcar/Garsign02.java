package tw.tcnr01.mytrashcar;

import static tw.tcnr01.mytrashcar.utils.CommonUtils.showProgressDialog;
import static tw.tcnr01.mytrashcar.utils.CommonUtils.sortJsonArray;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tw.tcnr01.mytrashcar.utils.ProcessData;


public class Garsign02 extends AppCompatActivity {

    private ListView gslist002;
    private ArrayAdapter<String> adapter;
    private ArrayList<Map<String, Object>> mList2;
    private TextView t_title;
    private String check_t = null;
    private TableRow gstab02;
    private Spinner s001, s002;
    private String m_Response;
    private TextView howmanydata;
    private Uri uri;
    private Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garsign02);//把畫面跟程式碼綁一起
        setupViewComponent();
    }

    private void setupViewComponent() {
        howmanydata = (TextView) findViewById(R.id.howmany);
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //---------------------------------------
        gslist002 = (ListView) findViewById(R.id.gs_list002);
        mList2 = new ArrayList<Map<String, Object>>();
        gslist002.getLayoutParams().height = newscrollheight;
        gslist002.setLayoutParams(gslist002.getLayoutParams()); // 重定ScrollView大小
        gstab02 = (TableRow) findViewById(R.id.gs_tab02);
        //********設定轉圈圈進度對話盒*****************************
        ProgressDialog pd = showProgressDialog(this);
        //***************************************************************
        OkHttpClient client = new OkHttpClient();
        String url = "http://opendataap2.penghu.gov.tw/resource/files/2021-03-31/c4a7fe6c95ed0d82c7038a9f81e182e3.json";//下載測試
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    m_Response = response.body().string();
                    Garsign02.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //解析JSON
                                JSONArray jsonArray = new JSONArray(m_Response);
                                //------JSON 排序-
                                jsonArray = sortJsonArray(jsonArray, "路線");
                                //----+表頭---------
//            lineid(清運點編號)、car(車牌號碼)、time(回傳時間)、location(回傳地點)、X(x座標)、Y(y座標)
//路線", "清運區", "清運站", "清運時間", "資源回收車收運時間"
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonData = jsonArray.getJSONObject(i);//讀取一筆資料
                                    Map<String, Object> item = new HashMap<String, Object>();//hashmap可以一筆一筆存
                                    String Line_a = jsonData.getString("路線");//抓key顯示value
                                    String Area_a = jsonData.getString("清運區");
                                    String Location_a = jsonData.getString("清運站");
                                    String Time_a = jsonData.getString("清運時間");
                                    String Time_b = jsonData.getString("資源回收車收運時間");

                                    if (Line_a.equals(check_t)) {//除錯的動作?
//                    Log.e("123", Line_a);
                                        Line_a = ".."; //相同的用..(兩點) 相同東西放在一起
                                    } else {
                                        check_t = Line_a;
//                    Log.e("456", Line_a);
                                    }

                                    item.put("路線", Line_a); //key+value
                                    item.put("清運區", Area_a);
                                    item.put("清運站", Location_a);
                                    item.put("清運時間", Time_a);
                                    item.put("資源回收車收運時間", Time_b);
                                    mList2.add(item);

                                    //=========設定listview========
                                    SimpleAdapter adapter = new SimpleAdapter(
                                            Garsign02.this,
                                            mList2,
                                            R.layout.garslist02,//應該garsitem
                                            new String[]{"路線", "清運區", "清運站", "清運時間", "資源回收車收運時間"},
                                            new int[]{R.id.gs_tt011, R.id.gs_tt012, R.id.gs_tt013, R.id.gs_tt014, R.id.gs_tt015}
                                    );
                                    gslist002.setAdapter(adapter);
                                }
                                howmanydata.setText("共" + jsonArray.length() + "筆" + ".");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd.cancel();
                        }
                    });
                }
            }
        });

        s001 = (Spinner) findViewById(R.id.city_s001);
        s002 = (Spinner) findViewById(R.id.city_s002);
        ArrayAdapter<CharSequence> s001adt = ArrayAdapter.createFromResource(this, R.array.city_a001, android.R.layout.simple_spinner_item);
        s001adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s001.setAdapter(s001adt);
        s001.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter spinzoneAdapter = null;
                switch (position) {
                    case 0://台中
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign02.this, R.array.area_a001, android.R.layout.simple_spinner_item);
                        it.setClass(Garsign02.this, Garsign01.class);
                        startActivity(it);
                        break;
//                    case 1://新北
//                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a002, android.R.layout.simple_spinner_item);
//                        break;
                    case 1://澎湖
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign02.this, R.array.area_a003, android.R.layout.simple_spinner_item);
//                        it.setClass(Garsign01.this, Garsign02.class);
//                        startActivity(it);
                        break;
                    case 2://新竹
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign02.this, R.array.area_a004, android.R.layout.simple_spinner_item);
                        it.setClass(Garsign02.this, Garsign03.class);
                        startActivity(it);
                        break;
//                    case 4://宜蘭
//                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a005, android.R.layout.simple_spinner_item);
////                            ArrayAdapter<CharSequence> s002adt = ArrayAdapter.createFromResource(Garsign01.this, R.array.city_a001, android.R.layout.simple_spinner_item);
////                            s002adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                            s002.setAdapter(s002adt);
//                        break;
                }
                if (null != spinzoneAdapter) {
                    spinzoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s002.setAdapter(spinzoneAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //-----------------------選單---------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_fb:
                uri = Uri.parse("https://www.facebook.com/kai.hao.9");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;

            case R.id.menu_notify:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_notify)
                        .setMessage(getString(R.string.menu_message))
                        .setCancelable(false)
                        .setIcon(R.drawable.iu06)
                        .setPositiveButton(R.string.menu_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
            case R.id.menu_member:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_member)
                        .setMessage(getString(R.string.menu_member_message) + "\n" + "維尼、大神、佳佳、波波、柏榕、老大、培揚")
                        .setCancelable(false)
                        .setIcon(R.drawable.iu06)
                        .setPositiveButton(R.string.menu_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
            case R.id.menu_logout:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}


