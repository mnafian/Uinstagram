package net.mnafian.uinstagram.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.mnafian.uinstagram.Adapter.FilterAdapterDrawable;
import net.mnafian.uinstagram.R;
import net.mnafian.uinstagram.Utilities.RecyclerItemClickListener;
import net.mnafian.uinstagram.Utilities.StaticClass;
import net.mnafian.uinstagram.Activity.ViewResultActivity;


public class LayerDrawableFragment extends Fragment {
    private ImageView testimage;
    private RecyclerView recList;
    public static String pathImage;
    private Drawable drawableTemp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imf_layer_drawable, container, false);
        testimage = (ImageView) view.findViewById(R.id.testimage);

        Bitmap myBitmap = BitmapFactory.decodeFile(pathImage);
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = myBitmap.getWidth();
        int inHeight = myBitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap unRotated = Bitmap.createScaledBitmap(myBitmap, outWidth, outHeight, true);

        try {
            ExifInterface exif = new ExifInterface(pathImage);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            unRotated = Bitmap.createBitmap(unRotated, 0, 0, unRotated.getWidth(), unRotated.getHeight(), matrix, true); // rotating bitmap
        }
        catch (Exception e) {

        }

        Bitmap bitmap = unRotated;
        final Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        updateImage(0, drawable);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recList = (RecyclerView) view.findViewById(R.id.rc_filter);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(layoutManager);

        FilterAdapterDrawable filterAdapter = new FilterAdapterDrawable(getActivity());
        recList.setAdapter(filterAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                updateImage(position, drawable);
            }
        }));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateImage(int position, Drawable drawable){
        Resources r = getResources();
        Drawable[] layers = new Drawable[2];
        layers[0] = drawable;
        switch (position) {
            case 0:
                layers[1] = r.getDrawable(R.drawable.wall1box);
                layers[1].setAlpha(90);
                break;

            case 1:
                layers[1] = r.getDrawable(R.drawable.wall2box);
                layers[1].setAlpha(90);
                break;

            case 2:
                layers[1] = r.getDrawable(R.drawable.wall3box);
                layers[1].setAlpha(90);
                break;

            case 3:
                layers[1] = r.getDrawable(R.drawable.wall4box);
                layers[1].setAlpha(90);
                break;

            case 4:
                layers[1] = r.getDrawable(R.drawable.wall5box);
                layers[1].setAlpha(90);
                break;

            case 5:
                layers[1] = r.getDrawable(R.drawable.wall6box);
                layers[1].setAlpha(90);
                break;

            case 6:
                layers[1] = r.getDrawable(R.drawable.wall7box);
                layers[1].setAlpha(90);
                break;

            case 7:
                layers[1] = r.getDrawable(R.drawable.wall8box);
                layers[1].setAlpha(90);
                break;

            case 8:
                layers[1] = r.getDrawable(R.drawable.wall9box);
                layers[1].setAlpha(90);
                break;

            case 9:
                layers[1] = r.getDrawable(R.drawable.wall10box);
                layers[1].setAlpha(90);
                break;

            case 10:
                layers[1] = r.getDrawable(R.drawable.wall11box);
                layers[1].setAlpha(90);
                break;

            case 11:
                layers[1] = r.getDrawable(R.drawable.wall12box);
                layers[1].setAlpha(90);
                break;
        }
        android.graphics.drawable.LayerDrawable layerDrawable = new android.graphics.drawable.LayerDrawable(layers);
        testimage.setImageDrawable(layerDrawable);
        testimage.destroyDrawingCache();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){
            Bitmap saveFinish = StaticClass.viewToBitmap(testimage);

            if (saveFinish != null) {
                Integer scaleWidth = saveFinish.getWidth();
                Integer scaleHeight = saveFinish.getHeight();

                Bitmap out = Bitmap.createScaledBitmap(saveFinish, scaleWidth, scaleHeight, false);
                final String fileName = StaticClass.SaveImage(out);

                final ProgressDialog pShow;
                pShow = new ProgressDialog(getActivity());
                pShow.setMessage("Memproses Gambar");
                pShow.show();

                pShow.setCancelable(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pShow.dismiss();
                        Intent intent = new Intent(getActivity(), ViewResultActivity.class);
                        intent.putExtra(StaticClass.GET_IMAGE_MESSAGE, fileName);
                        startActivity(intent);
                    }
                }, 1500);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Fragment newInstance(String path) {
        LayerDrawableFragment layerDrawableFragment = new LayerDrawableFragment();
        pathImage = path;
        return layerDrawableFragment;
    }
}
