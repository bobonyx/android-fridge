package tw.tcnr01.mytrashcar;

import static tw.tcnr01.mytrashcar.utils.SortJsonArray.sortJsonArray;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Garsign03 extends AppCompatActivity {

    private ListView gslist003;
    private ArrayAdapter<String> adapter;
    private ArrayList<Map<String, Object>> mList3;
    private TextView t_title;
    private String check_t = null;
    private TableRow gstab03;
    private Spinner s001,s002;
    private String m_Response;
    private TextView howmanydata;
    private Uri uri;
    private Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garsign03);//把畫面跟程式碼綁一起
        setupViewComponent();
    }

    private void setupViewComponent() {
        howmanydata=(TextView)findViewById(R.id.howmany);
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //---------------------------------------
        gslist003 = (ListView) findViewById(R.id.gs_list003);
        mList3 = new ArrayList<Map<String, Object>>();
        gslist003.getLayoutParams().height = newscrollheight;
        gslist003.setLayoutParams(gslist003.getLayoutParams()); // 重定ScrollView大小
        gstab03 = (TableRow) findViewById(R.id.gs_tab03);
        //********設定轉圈圈進度對話盒*****************************
        final ProgressDialog pd = new ProgressDialog(Garsign03.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//progressbox  跳出視窗顯示
        pd.setTitle("連結伺服器");
        pd.setMessage("Loading.........");
        pd.setIcon(R.drawable.garbagetruckicon1);
        pd.setIndeterminate(false);
        pd.show();
        //***************************************************************
        OkHttpClient client = new OkHttpClient();
        String url = "https://odws.hccg.gov.tw/001/Upload/25/opendata/9059/165/f91f9475-42b8-407c-89d3-f0dd5dc2e2f8.json";//下載測試
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
                    Garsign03.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //解析JSON
                                JSONArray jsonArray = new JSONArray(m_Response);
                                //------JSON 排序-
                                jsonArray = sortJsonArray(jsonArray,"車號");
                                //----+表頭---------
//            lineid(清運點編號)、car(車牌號碼)、time(回傳時間)、location(回傳地點)、X(x座標)、Y(y座標)
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonData = jsonArray.getJSONObject(i);//讀取一筆資料
                                    Map<String, Object> item = new HashMap<String, Object>();//hashmap可以一筆一筆存
                                    String Car = jsonData.getString("車號");//抓key顯示value
                                    String Location_a = jsonData.getString("清潔公車停置地點");
                                    String Time_a = jsonData.getString("預估到達時間");
                                    String A_a = jsonData.getString("清運日_星期幾");
                                    String B_a = jsonData.getString("回收日_星期幾");

                                    if (Car.equals(check_t)) {//除錯的動作?
//                    Log.e("123", Car);
                                        Car = ".."; //相同的用..(兩點) 相同東西放在一起
                                    } else {
                                        check_t = Car;
//                    Log.e("456", Car);
                                    }

                                    item.put("車號", Car); //key+value
                                    item.put("清潔公車停置地點", Location_a);
                                    item.put("預估到達時間", Time_a);
                                    item.put("清運日_星期幾", A_a);
                                    item.put("回收日_星期幾", B_a);
                                    mList3.add(item);

                                    //=========設定listview========
                                    SimpleAdapter adapter = new SimpleAdapter(
                                            Garsign03.this,
                                            mList3,
                                            R.layout.garslist03,//應該garsitem
                                            new String[]{"車號", "清潔公車停置地點", "預估到達時間", "清運日_星期幾", "回收日_星期幾"},
                                            new int[]{R.id.gs_tt021, R.id.gs_tt022, R.id.gs_tt023, R.id.gs_tt024, R.id.gs_tt025}
                                    );
                                    gslist003.setAdapter(adapter);
                                }
                                howmanydata.setText("共"+jsonArray.length()+"筆"+".");
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
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign03.this, R.array.area_a001, android.R.layout.simple_spinner_item);
                        it.setClass(Garsign03.this, Garsign01.class);
                        startActivity(it);
                        break;
//                    case 1://新北
//                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a002, android.R.layout.simple_spinner_item);
//                        break;
                    case 1://澎湖
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign03.this, R.array.area_a003, android.R.layout.simple_spinner_item);
                        it.setClass(Garsign03.this, Garsign02.class);
                        startActivity(it);
                        break;
                    case 2://新竹
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign03.this, R.array.area_a004, android.R.layout.simple_spinner_item);
//                        it.setClass(Garsign01.this, Garsign03.class);
//                        startActivity(it);
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
//
//        s002.setOnItemSelectedListener(s001On);
//        try {
//            String Task_opendata =
//                    new TransTask().execute("https://datacenter.taichung.gov.tw/swagger/OpenData/215be7a0-a5a1-48b8-9489-2633fed19de3").get();
////            臺中市垃圾及資源回收車動態資訊
////            lineid(清運點編號)、car(車牌號碼)、time(回傳時間)、location(回傳地點)、X(x座標)、Y(y座標)
//            //===== 設定 opendata 網址===============
//
//            List<Map<String, Object>> mList3;
//            mList3 = new ArrayList<Map<String, Object>>();

//
//        } catch (Exception e) {
//            e.printStackTrace();//原本的會比較細分相對應的錯誤 這憶起噴 網路連不到 這邊
//        }
//    }

//    private AdapterView.OnItemSelectedListener s001On=new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            sSex=parent.getSelectedItem().toString();
////            if (){
//        };
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {//類似setContentView(R.layout.garsign01)把畫面跟程式碼綁一起
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {//清單的方法
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            this.finish();
////            onDestroy();//會做記憶體釋放的動作   ad本身就會但時機不定
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
                it = new Intent(Intent.ACTION_VIEW,uri);
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
                        .setMessage(getString(R.string.menu_member_message)+"\n"+"維尼、大神、佳佳、波波、柏榕、老大、培揚")
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.gc();//強制記憶體釋放 garbage collection
//    }

//    public class TransTask extends AsyncTask<String, Void, String> {//跟API撈資料 ad最原始的方法
//
//        @Override
//        protected String doInBackground(String... params) {
//            StringBuilder sb = new StringBuilder();
//            try {
//                URL url = new URL(params[0]);
//                BufferedReader in = new BufferedReader(//串流和http交流 把資料一個一點一點的接(串)起來 緩衝區(排隊)有意外臨時接 但還沒讀
//                        new InputStreamReader(url.openStream()));//串流讀取 一直丟一直丟
//                String line = in.readLine();
//                while (line != null) {//當LINE不等於空值(字串還有)     讀取完
//                    Log.d("HTTP", line);
//                    sb.append(line);//string sb 串
//                    line = in.readLine();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return sb.toString();
//        }
//    }
//}

