package net.mnafian.uinstagram.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import net.mnafian.uinstagram.Fragment.EffectsFilterFragment;
import net.mnafian.uinstagram.Fragment.LayerDrawableFragment;
import net.mnafian.uinstagram.R;
import net.mnafian.uinstagram.Utilities.StaticClass;

/**
 * Created by mnafian on 29/10/15.
 */
public class ImageFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imf_mainmenu_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tipe = extras.getString("filter");
            String path = extras.getString(StaticClass.GET_IMAGE_MESSAGE);

            assert tipe != null;
            if (tipe.equals("filter1")){
                changeFragment(EffectsFilterFragment.newInstance(path));
            } else {
                changeFragment(LayerDrawableFragment.newInstance(path));
            }
        }
    }


    public void changeFragment(Fragment targetFragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, targetFragment);
        transaction.commit();
    }
}
