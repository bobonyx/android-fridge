package tw.tcnr06.cooklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RANDOM_MEAL extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "tcnr07=>";
    private TextView t001;
    private Button b001;
    private double random_meal;
    private TextView tResult;
    private String answer;
    private Uri FBuri;

    //---------
    private DbHelper dbHper;
    private static final String DB_FILE = "Fridge.db";
    private static final String DB_TABLE = "RandomMeal";
    private static final int DBversion = 1;
    private ArrayList<String> recSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_meal);
        setupViewComponent();
    }

    private void setupViewComponent() {
        // 設定class標題
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);

        enableStrictMode(this);//使用暫存堆疊，必須加入此方法
        t001=(TextView)findViewById(R.id.RM_t001);
        tResult=(TextView)findViewById(R.id.RM_result);
        b001=(Button)findViewById(R.id.RM_b001);

        b001.setOnClickListener(this);

//------
        initDB();


    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();
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

    //------生命週期
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //------生命週期

    @Override
    public void onClick(View v) {
        random_meal = (int) (Math.random() * 9 + 1);
        switch((int) random_meal){
            case 1:
                answer = getString(R.string.RM_resultTEST01) + "" + getString(R.string.RM_result);
                break;
            case 2:
                answer = getString(R.string.RM_resultTEST02) + "" + getString(R.string.RM_result);
                break;
            case 3:
                answer = getString(R.string.RM_resultTEST03) + "" + getString(R.string.RM_result);
                break;
            case 4:
                answer = getString(R.string.RM_resultTEST04) + "" + getString(R.string.RM_result);
                break;
            case 5:
                answer = getString(R.string.RM_resultTEST05) + "" + getString(R.string.RM_result);
                break;
            case 6:
                answer = getString(R.string.RM_resultTEST06) + "" + getString(R.string.RM_result);
                break;
            case 7:
                answer = getString(R.string.RM_resultTEST07) + "" + getString(R.string.RM_result);
                break;
            case 8:
                answer = getString(R.string.RM_resultTEST08) + "" + getString(R.string.RM_result);
                break;
            case 9:
                answer = getString(R.string.RM_resultTEST09) + "" + getString(R.string.RM_result);
                break;
        }

//        random_meal = Math.random();
//        if(random_meal < 0.334){
////            tResult.setText("麥當勞");
//            answer = getString(R.string.RM_resultTEST01) + "," + getString(R.string.RM_result);
//        }else{
//            if (random_meal < 0.667) {
//                answer = getString(R.string.RM_resultTEST02) + "," + getString(R.string.RM_result);
//            } else {
//                if(random_meal <= 0.999){
//                    answer = getString(R.string.RM_resultTEST03) + "," + getString(R.string.RM_result);
//                }else{
//                }
//            }
//
//            }

            tResult.setText(answer);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        Intent it = new Intent();

        switch (item.getItemId()) {
            case R.id.menu_fb:
                FBuri = Uri.parse("https://www.facebook.com/kai.hao.9");
                it = new Intent(Intent.ACTION_VIEW,FBuri);
                startActivity(it);
                break;

            case R.id.menu_member:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_member)
                        .setMessage(getString(R.string.menu_member_message)+"\n"+"維尼、大神、佳佳、波波、柏榕、老大、培揚")
                        .setCancelable(false)
//                        .setIcon(R.drawable.icon02)
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

            case R.id.m_return:
                this.finish();
                break;

            //            case R.id.menu_notify:
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.menu_notify)
//                        .setMessage(getString(R.string.menu_message))
//                        .setCancelable(false)
////                        .setIcon(R.drawable.icon02)
//                        .setPositiveButton(R.string.menu_yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton(R.string.menu_no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}