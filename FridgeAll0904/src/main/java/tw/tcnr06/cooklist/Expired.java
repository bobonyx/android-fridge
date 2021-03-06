package tw.tcnr06.cooklist;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Expired extends AppCompatActivity implements View.OnClickListener {
    private TextView count_t;
    private EditText exp_id, exp_name, exp_date;


    private TextView tvTitle;
    private Button btNext, btPrev, btTop, btEnd;
    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;
    //--------------------------


    private Button btEdit, btDel;
    String tname, tgrp, taddr;

    private Spinner mSpnName;
    int up_item = 0;
    //------------------------------
    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;
    private Button btAdd, btAbandon, btquery, btcancel, btreport;

    //    private static ContentResolver mContRes;
    private String[] MYCOLUMN = new String[]{"id", "name", "dates"};
    int tcount;

    // ------------------
    public static String myselection = "";
    public static String myorder = "id ASC"; // ????????????
    public static String myargs[] = new String[]{};

    private LinearLayout blinear02;
    private ListView listView;
    private TextView bsubTitle;
    private RelativeLayout blinear01;
    //===============
    private tw.tcnr06.cooklist.DbHelper dbHper;
    private static final String DB_FILE = "fridge.db"; //???????????????
    private static final String DB_TABLE = "fridge";
    private static final int DBversion = 1;
    //-----------------
    private String sqlctl;
    private String tid;
    private String s_id;
    private String ser_msg;
    private int servermsgcolor;
    private TextView b_servermsg;
    //--------------------------
    // ----------????????????--------------------------------
    private Long startTime;
    private Handler handler = new Handler();

//    int autotime =120;// ?????????????????? ????????????MySQL??????
    //------------------------------
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    private TextView nowtime;  //???????????????????????????
    int update_time = 0;
    private String str;



    private Uri uri;

    // --------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableStrictMode(this);//??????Thead ???????????????
        setContentView(R.layout.expsign01);
//        setupViewComponent();
    }

    private void enableStrictMode(Context context) {
        //-------------????????????????????????????????????------------------------------
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

        // ??????class??????
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);


        tvTitle = (TextView) findViewById(R.id.tvIdTitle); //??????????????????
        exp_id = (EditText) findViewById(R.id.edid); //id????????????
        exp_name = (EditText) findViewById(R.id.edtName);//????????????
        exp_date = (EditText) findViewById(R.id.edtGrp);//????????????

        b_servermsg=(TextView)findViewById(R.id.tserver_msg);
        btNext = (Button) findViewById(R.id.btIdNext);//?????????
        btPrev = (Button) findViewById(R.id.btIdPrev);//?????????
        btTop = (Button) findViewById(R.id.btIdtop);//??????
        btEnd = (Button) findViewById(R.id.btIdend);//??????

        btEdit = (Button) findViewById(R.id.btnupdate);//??????
        btDel = (Button) findViewById(R.id.btIdDel);//??????

        //-----------------------
        btAdd = (Button) findViewById(R.id.btnAdd);//??????
        btAbandon = (Button) findViewById(R.id.btnabandon);//????????????
        btquery = (Button) findViewById(R.id.btnquery);//??????
        btcancel = (Button) findViewById(R.id.btidcancel);// list item ??????????????????????????????
        btreport = (Button) findViewById(R.id.btnlist);//??????

        blinear01 = (RelativeLayout) findViewById(R.id.relative01);
        blinear02 = (LinearLayout) findViewById(R.id.linear02);

        listView = (ListView) findViewById(R.id.listView);// list item ????????????
        bsubTitle = (TextView) findViewById(R.id.subTitle);// list item ??????????????????
        //---------??????layout ??????---------------
        u_layout_def();
        //-----------------------------------------???????????????

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

        b_servermsg = (TextView) findViewById(R.id.servermsg);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));
        tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Aqua));

        mSpnName = (Spinner) this.findViewById(R.id.spnName);

        //===================================
        nowtime = (TextView) findViewById(R.id.now_time);
        startTime = System.currentTimeMillis();        // ??????????????????

        handler.postDelayed(updateTimer, 1000);  // ??????Delay?????????
        //===================================
        java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis()); // ????????????????????
        str = formatter.format(curDate);
        nowtime.setText(getString(R.string.exp_nowtime) + str);
        // ----------------------------------------

        initDB();
        showRec(index);//????????????
        u_setspinner();
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Navy));//??????????????????????????????
        tvTitle.setText("??????????????? ???" + tcount + " ???");
        exp_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
        // -------------------------
        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);//????????????
