package tw.tcnr06.cooklist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;

public class CEdit extends AppCompatActivity implements View.OnClickListener{

    String TAG = "tcnr06=>";
    private Button btn_cancel,btn_ok;
    private Intent intent = new Intent();
    //===============
    private FriendDbHelper dbHper;
    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE = "recipe";
    private static final int DBversion = 1;
    private ArrayList<String> recSet;

    private EditText b_id, b_title, b_recipe_text;
    String t_title, t_recipe_text;
    String msg = null;
    private TextView count_t;
    private String sqlctl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//使用暫存堆疊，必須加入此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cedit);
        setupViewComponent();
    }

    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續-------------------
        //----怕連上000時卡住，先把資料暫存，等主機OK再上傳
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

        btn_ok=(Button)findViewById(R.id.btn_ok);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);

        b_title = (EditText) findViewById(R.id.et_title);
        b_recipe_text = (EditText) findViewById(R.id.et_content);


        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        //------------------
        initDB();

    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper(this, DB_FILE, null, DBversion);
            recSet = dbHper.getRecSet();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //-----------------------
            case R.id.btn_ok: //按下新增鈕
                // 查詢name是否有有此筆資料
                t_title = b_title.getText().toString().trim();
                t_recipe_text = b_recipe_text.getText().toString().trim();

                if (t_title.equals("") || t_recipe_text.equals("")) {
                    Toast.makeText(getApplicationContext(), "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //-------------培揚寫法存入暫存----------
//                myDBhelperexp=new MyDBhelperexp(Record.this, "expiredmod.db",null,1);
//                Boolean flag=myDBhelperexp.insertData(content);
//                if(flag==true){
//                    //如果添加成功將數據回傳的結果設置為2
//                    setResult(2);
//                    Toast.makeText(Record.this, R.string.exp_saveok,Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(Record.this, R.string.exp_savenotok,Toast.LENGTH_SHORT).show();
//                }
                //-------------培揚寫法存入暫存----------

                //-------直接增加到MySQL--------------
                mysql_insert();//寫入資料庫
                dbmysql();
                //----------------------------------------
                msg = null;
                // -------------------------
                ContentValues newRow = new ContentValues();
                newRow.put("title", t_title);
                newRow.put("recipe_text", t_recipe_text);

                //------------------------------

                long rowID = dbHper.insertRec_m(newRow);
//                long rowID = dbHper.insertRec(t_title, t_recipe_text);//寫入暫存
                if (rowID != -1) {
                    //b_id.setHint("請繼續輸入");
                    //b_title.setText("");
                    //b_recipe_text.setText("");

                    msg = "新增食譜  成功 !\n" + "目前共有 " + dbHper.RecCount() + " 筆食譜 !";;
                    //ctlLast();  //成功跳到最後一筆
                } else {
                    msg = "新增食譜  失敗 !";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                //count_t.setText("共計:" + Integer.toString(dbHper.RecCount()) + "筆");
                    setupViewComponent();
                    u_insert();
                //dbmysql(); //重新匯入
                break;
            //------------------------------------

            case R.id.btn_cancel:

                Toast.makeText(CEdit.this, "*取消新增*", Toast.LENGTH_SHORT).show();
                intent.setClass(CEdit.this, Cooklist.class);
                //  執行指定的class
                startActivity(intent);
                break;
        }

    }//onClick END---------------------

    private void u_insert() {
//        btAdd.setVisibility(View.VISIBLE);
//        btAbandon.setVisibility(View.VISIBLE);
//        btEdit.setVisibility(View.INVISIBLE);
//        btDel.setVisibility(View.INVISIBLE);
//        b_id.setEnabled(false);
        b_title.setHint("為你的食譜取個好名字吧！");
        //b_id.setText("");
        b_title.setText("");
        b_recipe_text.setText("");


    }

//    private void ctlLast() {
//        // 最後一筆
//        index = recSet.size() - 1;
//        showRec(index);
//    }

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

    private void mysql_insert() {
//        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(t_title);
        nameValuePairs.add(t_recipe_text);

        //--------寫死的參數
//        nameValuePairs.add("456456456");
//        nameValuePairs.add("454566");
//        nameValuePairs.add("777777");
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeInsert(nameValuePairs);  //真正執行新增
//-----------------------------------------------

    }
}//END