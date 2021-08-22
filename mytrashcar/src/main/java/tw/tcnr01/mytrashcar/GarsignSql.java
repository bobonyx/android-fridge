package tw.tcnr01.mytrashcar;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tw.tcnr01.mytrashcar.DBConnectorgars01;
import tw.tcnr01.mytrashcar.FriendDBHelpergarsign01;
import tw.tcnr01.mytrashcar.MyAlertDialoggarsign01;

public class GarsignSql extends AppCompatActivity implements View.OnClickListener {

private TextView count_t;
    private EditText b_id, b_name, b_grp, b_address;


    private TextView tvTitle;
    private Button btNext, btPrev, btTop, btEnd;
    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;
    //--------------------------
    private float x1; // 觸控的 X 軸位置
    private float y1; // 觸控的 Y 軸位置
    private float x2;
    private float y2;
    int range = 50; // 兩點距離
    int ran = 60; // 兩點角度

    private Button btEdit, btDel;
    String tname, tgrp, taddr;
    String TAG = "tncr01=";
    private Spinner mSpnName;
    int up_item = 0;
    //------------------------------
    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;
    private Button btAdd, btAbandon, btquery, btcancel, btreport;

    //    private static ContentResolver mContRes;
    private String[] MYCOLUMN = new String[]{"id", "name", "grp", "address"};
    int tcount;

    // ------------------
    public static String myselection = "";
    public static String myorder = "id ASC"; // 排序欄位
    public static String myargs[] = new String[]{};

