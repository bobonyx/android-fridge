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

    private DbHelper dbHper;
    //private DBConnector dbConnector;
    private static final String DB_FILE = "cook_friends.db";
    private static final String DB_TABLE = "recipe";
    private static final int DBversion = 1;
    private TextView tvTitle;

    private EditText b_id, b_title, b_recipe_text;
    String t_title, t_recipe_text;
    String ttitle, trecipe_text;
    String msg = null;

    private List<Map<String, Object>> mList;
    private ArrayList<String> recSet;
    private RelativeLayout blinear01;
    private LinearLayout blinear02;
    String TAG = "tcnr06=>";
    private String sqlctl;
    private int index = 0;

    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;
    private String s_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        setupViewComponent();
        initDB();
    }

    private void setupViewComponent() {

//        initDB();
//        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
//        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.White));
//        tvTitle.setBackgroundResource(R.color.FridgeDark);
//        tvTitle.setText("??????????????? ??? " + recSet.size() + " ???");
//
////        b_title = (EditText) findViewById(R.id.et_title);
////        b_recipe_text = (EditText) findViewById(R.id.et_content);
////
////        t_title = b_title.getText().toString().trim();
////        t_recipe_text = b_recipe_text.getText().toString().trim();
////        msg = null;
////        recSet = dbHper.getRecSet_query(t_title, t_recipe_text);
////        Toast.makeText(getApplicationContext(), "??????????????? ??? " + recSet.size() + " ???", Toast.LENGTH_SHORT).show();
//
////        blinear01 = (RelativeLayout) findViewById(R.id.rel3);
////        blinear02 = (LinearLayout) findViewById(R.id.linear02);
////        recSet = dbHper.getRecSet_query(t_title,t_recipe_text);
//        //recSet = dbHper.getRecSet_query();//???sqlite
//        recSet = dbHper.getRecSet_query(ttitle,trecipe_text);//???sqlite
//        //Toast.makeText(getApplicationContext(), "??????????????? ??? " + recSet.size() + " ???", Toast.LENGTH_SHORT).show();
//
//        //===========???SQLite ??????=============
//        List<Map<String, Object>> mList;
//        mList = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < recSet.size(); i++) {
//            Map<String, Object> item = new HashMap<String, Object>();
//            String[] fld = recSet.get(i).split("#");
//            item.put("imgView", R.drawable.userconfig);//??????url
//            item.put("txtView", fld[1] + "\n"
//                    +" ???????????????????????? "+fld[0]);
//            mList.add(item);
//        }
//
//        //=========??????listview========
//
//        SimpleAdapter adapter = new SimpleAdapter(this,
//                mList, R.layout.list_item,
//                new String[]{"imgView","txtView"},//??????layout????????????
//                new int[]{R.id.imgView, R.id.txtView} );
//        setListAdapter(adapter);
//
//        //----------------------------------
//        ListView listview = getListView();
//        listview.setTextFilterEnabled(true);
//        listview.setOnItemClickListener(listviewOnItemClkLis);
    }
    private void initDB() {
        if(dbHper==null){
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
            recSet=dbHper.getRecSet_Cook();
        }
    }
    // ??????MySQL ??????
    private void dbmysql() {
        sqlctl = "SELECT * FROM recipe ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Cook_DBConnector.executeQuery_Cook(nameValuePairs);
            /**************************************************************************
             * SQL ??????????????????????????????JSONArray
             * ?????????????????????????????????JSONObject?????? JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL ?????????????????????
                int rowsAffected = dbHper.clearRec_Cook(); // ?????????,????????????SQLite??????(??????SQLite????????????????????????????????????????????????????????????????????????????????????ID??????????????????
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
                    long rowID = dbHper.insertRec_Cookm(newRow);
                    //Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length()) + " ?????????", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Cook();  //????????????SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void mysql_del() {
        //---------
        s_id = b_id.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Cook_DBConnector.executeDelet_Cook(nameValuePairs);   //????????????
//-----------------------------------------------
    }

    private void showRec(int index) {

        msg = "";
        if (recSet.size() != 0) {
            String stHead = "?????????????????? " + (index + 1) + " ??? / ??? " + recSet.size() + " ???";
            msg = getString(R.string.count_t) + recSet.size() + "???";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            b_id.setTextColor(ContextCompat.getColor(this, R.color.Red));
            b_id.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow));
            b_id.setText(fld[0]);
            b_title.setText(fld[1]);
            b_recipe_text.setText(fld[2]);

            //mSpnName.setSelection(index, true); //spinner ?????????????????????
        } else {
            String stHead = "???????????????0 ???";
            msg = getString(R.string.count_t) + "0???";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            b_id.setText("");
            b_title.setText("");
            b_recipe_text.setText("");

        }

        //count_t.setText(msg);


    }



    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String s = "???????????? "  + Integer.toString(position +1)   + "???\n"
//                    + ((TextView) view.findViewById(R.id.txtView)).getText()   .toString();
            //tvTitle.setText(s);

            String s = "???????????? "  + Integer.toString(position +1)   + "???";
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            // ???????????? --???????????????
            Cook_MyAlertDialog myAltDlg = new Cook_MyAlertDialog(Copen01.this);
            myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
            myAltDlg.setTitle("????????????");
            myAltDlg.setMessage("????????????????????????\n????????????"+ Integer.toString(position +1)   +"???????????????????");
            myAltDlg.setCancelable(false);
            myAltDlg.setIcon(android.R.drawable.ic_delete);
            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "????????????", aldBtListener);
            myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", aldBtListener);
            myAltDlg.show();
        }


    };


    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {

        private int old_index;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:

                    old_index=index;
                    // ---------------------------
                    mysql_del();// ??????MySQL??????
                    dbmysql();
                    // ---------------------------
                    index=old_index;
                    //u_setspinner();
                    if (index == dbHper.RecCount_Cook()) {
                        index--;
                    }
                    showRec(index);
//                    mSpnName.setSelection(index, true); //spinner ?????????????????????
//                }
                    msg = "??????????????????" ;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;

                case BUTTON_NEGATIVE:
                    msg = "?????????????????????";
                    Toast.makeText(Copen01.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };






//--------------------????????????------------------------
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
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }


    //--------------------------------------------
}//------------END