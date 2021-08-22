package tw.tcnr02.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Shop extends AppCompatActivity implements View.OnClickListener {

    private Button b001;
    private Intent intent = new Intent();
    private Uri uri;
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);
        setupViewComponent();
    }

    private void setupViewComponent() {
        b001=(Button)findViewById(R.id.sh_b001);

        b001.setOnClickListener(this);
    }

    //-----------生命週期-------------

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
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
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
            case R.id.menu_logout:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sh_b001:
                intent.putExtra("class_title", getString(R.string.shoplist_name));
                intent.setClass(Shop.this, Shop_listadd.class);
                break;

        }
        startActivity(intent);
    }
}