//-------------------------------------------------------------------------


    }
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            old_index = mSpnName.getSelectedItemPosition();
            Long spentTime = System.currentTimeMillis() - startTime;
            String hours = String.format("%02d", (spentTime / 1000) / 60 / 60);  // ???????????????????????????
            String minius = String.format("%02d", ((spentTime / 1000) / 60) % 60);  // ???????????????????????????
            String seconds = String.format("%02d", (spentTime / 1000) % 60);          // ????????????????????????
//            handler.postDelayed(this, autotime * 1000); // ?????????????????????
            // -------????????????MySQL
            dbmysql();
            recSet = dbHper.getRecSetexp();  //????????????SQLite
            u_setspinner();  //????????????spinner??????
            index = old_index;
            showRec(index); //??????spainner ???????????????????????????
            //-------------------------------------------------------------------------------
            ++update_time;
//            nowtime.setText(getString(R.string.now_time) + "(???" + autotime + "???)" + str + "->"
//                    + hours + ":" + minius + ":" + seconds
//                    + " (" + (update_time) + "???)");
//            //------ ???????????? ---------------------------
//            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
//            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
//            toneG.release();
//            // --------------------------------------------------------
        }
    };



  private void u_setspinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            adapter.add(fld[1] + " " + fld[2]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//??????????????????????????????
        mSpnName.setAdapter(adapter);

        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);//????????????
        //        mSpnName.setSelection(index, true); //spinner ?????????????????????

    }

    private void initDB() { //???????????????
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSetexp();//??????all data
    }

    private View.OnClickListener btn01On = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//        intent.putExtra("class_title87", getString(R.string.exp_clean));//????????????
//            intent.setClass(Expired.this, Garsign01.class);//???m0607???????????????
//            startActivity(intent);
        }
    };

      //??????item



    //---------?????????4?????????????????????-------
