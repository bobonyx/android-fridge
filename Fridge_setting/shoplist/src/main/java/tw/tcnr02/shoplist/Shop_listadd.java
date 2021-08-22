package tw.tcnr02.shoplist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Shop_listadd extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_listadd);
        setupViewComponent();
    }

    private void setupViewComponent() {
    }

    @Override
    public void onClick(View v) {
        Intent intent=this.getIntent();        
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);

    }
}