    private LinearLayout blinear02;
    private ListView listView;
    private TextView bsubTitle;
    private RelativeLayout blinear01;
    //===============
    private FriendDBHelpergarsign01 dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "garbagetrucktest2";
    private static final int DBversion = 1;
    //-----------------
    private String sqlctl;
    private String tid;
    private Button btAcannel;
    private String s_id;
    private int old_index;
    private TextView b_servermsg;
    private String ser_msg;
    private int servermsgcolor;
    private Uri uri;
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this); //使用暫存Thread 需要用此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViewComponent();
    }
    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
    }
    private void setupViewComponent() {
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        b_id = (EditText) findViewById(R.id.edid);
        b_name = (EditText) findViewById(R.id.edtName);
        b_grp = (EditText) findViewById(R.id.edtGrp);
        b_address = (EditText) findViewById(R.id.edtAddr);
        count_t = (TextView) findViewById(R.id.count_t);

        btNext = (Button) findViewById(R.id.btIdNext);
        btPrev = (Button) findViewById(R.id.btIdPrev);
        btTop = (Button) findViewById(R.id.btIdtop);
        btEnd = (Button) findViewById(R.id.btIdend);

        btEdit = (Button) findViewById(R.id.btnupdate);
        btDel = (Button) findViewById(R.id.btIdDel);

        //-----------------------
        btAdd = (Button) findViewById(R.id.btnAdd);
        btAbandon = (Button) findViewById(R.id.btnabandon);
        btquery = (Button) findViewById(R.id.btnquery);
        btcancel = (Button) findViewById(R.id.btidcancel);
        btreport = (Button) findViewById(R.id.btnlist);

        blinear01 = (RelativeLayout) findViewById(R.id.relative01);
        blinear02 = (LinearLayout) findViewById(R.id.linear02);

        listView = (ListView) findViewById(R.id.listView);
        bsubTitle = (TextView) findViewById(R.id.subTitle);
        b_servermsg=(TextView)findViewById(R.id.servermsg);

        //---------設定layout 顯示---------------
        u_layout_def();
        //-----------------------------------------

        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btTop.setOnClickListener(this);
        btEnd.setOnClickListener(this);

        btEdit.setOnClickListener(this);
        btDel.setOnClickListener(this);

        btAdd.setOnClickListener(this);
        btAbandon.setOnClickListener(this);
        btquery.setOnClickListener(this);
        btcancel.setOnClickListener(this);
        btreport.setOnClickListener(this);

        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        //-----------------
        mSpnName = (Spinner) this.findViewById(R.id.spnName);
//---------------------
        initDB();
        showRec(index);
        u_setspinner();
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        tvTitle.setText("顯示資料： 共" + tcount + " 筆");
        b_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
        // -------------------------
        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
//        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
//        //==========================================
//        mSpnName = (Spinner) this.findViewById(R.id.spnName);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item);
//        for (int i = 0; i < recSet.size(); i++) {
//            String[] fld = recSet.get(i).split("#");
//            adapter.add(fld[0] + "|" + fld[1] + "|" + fld[2] + "|" + fld[3]);
//        }
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpnName.setAdapter(adapter);
//        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
//        count_t.setText("共計:" + Integer.toString(dbHper.RecCount()) + "筆");
//        showRec(index);
    }

    private void u_setspinner() {//重構簡單化
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            adapter.add(fld[0] + " " + fld[1] + " " + fld[2] + " " + fld[3]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnName.setAdapter(adapter);

        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        //        mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
    }

    private void u_layout_def() {
        btAdd.setVisibility(View.INVISIBLE);
        btAbandon.setVisibility(View.INVISIBLE);
        btquery.setVisibility(View.INVISIBLE);
        btEdit.setVisibility(View.VISIBLE);
        btDel.setVisibility(View.VISIBLE);

        blinear01.setVisibility(View.VISIBLE);
        blinear02.setVisibility(View.INVISIBLE);
        btreport.setVisibility(View.INVISIBLE);

        b_id.setEnabled(false);
        //-----------------------
    }

    private final Spinner.OnItemSelectedListener mSpnNameOnItemSelLis=new Spinner.OnItemSelectedListener() {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int iSelect = mSpnName.getSelectedItemPosition();//找到按何項
            String[] fld = recSet.get(iSelect).split("#");
            String s = "資料:共" + recSet.size() + "筆" + "你按下" + (iSelect + 1) + "項";//起始為0
            tvTitle.setText(s);
//            count_t.setText("共計:" + Integer.toString(dbHper.RecCount()) + "筆");
            b_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
            b_id.setText(fld[0]);
            b_name.setText(fld[1]);
            b_grp.setText(fld[2]);
            b_address.setText(fld[3]);
            index = iSelect;
            if (btAdd.getVisibility() == View.VISIBLE) {
                b_name.setHint("請繼續輸入");
                b_id.setText("");
                b_name.setText("");
                b_grp.setText("");
                b_address.setText("");
            }
        }




                @Override
        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
            b_id.setText("");
            b_name.setText("");
            b_grp.setText("");
            b_address.setText("");
        }
    };
    private void initDB () {
        if (dbHper == null) {
            dbHper = new FriendDBHelpergarsign01(this, DB_FILE, null, DBversion);
            recSet=dbHper.getRecSet();
        }
    }
    private void showRec(int index) {
        msg = "";
        if (recSet.size() != 0) {
            String stHead = "顯示資料：第 " + (index + 1) + " 筆 / 共 " + recSet.size() + " 筆";
            msg = getString(R.string.count_t) + recSet.size() + "筆";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            b_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
            b_id.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            b_id.setText(fld[0]);
            b_name.setText(fld[1]);
            b_grp.setText(fld[2]);
            b_address.setText(fld[3]);
            mSpnName.setSelection(index, true); //spinner 小窗跳到第幾筆
        } else {
            String stHead = "顯示資料：0 筆";
            msg = getString(R.string.count_t) + "0筆";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            b_id.setText("");
            b_name.setText("");
            b_grp.setText("");
            b_address.setText("");
        }

        count_t.setText(msg);
        }

    @Override
    public void onClick(View v) {
        int rowsAffected;
        Uri uri;
        String s_id;
        String whereClause;
        String[] selectionArgs;
        switch (v.getId()) {
            case R.id.btIdtop:
                ctlFirst();
                break;
            case R.id.btIdNext:
                ctlNext();
                break;
            case R.id.btIdPrev:
                ctlPrev();
                break;
            case R.id.btIdend:
                ctlLast();
                break;
            //------------------------------------
//            case R.id.btnupdate:
//                // 資料更新
//                tid = b_id.getText().toString().trim();
//                tname = b_name.getText().toString().trim();
//                tgrp = b_grp.getText().toString().trim();
//                taddr = b_address.getText().toString().trim();
//                old_index=index;
//                String old_id = b_id.getText().toString();
//                mysql_update(); // 執行MySQL更新
//                dbmysql();
//                //-------------------------------------
//                recSet = dbHper.getRecSet();
//                u_setspinner();
////------------------------
////                用 old_id 搜尋 index
////                recSet=dbHper.getRecSet_query_id(old_id);
////--------------------------------
//                index=old_index;   //這是陽春型
//                showRec(index);
//                msg = "第 " + (index + 1) + " 筆記錄  已修改 ! " ;
//                ctlLast();
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//                break;

                //------------------------------------
//            case R.id.btIdDel:
//                // 刪除資料 --使用對話盒
//                MyAlertDialoggarsign01 myAltDlg = new MyAlertDialoggarsign01(this);
//                myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
//                myAltDlg.setTitle("刪除資料");
//                myAltDlg.setMessage("資料刪除無法復原\n確定將所有資料刪除嗎?");
//                myAltDlg.setCancelable(false);
//                myAltDlg.setIcon(android.R.drawable.ic_delete);
//                myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "確定刪除", aldBtListener);
//                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消刪除", aldBtListener);
//                myAltDlg.show();
//                break;
            //-----------------------
//            case R.id.btnAdd: //按下新增鈕
//                // 查詢name是否有有此筆資料
//                tname = b_name.getText().toString().trim();
//                tgrp = b_grp.getText().toString().trim();
//                taddr = b_address.getText().toString().trim();
//
//                if (tname.equals("") || tgrp.equals("")||taddr.equals("")) {
//                    Toast.makeText(getApplicationContext(), "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //-------直接增加到MySQL-------------------------------
//                mysql_insert();
//                dbmysql();
//                //----------------------------------------
//                msg = null;
//                // -------------------------
//                ContentValues newRow = new ContentValues();
//                newRow.put("name", tname);
//                newRow.put("grp", tgrp);
//                newRow.put("address", taddr);
//                //------------------------------
//
//                long rowID = dbHper.insertRec_m(newRow);
//                if (rowID != -1) {
//                    b_id.setHint("請繼續輸入");
//                    b_name.setText("");
//                    b_grp.setText("");
//                    b_address.setText("");
//                    msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + dbHper.RecCount() + " 筆記錄 !";
//                    ctlLast();  //成功跳到最後一筆
//                } else {
//                    msg = "新增記錄  失敗 !";
//                }
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                count_t.setText("共計:" + Integer.toString(dbHper.RecCount()) + "筆");
////                setupViewComponent();
//                u_insert();
////                ctlLast();  //成功跳到最後一筆
////                dbmysql();
//                break;
            //------------------------------------
//            case R.id.btnabandon: //按下放棄鈕
//                mSpnName.setEnabled(true);
//                Toast.makeText(getApplicationContext(), "*取消執行*", Toast.LENGTH_SHORT).show();
//                initDB();
//                u_setspinner();
//                u_layout_def();
//                ctlLast();  //成功跳到最後一筆
//                break;
            case R.id.btnquery: //按下查詢鈕
                tname = b_name.getText().toString().trim();
                tgrp = b_grp.getText().toString().trim();
                taddr = b_address.getText().toString().trim();
                msg = null;
                recSet = dbHper.getRecSet_query(tname, tgrp, taddr);
                Toast.makeText(getApplicationContext(), "顯示資料： 共 " + recSet.size() + " 筆", Toast.LENGTH_SHORT).show();
                u_setspinner();
                break;
//            case R.id.btnlist: //按下列表鈕
//                tname = b_name.getText().toString().trim();
//                tgrp = b_grp.getText().toString().trim();
//                taddr = b_address.getText().toString().trim();
//                msg = null;
//                recSet = dbHper.getRecSet_query(tname, tgrp, taddr);
//                Toast.makeText(getApplicationContext(), "顯示資料： 共 " + recSet.size() + " 筆", Toast.LENGTH_SHORT).show();
//                bsubTitle.setText("顯示資料： 共 " + recSet.size() + " 筆");
//===========取SQLite 資料=============
//                List<Map<String, Object>> mList;
//                mList = new ArrayList<Map<String, Object>>();
//                for (int i = 0; i < recSet.size(); i++) {
//                    Map<String, Object> item = new HashMap<String, Object>();
//                    String[] fld = recSet.get(i).split("#");
//                    item.put("imgView", R.drawable.userconfig);
//                    item.put("txtView", "id:" + fld[0] + "\nname:" + fld[1] + "\ngroup:" + fld[2] + "\naddr:" + fld[3]);
//                    mList.add(item);
//                }
//=========設定listview========
//                blinear01.setVisibility(View.INVISIBLE);
//                blinear02.setVisibility(View.VISIBLE);
//                SimpleAdapter adapter = new SimpleAdapter(
//                        this,
//                        mList,
//                        R.layout.list_item,
//                        new String[]{"imgView", "txtView"},
//                        new int[]{R.id.imgView, R.id.txtView}
//                );
//                listView.setAdapter(adapter);
//                listView.setTextFilterEnabled(true);
//                listView.setOnItemClickListener(listviewOnItemClkLis);
//                break;
            //------------------------------------
            //------------------------------------
//            case R.id.btidcancel:
////                blinear01.setVisibility(View.VISIBLE);
////                blinear02.setVisibility(View.INVISIBLE);
//                u_layout_def();
//                u_setspinner();
////                setupViewComponent();
//                break;
        }
    }

    private void mysql_update() {
        s_id = b_id.getText().toString().trim();
        tname = b_name.getText().toString().trim();
        tgrp = b_grp.getText().toString().trim();
        taddr = b_address.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(s_id);
        nameValuePairs.add(tname);
        nameValuePairs.add(tgrp);
        nameValuePairs.add( taddr);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnectorgars01.executeUpdate( nameValuePairs);
//-----------------------------------------------
    }

    private void mysql_insert() {
        //        sqlctl = "SELECT * FROM garbagetrucktest2 ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(tname);
        nameValuePairs.add(tgrp);
        nameValuePairs.add(taddr);
//        nameValuePairs.add("dkdkd");
//        nameValuePairs.add("fffff");
//        nameValuePairs.add("sssss");
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnectorgars01.executeInsert(nameValuePairs);  //真正執行新增
//-----------------------------------------------
    }