//    private View.OnClickListener MainBtnOn =new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.fri_b001:
//                    intent.putExtra("class_title",getString(R.string.app_name));
//                    intent.setClass(Expired.this,Frisign01.class );
//                    break;
//                case R.id.fri_b002:
//                    intent.putExtra("class_title",getString(R.string.expired_name));
//                    intent.setClass(Expired.this,Expired.class );
//                    break;
//                case R.id.fri_b003:
//                    intent.putExtra("class_title",getString(R.string.shop_name));
//                    intent.setClass(Expired.this,Shop.class );
//                    break;
//                case R.id.fri_b004:
//                    intent.putExtra("class_title",getString(R.string.cook_name));
//                    intent.setClass(Expired.this,Cooklist.class );
//                    break;
//            }
//            startActivity(intent);
//        }
//    };



    private void u_layout_def() {

        btAdd.setVisibility(View.INVISIBLE); //??????
        btAbandon.setVisibility(View.INVISIBLE);
        btquery.setVisibility(View.INVISIBLE);
        btEdit.setVisibility(View.INVISIBLE); //??????
        btDel.setVisibility(View.VISIBLE);

        blinear01.setVisibility(View.VISIBLE);
        blinear02.setVisibility(View.INVISIBLE);
        btreport.setVisibility(View.VISIBLE);
        exp_date.setEnabled(false);
        exp_id.setEnabled(false);//id????????????
        //-----------------------

    }

    private Spinner.OnItemSelectedListener mSpnNameOnItemSelLis = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position,
                                   long id) {
            int iSelect = mSpnName.getSelectedItemPosition(); //???????????????
            String[] fld = recSet.get(iSelect).split("#");
            String s = "????????????" + recSet.size() + " ???," + "?????????  " + String.valueOf(iSelect + 1) + "???"; //?????????0
            tvTitle.setText(s);
            exp_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
            exp_id.setText(fld[0]);
            exp_name.setText(fld[1]);
            exp_date.setText(fld[2]);

            //-------???????????????item---
            index = iSelect;
            // -----????????????????????????---------------
            if(btAdd.getVisibility() == View.VISIBLE){
                exp_name.setHint("");
                exp_id.setHint("");
                exp_id.setText("");
                exp_name.setText("");
                exp_date.setText("");

            }
            //--------------------------------
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            exp_id.setText("");
            exp_name.setText("");
            exp_date.setText("");

        }
    };

    @Override
    public void onClick(View v) {
        int rowsAffected;
        Uri uri;
        String s_id;
        String whereClause;
        String[] selectionArgs;
        Log.d(TAG, "a=" + v.getId());
        switch (v.getId()) {
            case R.id.btIdNext:
                ctlNext();
                break;
            case R.id.btIdPrev:
                ctlPrev();
                break;
            case R.id.btIdtop:
                ctlFirst();
                break;
            case R.id.btIdend:
                ctlLast();
                break;
            //------------------------------------
            case R.id.btnupdate:
                // ????????????
                tid = exp_id.getText().toString().trim();
                tname = exp_name.getText().toString().trim();
                tgrp = exp_date.getText().toString().trim();
                old_index=index;
                mysql_update(); // ??????MySQL??????
                dbmysql();
                //-------------------------------------
                recSet = dbHper.getRecSetexp();
                u_setspinner();
                index=old_index;
                showRec(index);
                msg = "??? " + (index + 1) + " ?????????  ????????? ! " ;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                ctlLast();
                break;

                ////                old_index=index;
//                String old_id = exp_id.getText().toString();
//                mysql_update(); // ??????MySQL??????
//                dbmysql();
//                //-------------------------------------
////             recSet = dbHper.getRecSetexp();  //???????????????
//                u_setspinner();
//
////                ??? old_id ?????? index
//              recSet = dbHper.getRecSet_queryexp_id(old_id);



////--------------------------------
//                index=old_index;   //???????????????
//
//                showRec(index);
//                msg = "??? " + (index + 1) + " ?????????  ????????? ! " ;
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//                ctlLast();


            //------------------------------------
            case R.id.btIdDel:
                // ???????????? --???????????????
                Cook_MyAlertDialog myAltDlg = new Cook_MyAlertDialog(this);
                myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
                myAltDlg.setTitle("????????????");
                myAltDlg.setMessage("????????????????????????\n???????????????????????????????");
                myAltDlg.setCancelable(false);
                myAltDlg.setIcon(android.R.drawable.ic_delete);
                myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "????????????", aldBtListener);
                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", aldBtListener);
                myAltDlg.show();
                break;
            //-----------------------
            case R.id.btnAdd: //???????????????
                // ??????name????????????????????????

                // ??????name????????????????????????
                tname = exp_name.getText().toString().trim();
                tgrp = exp_date.getText().toString().trim();



                //-------???????????????MySQL--------------
                mysql_insert();
                dbmysql();
                //----------------------------------------
                msg = null;
                // -------------------------
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy???MM???dd??? hh:mm:ss");
                Date date =new Date(System.currentTimeMillis());
                String time=simpleDateFormat.format(date);

                ContentValues newRow = new ContentValues();
                newRow.put("name", tname);
                newRow.put("dates", time);

                //------------------------------

                long rowID = dbHper.insertRecexp(newRow);
                if (rowID != -1) {
                    exp_id.setHint("");
                    exp_name.setText("");
                    exp_date.setText("");
                    msg = "????????????  ?????? ! \n" + "????????????????????? " + dbHper.RecCountexp() + " ????????? !";
                    ctlLast();  //????????????????????????
                } else {
                    msg = "????????????  ?????? !";
                }
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//                setupViewComponent();
                dbmysql();
                ctlLast();  //????????????????????????
                u_insert();


                break;

            case R.id.btnabandon: //???????????????
                mSpnName.setEnabled(true);
//                Toast.makeText(getApplicationContext(), "*????????????*", Toast.LENGTH_SHORT).show();
                initDB();
                u_setspinner();
                u_layout_def();
                ctlLast();  //????????????????????????
                break;

            case R.id.btnquery: //???????????????
                tname = exp_name.getText().toString().trim();
                tgrp = exp_date.getText().toString().trim();
                exp_name.setText("");
                exp_date.setText("");
                msg = null;
                recSet = dbHper.getRecSet_queryexp(tname, tgrp);
//                Toast.makeText(getApplicationContext(), "??????????????? ??? " + recSet.size() + " ???", Toast.LENGTH_SHORT).show();
                u_setspinner();
                break;
            case R.id.btnlist: //???????????????

                exp_id.setEnabled(false);
                exp_id.setText("");
                exp_name.setText("");
                exp_date.setText("");
                exp_id.setHint("");

                tname = exp_name.getText().toString().trim();
                tgrp = exp_date.getText().toString().trim();
                msg = null;
                recSet = dbHper.getRecSet_queryexp(tname, tgrp);
//                Toast.makeText(getApplicationContext(), "??????????????? ??? " + recSet.size() + " ???", Toast.LENGTH_SHORT).show();
//                bsubTitle.setText("??????????????? ??? " + recSet.size() + " ???");
//===========???SQLite ??????=============
                List<Map<String, Object>> mList;
                mList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < recSet.size(); i++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    String[] fld = recSet.get(i).split("#");
                    item.put("imgView", R.drawable.trashcan_btn);
                    item.put("txtView", "\n????????????:" + fld[1] + "\n????????????:" + fld[2]);
                    mList.add(item);
                }
