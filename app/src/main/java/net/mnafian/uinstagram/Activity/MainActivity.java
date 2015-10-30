package net.mnafian.uinstagram.Activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import net.mnafian.uinstagram.Adapter.GetImageAdapter;
import net.mnafian.uinstagram.Adapter.ImageInstaAdapter;
import net.mnafian.uinstagram.Model.FilesItem;
import net.mnafian.uinstagram.R;
import net.mnafian.uinstagram.Utilities.StaticClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    File file;
    private File[] listFile;
    private CoordinatorLayout reqCoorlayout;
    private RecyclerView uinRecList;
    private Intent intent;
    private String imgDecodableString;
    private Uri imageUri;
    private List<FilesItem> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reqCoorlayout = (CoordinatorLayout) findViewById(R.id.main_coor);
        uinRecList = (RecyclerView) findViewById(R.id.sm_imagelist_rcview);
        uinRecList.setHasFixedSize(true);
        RecyclerView.LayoutManager llm = new GridLayoutManager(this, 2);
        uinRecList.setLayoutManager(llm);

        initializeData();

        final FloatingActionMenu floatMenu = (FloatingActionMenu) findViewById(R.id.menu_labels_uinstagram);
        floatMenu.setClosedOnTouchOutside(true);

        FloatingActionButton uinMenuFilterOne = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.uin_menufilter1);
        FloatingActionButton uinMenuFilterTwo = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.uin_menufilter2);
        intent = new Intent(MainActivity.this, ImageFilter.class);

        uinMenuFilterOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOptionMenu();
                intent.putExtra("filter", "filter1");
                floatMenu.close(true);
            }
        });

        uinMenuFilterTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOptionMenu();
                intent.putExtra("filter", "filter2");
                floatMenu.close(true);
            }
        });
    }

    private void initializeData(){

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Snackbar.make(reqCoorlayout, "SDCard Tidak ditemukan", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Uinstagram");
            file.mkdirs();
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                FilesItem filesItem = new FilesItem();
                filesItem.setFilePath(listFile[i].getAbsolutePath());
                filesItem.setFileName(listFile[i].getName());
                imageList.add(filesItem);
            }
        }

        ImageInstaAdapter filesAdapter = new ImageInstaAdapter(imageList, this, this);
        uinRecList.setAdapter(filesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageList.clear();
        initializeData();
    }

    public void doClear(){
        imageList.clear();
        initializeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Uri selectedImage;

        try {
            if (requestCode == StaticClass.RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                if (currentapiVersion >= Build.VERSION_CODES.KITKAT) {
                    selectedImage = data.getData();
                    imgDecodableString = getRealPathFromURI_API19(selectedImage);
                    Log.e("image location", imgDecodableString);

                    intent.putExtra(StaticClass.GET_IMAGE_MESSAGE, imgDecodableString);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

                } else {
                    selectedImage = data.getData();
                    imgDecodableString = getRealPathFromURI_API11to18(this, selectedImage);

                    intent.putExtra(StaticClass.GET_IMAGE_MESSAGE, imgDecodableString);
                    startActivity(intent);

                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            } else if (requestCode == StaticClass.RESULT_LOAD_IMG_CAMERA && null != data) {

                if (resultCode == RESULT_OK) {

                    if (currentapiVersion >= Build.VERSION_CODES.KITKAT) {
                        selectedImage = data.getData();
                        imgDecodableString = getRealPathFromURI_API19(selectedImage);
                        Log.e("image location", imgDecodableString);

                        intent.putExtra(StaticClass.GET_IMAGE_MESSAGE, imgDecodableString);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

                    } else {
                        selectedImage = data.getData();
                        imgDecodableString = getRealPathFromURI_API11to18(this, selectedImage);
                        Log.e("image location", imgDecodableString);

                        intent.putExtra(StaticClass.GET_IMAGE_MESSAGE, imgDecodableString);
                        startActivity(intent);

                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(reqCoorlayout, getResources().getString(R.string.cancel_take), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(reqCoorlayout, getResources().getString(R.string.cancel_take), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                Snackbar.make(reqCoorlayout, getResources().getString(R.string.error_image), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (Exception e) {
            Log.e("error get image", e.toString());
            Snackbar.make(reqCoorlayout, getResources().getString(R.string.something_wrong), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public void chooseOptionMenu() {
        Holder holder = new ListHolder();
        GetImageAdapter adapter = new GetImageAdapter(this);

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                if (position == 1) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, StaticClass.RESULT_LOAD_IMG);
                    dialog.dismiss();
                } else if (position == 2) {

                    String fileName = "IMG_UINSTAGRAM.jpg";
                    ContentValues values = new ContentValues();

                    values.put(MediaStore.Images.Media.TITLE, fileName);

                    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(cameraIntent, StaticClass.RESULT_LOAD_IMG_CAMERA);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Snackbar.make(reqCoorlayout, "Temporary not available", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        };

        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setHeader(R.layout.uin_dialog_header)
                .setGravity(Gravity.CENTER)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setInAnimation(R.anim.anim_slide_in_up)
                .setOutAnimation(R.anim.anim_slide_out_up)
                .setCancelable(true)
                .create();
        View view = dialog.getHolderView();
        TextView textView = (TextView) view.findViewById(R.id.dialog_header_tittle);
        textView.setText("Pilih Gambar Dari");
        dialog.show();
    }
}