//    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String s = "你按下第 " + Integer.toString(position + 1) + "筆"
//                    + ((TextView) view.findViewById(R.id.txtView))
//                    .getText()
//                    .toString();
//            bsubTitle.setText(s);
//        }
//    };

//    private DialogInterface.OnClickListener aldBtListener=new DialogInterface.OnClickListener() {
//
//
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which) {
//                case BUTTON_POSITIVE:
//                    //                    int rowsAffected = dbHper.clearRec();  //刪除全部資料
////                tid = b_id.getText().toString().trim();
//                    old_index=index;
//                    // ---------------------------
//                    mysql_del();// 執行MySQL刪除
//                    dbmysql();
//                    // ---------------------------
//                    index=old_index;
//                    u_setspinner();
//                    if (index == dbHper.RecCount()) {
//                        index--;
//                    }
//                    showRec(index);
//                    msg = "資料已刪除" ;
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
//                    break;
//                case BUTTON_NEGATIVE:
//                    msg = "放棄刪除所有資料 !";
//                    break;
//            }
//            Toast.makeText(GarsignSql.this, msg, Toast.LENGTH_SHORT).show();
//
//
//        }
//    };

    private void mysql_del() {
        //---------
        s_id = b_id.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnectorgars01.executeDelet(nameValuePairs);   //執行刪除
//-----------------------------------------------
    }

    private void ctlLast() {
        index=recSet.size()-1;
        showRec(index);
    }

    private void ctlPrev() {
        index--;
        if (index<0)  index=recSet.size()-1;
        showRec(index);
    }

    private void ctlNext() {
        index++;
        if(index >= recSet.size())      index = 0;
        showRec(index);
    }

    private void ctlFirst() {
        // 第一筆
        index = 0;
        showRec(index);
    }
//--------------------生命週期------------------------

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){//cellphone特有方法滑動(摸手機)
//            case MotionEvent.ACTION_DOWN://你摸做甚麼事按下
//                x1=event.getX();
//                y1=event.getY();
//                break;
//            case MotionEvent.ACTION_UP://你摸 放開
//                x2=event.getX();
//                y2=event.getY();
//                // 判斷左右的方法，因為屏幕的左上角是：0，0 點右下角是max,max
//                // 並且移動距離需大於 > range
//                float xbar = Math.abs(x2 - x1);
//                float ybar = Math.abs(y2 - y1);
//                double z = Math.sqrt(xbar * xbar + ybar * ybar);
//                int angle = Math.round((float) (Math.asin(ybar / z) / Math.PI * 180));// 角度
//                if (x1 != 0 && y1 != 0) {
//                    if (x1 - x2 > range) { // 向左滑動
//                        ctlPrev();
//                    }
//                    if (x2 - x1 > range) { // 向右滑動
//                        ctlNext();
//                        // t001.setText("向右滑動\n" + "滑動參值x1=" + x1 + " x2=" + x2 + "
//                        // r=" + (x2 - x1)+"\n"+"ang="+angle);
//                    }
//                    if (y2 - y1 > range && angle > ran) { // 向下滑動
//                        // 往下角度需大於50
//                        // 最後一筆
//                        ctlLast();
//                    }
//                    if (y1 - y2 > range && angle > ran) { // 向上滑動
//                        // 往上角度需大於50
//                        ctlFirst();// 第一筆
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_MOVE://你摸移動到哪裡拖曳
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    private void u_insert() {
        btAdd.setVisibility(View.VISIBLE);
        btAbandon.setVisibility(View.VISIBLE);
        btEdit.setVisibility(View.INVISIBLE);
        btDel.setVisibility(View.INVISIBLE);
        b_id.setEnabled(false);
        b_id.setText("");
        b_name.setText("");
        b_grp.setText("");
        b_address.setText("");
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (dbHper == null)
            dbHper = new FriendDBHelpergarsign01(this, DB_FILE, null, DBversion);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //--------------------------------------------
    private void dbmysql() {
        // 讀取MySQL 資料
            sqlctl = "SELECT * FROM garbagetrucktest2 ORDER BY id ASC";
            ArrayList<String> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(sqlctl);
            try {
                String result = DBConnectorgars01.executeQuery(nameValuePairs);
                /**************************************************************************
                 * SQL 結果有多筆資料時使用JSONArray
                 * 只有一筆資料時直接建立JSONObject物件 JSONObject
                 * jsonData = new JSONObject(result);
                 **************************************************************************/
                //==========================================
                chk_httpstate();  //檢查 連結狀態
                //==========================================
                JSONArray jsonArray = new JSONArray(result);
                // -------------------------------------------------------
                if (jsonArray.length() > 0) { // MySQL 連結成功有資料  防止兩頭空
                    int rowsAffected = dbHper.clearRec();                 // 匯入前,刪除所有SQLite資料沒做的話id會重複會中斷
                    // 處理JASON 傳回來的每筆資料
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        ContentValues newRow = new ContentValues();
                        // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                        Iterator itt = jsonData.keys();
                        while (itt.hasNext()) {
                            String key = itt.next().toString();
                            String value = jsonData.getString(key); // 取出欄位的值
                            if (value == null) {
                                continue;
                            } else if ("".equals(value.trim())) {
                                continue;
                            } else {
                                jsonData.put(key, value.trim());
                            }
                            // ------------------------------------------------------------------
                            newRow.put(key, value.toString()); // 動態找出有幾個欄位
                            // -------------------------------------------------------------------
                        }
                        // ---(2) 使用固定已知欄位---------------------------
                        // newRow.put("id", jsonData.getString("id").toString());
                        // newRow.put("name",
                        // jsonData.getString("name").toString());
                        // newRow.put("grp", jsonData.getString("grp").toString());
                        // newRow.put("address", jsonData.getString("address")
                        // -------------------加入SQLite---------------------------------------
                        long rowID = dbHper.insertRec_m(newRow);
                        Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                    }
                    // ---------------------------
                } else {
                    Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
                }
                recSet = dbHper.getRecSet();  //重新載入SQLite
                u_setspinner();
                // --------------------------------------------------------
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }

    private void chk_httpstate() {
//**************************************************
//*       檢查連線狀況
//**************************************************
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
        if (DBConnectorgars01.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + DBConnectorgars01.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = DBConnectorgars01.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + DBConnectorgars01.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + DBConnectorgars01.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + DBConnectorgars01.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + DBConnectorgars01.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + DBConnectorgars01.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (DBConnectorgars01.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + DBConnectorgars01.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Red);
        }
        b_servermsg.setText(ser_msg);
        b_servermsg.setTextColor(servermsgcolor);

        //-------------------------------------------------------------------
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