//=========??????listview========
                blinear01.setVisibility(View.INVISIBLE);
                blinear02.setVisibility(View.VISIBLE);
                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        mList,
                        R.layout.exp_list_item,
                        new String[]{"imgView", "txtView"},
                        new int[]{R.id.imgView, R.id.txtView}
                );
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);
//                listView.setOnItemClickListener(listviewOnItemClkLis);
//                listView.setOnItemLongClickListener(gg);
                break;
            //------------------------------------

            case R.id.btidcancel:
//                blinear01.setVisibility(View.VISIBLE);
//                blinear02.setVisibility(View.INVISIBLE);
                u_layout_def();
                u_setspinner();
//                setupViewComponent();
                break;



            //--------------------------
            // ----------
            //-------------

        }
    }


    private void mysql_update() {
        s_id = exp_id.getText().toString().trim();
        tname = exp_name.getText().toString().trim();
        tgrp = exp_date.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(s_id);
        nameValuePairs.add(tname);
        nameValuePairs.add(tgrp);

        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Cook_DBConnector.executeUpdate( nameValuePairs);
//-----------------------------------------------


    }


    private void mysql_insert() {
        //        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(tname);
        nameValuePairs.add(tgrp);

        try {
            Thread.sleep(500); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Cook_DBConnector.executeInsertexp(nameValuePairs);  //??????????????????
//-----------------------------------------------
    }

//    private AdapterView.OnItemLongClickListener gg =new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            MyAlertDialogexp myAltDlg = new MyAlertDialogexp(Expired.this);
//            myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
//            myAltDlg.setTitle("????????????");
//            myAltDlg.setMessage("????????????????????????\n????????????????????????????");
//            myAltDlg.setCancelable(false);
//            myAltDlg.setIcon(android.R.drawable.ic_delete);
//            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "????????????", aldBtListener);
//            myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", aldBtListener);
//            dbmysql();
//            myAltDlg.show();
//
//            return true;
//        }
//    };


//    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String s = "???????????? " + Integer.toString(position + 1) + "???"
//                    + ((TextView) view.findViewById(R.id.txtView))
//                    .getText()
//                    .toString();
//            bsubTitle.setText(s);
//        }
//    };
    private int old_index;
    // ---------------------------------------------
    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() { //???????????????dialog

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    //                    int rowsAffected = dbHper.clearRecexp();  //??????????????????
//                tid = exp_id.getText().toString().trim();
                    old_index=index;
                    // ---------------------------
                    mysql_del();// ??????MySQL??????

                    dbmysql();
                    // ---------------------------
                    index=old_index;
                    u_setspinner();
                    if (index == dbHper.RecCountexp()) {
                        index--;
                    }
                    showRec(index);
//                    mSpnName.setSelection(index, true); //spinner ?????????????????????
//                }
//                    msg = "???????????????" ;
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEGATIVE:
//                    msg = "???????????????????????? !";
//                    Toast.makeText(Expired.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void mysql_del() {
        //---------
        s_id = exp_id.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Cook_DBConnector.executeDelet(nameValuePairs);   //????????????
//-----------------------------------------------
    }


    private void showRec(int index) {
        msg = "";
        if (recSet.size() != 0) {
            String stHead = "?????????????????? " + (index + 1) + " ??? / ??? " + recSet.size() + " ???";
            msg = getString(R.string.exp_count_t) + recSet.size() + "???";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            exp_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
            exp_id.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            exp_id.setText(fld[0]);
            exp_name.setText(fld[1]);
            exp_date.setText(fld[2]);
            mSpnName.setSelection(index, true); //spinner ?????????????????????
        } else {
            String stHead = "???????????????0 ???";
            msg = getString(R.string.exp_count_t) + "0???";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            exp_id.setText("");
            exp_name.setText("");
            exp_date.setText("");
        }


        }


    //------------------------------------------------
    private void ctlFirst() {
        // ?????????
        index = 0;
        showRec(index);
    }

    private void ctlPrev() {
        // ?????????
        index--;
        if (index < 0)
            index = recSet.size() - 1;
        showRec(index);
    }

    private void ctlNext() {
        // ?????????
        index++;
        if (index >= recSet.size())
            index = 0;
        showRec(index);
    }


    private void ctlLast() {
        // ????????????
        index = recSet.size() - 1;
        showRec(index);
    }

    //---------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        dbmysql();
        setupViewComponent();//onCreate(null); // ??????

    }




    private void u_insert() {
        btAdd.setVisibility(View.VISIBLE);
        btAbandon.setVisibility(View.VISIBLE);
        btEdit.setVisibility(View.INVISIBLE);
        btDel.setVisibility(View.INVISIBLE);
//        b_edid.setEnabled(false);
//        b_edid.setText("");
//        e001.setText("");
//        e002.setText("");
//        e003.setText("");

    }
    // ??????MySQL ??????
    private void dbmysql() {
        sqlctl = "SELECT * FROM fridge ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Cook_DBConnector.executeQueryexp(nameValuePairs);
            /**************************************************************************
             * SQL ??????????????????????????????JSONArray
             * ?????????????????????????????????JSONObject?????? JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //==========================================
//            chk_httpstate();  //?????? ????????????
//==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL ?????????????????????
                int rowsAffected = dbHper.clearRecexp(); // ?????????,????????????SQLite??????(??????SQLite????????????????????????????????????????????????????????????????????????????????????ID??????????????????
                // ??????JASON ????????????????????????
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // ??????????????????
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // ???????????????????????????
                        // -------------------------------------------------------------------
                    }
                    // ---(2) ????????????????????????---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------??????SQLite---------------------------------------
                    long rowID = dbHper.insertRecexp(newRow);
//                    Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length()) + " ?????????", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSetexp();  //????????????SQLite
            u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

//    private void chk_httpstate() {
//        //**************************************************
////*       ??????????????????
////**************************************************
//        //?????????????????? DBConnector01.httpstate ?????????????????? 200(??????????????????)
//        if (tw.tcnr09.expired.DBConnector.httpstate == 200) {
//            ser_msg = "?????????????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//            servermsgcolor = ContextCompat.getColor(this, R.color.Blue);
////                Toast.makeText(getBaseContext(), "???????????????????????? ",
////                        Toast.LENGTH_SHORT).show();
//        } else {
//            int checkcode = tw.tcnr09.expired.DBConnector.httpstate / 100;
//            switch (checkcode) {
//                case 1:
//                    ser_msg = "????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//                    break;
//                case 2:
//                    ser_msg = "????????????????????????????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//                    break;
//                case 3:
//                    ser_msg = "??????????????????????????????????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//                case 4:
//                    ser_msg = "???????????????????????????????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//                case 5:
//                    ser_msg = "?????????error responses??????????????????(code:" + tw.tcnr09.expired.DBConnector.httpstate + ") ";
//                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//                    break;
//            }
////                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//        }
//        if (tw.tcnr09.expired.DBConnector.httpstate == 0) {
//            ser_msg = "?????????????????????(code:" + DBConnector.httpstate + ") ";
//            servermsgcolor = ContextCompat.getColor(this, R.color.Red);
//        }
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);
//
//        //-------------------------------------------------------------------
//    }
    //------MENU-------




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainexp, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = new Intent();
        switch (item.getItemId()) {
            case R.id.m_add://??????
                exp_id.setText("");
                exp_name.setText("");
                exp_date.setText("");
                exp_name.setHint("?????????");
                exp_date.setHint("");

                u_insert();
                break;
            case R.id.m_query://??????
                btAdd.setVisibility(View.INVISIBLE);
                btAbandon.setVisibility(View.VISIBLE);
                btEdit.setVisibility(View.INVISIBLE);
                btDel.setVisibility(View.INVISIBLE);
                btquery.setVisibility(View.VISIBLE);

                exp_id.setEnabled(false);
                exp_id.setText("");
                exp_name.setText("");
                exp_date.setText("");
                exp_id.setHint("");
                break;


//
//            case R.id.m_mysql:
//                // ??????MySQL
//                dbmysql();
//                break;
//
//

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
                        .setIcon(R.drawable.circle)
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
                        .setMessage(getString(R.string.menu_member_message)+"\n"+"????????????????????????????????????????????????????????????")
                        .setCancelable(false)
                        .setIcon(R.drawable.circle)
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
//                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
                        .show();

                break;

            case R.id.menu_return:
                this.finish();
                break;

            case R.id.menu_logout:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
