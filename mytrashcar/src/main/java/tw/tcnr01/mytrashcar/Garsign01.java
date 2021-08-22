package tw.tcnr01.mytrashcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import tw.tcnr01.mytrashcar.utils.GetTrashData;
import tw.tcnr01.mytrashcar.utils.SelectLocationListener;


public class Garsign01 extends AppCompatActivity implements View.OnClickListener {

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
    private ImageButton searchcar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garsign01);//把畫面跟程式碼綁一起
        setupViewComponent();
    }

    private void setupViewComponent() {
        howmanydata = (TextView) findViewById(R.id.howmany);
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100; // 設定ScrollView使用尺寸的4/5
        //---------------------------------------
        gslist001 = (ListView) findViewById(R.id.gs_list001);
        searchcar = (ImageButton) findViewById(R.id.gs_search001);
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
                }
                if (null != spinzoneAdapter) {
                    spinzoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s002.setAdapter(spinzoneAdapter);
                    s002.setOnItemSelectedListener(new SelectLocationListener(Garsign01.this, howmanydata, gslist001));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gs_search001:
                it.setClass(Garsign01.this, GarsignSql.class);
                startActivity(it);
                break;
        }
    }
}


