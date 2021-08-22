package tw.tcnr06.cooklist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Copen01 extends ListActivity {

    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "recipe";
    private static final int DBversion = 1;
    private String tname;
    private String tgrp;
    private String taddress;
    private TextView t001;
    private TextView tvTitle;

    private EditText b_id, b_title, b_recipe_text;
    String t_title, t_recipe_text;
    String msg = null;

    private List<Map<String, Object>> mList;
    private ArrayList<String> recSet;
    private RelativeLayout blinear01;
    private LinearLayout blinear02;
    String TAG = "tcnr06=>";
    private String sqlctl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        setupViewComponent();
        initDB();
    }

    private void setupViewComponent() {
        initDB();
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.White));
        tvTitle.setBackgroundResource(R.color.FridgeDark);
        tvTitle.setText("顯示資料： 共 " + recSet.size() + " 筆");

//        b_title = (EditText) findViewById(R.id.et_title);
//        b_recipe_text = (EditText) findViewById(R.id.et_content);
//
//        t_title = b_title.getText().toString().trim();
//        t_recipe_text = b_recipe_text.getText().toString().trim();
//        msg = null;
//        recSet = dbHper.getRecSet_query(t_title, t_recipe_text);
//        Toast.makeText(getApplicationContext(), "顯示資料： 共 " + recSet.size() + " 筆", Toast.LENGTH_SHORT).show();

//        blinear01 = (RelativeLayout) findViewById(R.id.rel3);
//        blinear02 = (LinearLayout) findViewById(R.id.linear02);
        //recSet = dbHper.getRecSet_query(t_title,t_recipe_text);
        recSet = dbHper.getRecSet_query();
        //Toast.makeText(getApplicationContext(), "顯示資料： 共 " + recSet.size() + " 筆", Toast.LENGTH_SHORT).show();

        //===========取SQLite 資料=============
        List<Map<String, Object>> mList;
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet.get(i).split("#");
            item.put("imgView", R.drawable.userconfig);//要用url
            item.put("txtView", fld[1] + "\n"
                    +" 點擊食譜查看更多 "+ recSet.size());
            mList.add(item);
        }

        //=========設定listview========
//        blinear01.setVisibility(View.INVISIBLE);
//        blinear02.setVisibility(View.VISIBLE);
        SimpleAdapter adapter = new SimpleAdapter(this,
                mList, R.layout.list_item,
                new String[]{"imgView","txtView"},//代表layout中的欄位
                new int[]{R.id.imgView, R.id.txtView} );
        setListAdapter(adapter);

        //----------------------------------
        ListView listview = getListView();
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(listviewOnItemClkLis);
    }
    private void initDB() {
        if(dbHper==null){
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
            recSet=dbHper.getRecSet();
        }
    }
    // 讀取MySQL 資料
    private void dbmysql() {
        sqlctl = "SELECT * FROM recipe ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery(nameValuePairs);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                int rowsAffected = dbHper.clearRec(); // 匯入前,刪除所有SQLite資料(因為SQLite是暫存的資料，所以在匯入真正資料庫時要清空暫存資料，不然ID等資料會重複
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
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet();  //重新載入SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }



    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String s = "你按下第 "  + Integer.toString(position +1)   + "筆\n"
//                    + ((TextView) view.findViewById(R.id.txtView)).getText()   .toString();
            //tvTitle.setText(s);

            String s = "你按下第 "  + Integer.toString(position +1)   + "筆";
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }


    };






//--------------------生命週期------------------------
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
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }


    //--------------------------------------------
}//------------END