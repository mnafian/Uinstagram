package net.mnafian.uinstagram.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.mnafian.uinstagram.R;
import net.mnafian.uinstagram.Utilities.StaticClass;

import java.io.File;

/**
 * Created by mnafian on 30/10/15.
 */
public class ViewResultActivity extends AppCompatActivity {

    private ImageView imageResult;
    private CoordinatorLayout reqCoorlayout;
    private ImageButton imgShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uin_viewresult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reqCoorlayout = (CoordinatorLayout) findViewById(R.id.main_coor);
        imageResult = (ImageView) findViewById(R.id.uin_detailimage_result);
        imgShare = (ImageButton) findViewById(R.id.sm_btn_share_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String path = extras.getString(StaticClass.GET_IMAGE_MESSAGE);

            assert path != null;
            final String root = Environment.getExternalStorageDirectory().toString();
            File imageFile = new File(root + "/Uinstagram/" + path);
            if(imageFile.exists()){
                imageResult.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));

                imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mediaPath = root + "/Uinstagram/" + path;

                        String caption = "Share gambar kamu";

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");

                        File media = new File(mediaPath);
                        Uri uri = Uri.fromFile(media);

                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_TEXT, caption);
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(share);
                    }
                });

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_data:
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
