package tw.tcnr01.mytrashcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import tw.tcnr01.mytrashcar.utils.GetTrashData;


public class Garsign01 extends AppCompatActivity {

    private ListView gslist001;
    private ArrayAdapter<String> adapter;
    private TextView t_title;
    private String check_t = null;
    private TableRow gstab01;
    private Spinner s001, s002;
    private String m_Response;
    private TextView howmanydata;
    private TextView gs_t001, gs_t002, gs_t003, gs_t004, gs_t005;
    private Uri uri;
    private Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garsign01);//把畫面跟程式碼綁一起
        setupViewComponent();
//        ((Button)findViewById(R.id.btn_test)).setOnClickListener(v -> setupViewComponent());//箭函式(點燈點lambda) 可以按了重複run debug
//        ((Button) findViewById(R.id.btn_test)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    String aaa = null;
//                    aaa.substring(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(Garsign01.this, "無法取得該參數", Toast.LENGTH_SHORT).show();// "error handle"   可以提醒可能發生錯誤及處理方法且不會閃退
//                }
//
//            }
//        });
    }

    private void setupViewComponent() {
        howmanydata = (TextView) findViewById(R.id.howmany);
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //---------------------------------------
        gslist001 = (ListView) findViewById(R.id.gs_list001);

        gslist001.getLayoutParams().height = newscrollheight;
        gslist001.setLayoutParams(gslist001.getLayoutParams()); // 重定ScrollView大小
        gstab01 = (TableRow) findViewById(R.id.gs_tab01);
        gs_t001 = (TextView) findViewById(R.id.gs_t001);
        gs_t002 = (TextView) findViewById(R.id.gs_t002);
        gs_t003 = (TextView) findViewById(R.id.gs_t003);
        gs_t004 = (TextView) findViewById(R.id.gs_t004);
        gs_t005 = (TextView) findViewById(R.id.gs_t005);

        // 取得預設資料(台中)
        GetTrashData.getData(this, howmanydata, gslist001, GetTrashData.LOCATION.TAICHUNG);

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
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a001, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t001));
                        gs_t002.setText(getString(R.string.t002));
                        gs_t003.setText(getString(R.string.t003));
                        gs_t004.setText(getString(R.string.t004));
                        gs_t005.setText(getString(R.string.t005));
                        GetTrashData.getData(Garsign01.this, howmanydata, gslist001, GetTrashData.LOCATION.TAICHUNG);
                        break;
//                    case 1://新北
//                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a002, android.R.layout.simple_spinner_item);
//                        break;
                    case 1://澎湖
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a003, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t011));
                        gs_t002.setText(getString(R.string.t012));
                        gs_t003.setText(getString(R.string.t013));
                        gs_t004.setText(getString(R.string.t014));
                        gs_t005.setText(getString(R.string.t015));
                        GetTrashData.getData(Garsign01.this, howmanydata, gslist001, GetTrashData.LOCATION.PENGHU);
                        break;
                    case 2://新竹
                        spinzoneAdapter = ArrayAdapter.createFromResource(Garsign01.this, R.array.area_a004, android.R.layout.simple_spinner_item);
                        gs_t001.setText(getString(R.string.t001));
                        gs_t002.setText(getString(R.string.t022));
                        gs_t003.setText(getString(R.string.t023));
                        gs_t004.setText(getString(R.string.t024));
                        gs_t005.setText(getString(R.string.t025));
                        GetTrashData.getData(Garsign01.this, howmanydata, gslist001, GetTrashData.LOCATION.HSINCHU);
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
//            List<Map<String, Object>> mList;
//            mList = new ArrayList<Map<String, Object>>();

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

