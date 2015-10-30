package net.mnafian.uinstagram.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
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

import net.mnafian.uinstagram.Adapter.FilterAdapterFactory;
import net.mnafian.uinstagram.R;
import net.mnafian.uinstagram.Utilities.GLToolbox;
import net.mnafian.uinstagram.Utilities.RecyclerItemClickListener;
import net.mnafian.uinstagram.Utilities.StaticClass;
import net.mnafian.uinstagram.Utilities.TextureRenderer;
import net.mnafian.uinstagram.Activity.ViewResultActivity;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class EffectsFilterFragment extends Fragment implements GLSurfaceView.Renderer {

	private RecyclerView recList;
	int mCurrentEffect;
	private GLSurfaceView mEffectView;
	private int[] mTextures = new int[2];
	private EffectContext mEffectContext;
	private Effect mEffect;
	private TextureRenderer mTexRenderer = new TextureRenderer();
	private int mImageWidth;
	private int mImageHeight;
	private boolean mInitialized = false;
	private volatile boolean saveFrame;
	public static String pathImage;
	public Bitmap tempBitmap;

	public void setCurrentEffect(int effect) {
		mCurrentEffect = effect;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.imf_effect_factory, container, false);
		mEffectView = (GLSurfaceView) rootView.findViewById(R.id.effectsview);
		mEffectView.setEGLContextClientVersion(2);
		mEffectView.setRenderer(this);
		mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mCurrentEffect = 0;

		LinearLayoutManager layoutManager
				= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

		recList = (RecyclerView) rootView.findViewById(R.id.rc_filter);
		recList.setHasFixedSize(true);
		recList.setLayoutManager(layoutManager);

		FilterAdapterFactory filterAdapter = new FilterAdapterFactory(getActivity());
		recList.setAdapter(filterAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setCurrentEffect(position);
                mEffectView.requestRender();
            }
        }));

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void loadTextures() {
		// Generate textures
		GLES20.glGenTextures(2, mTextures, 0);

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
		mImageWidth = bitmap.getWidth();
		mImageHeight = bitmap.getHeight();
		mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

		// Set texture parameters
		GLToolbox.initTexParams();
		bitmap.recycle();
	}

	private void initEffect() {
		EffectFactory effectFactory = mEffectContext.getFactory();
		if (mEffect != null) {
			mEffect.release();
		}
		/**
		 * Initialize the correct effect based on the selected menu/action item
		 */
		switch (mCurrentEffect) {

		case 0:
			break;

		case 1:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
			mEffect.setParameter("scale", 0.5f);
			break;

		case 2:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
			mEffect.setParameter("black", .1f);
			mEffect.setParameter("white", .7f);
			break;

		case 3:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
			mEffect.setParameter("brightness", 2.0f);
			break;

		case 4:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
			mEffect.setParameter("contrast", 1.4f);
			break;

		case 5:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
			break;

		case 6:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
			break;

		case 7:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
			mEffect.setParameter("first_color", Color.YELLOW);
			mEffect.setParameter("second_color", Color.DKGRAY);
			break;

		case 8:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
			mEffect.setParameter("strength", .8f);
			break;

		case 9:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
			mEffect.setParameter("scale", .5f);
			break;

		case 10:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("vertical", true);
			break;

		case 11:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("horizontal", true);
			break;

		case 12:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
			mEffect.setParameter("strength", 1.0f);
			break;

		case 13:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
			break;

		case 14:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
			break;

		case 15:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
			break;

		case 16:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_POSTERIZE);
			break;

		case 17:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
			mEffect.setParameter("angle", 180);
			break;

		case 18:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
			mEffect.setParameter("scale", .5f);
			break;

		case 19:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
			break;

		case 20:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
			break;

		case 21:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
			mEffect.setParameter("scale", .9f);
			break;

		case 22:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
			mEffect.setParameter("tint", Color.MAGENTA);
			break;

		case 23:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
			mEffect.setParameter("scale", .5f);
			break;

		default:
			break;

		}
	}

	private void applyEffect() {
		mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
	}

	private void renderResult() {
		if (mCurrentEffect != 0) {
			// if no effect is chosen, just render the original bitmap
			mTexRenderer.renderTexture(mTextures[1]);
		} else {
			saveFrame=true;
			// render the result of applyEffect()
			mTexRenderer.renderTexture(mTextures[0]);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (!mInitialized) {
			// Only need to do this once
			mEffectContext = EffectContext.createWithCurrentGlContext();
			mTexRenderer.init();
			loadTextures();
			mInitialized = true;
		}
		if (mCurrentEffect != 0) {
			// if an effect is chosen initialize it and apply it to the texture
			initEffect();
			applyEffect();
		}
		renderResult();
		tempBitmap = takeScreenshot(gl);
	}

	public Bitmap takeScreenshot(GL10 mGL) {
		final int mWidth = mEffectView.getWidth();
		final int mHeight = mEffectView.getHeight();
		IntBuffer ib = IntBuffer.allocate(mWidth * mHeight);
		IntBuffer ibt = IntBuffer.allocate(mWidth * mHeight);
		mGL.glReadPixels(0, 0, mWidth, mHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

		for (int i = 0; i < mHeight; i++) {
			for (int j = 0; j < mWidth; j++) {
				ibt.put((mHeight - i - 1) * mWidth + j, ib.get(i * mWidth + j));
			}
		}

		Bitmap mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		mBitmap.copyPixelsFromBuffer(ibt);
		return mBitmap;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (mTexRenderer != null) {
			mTexRenderer.updateViewSize(width, height);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_filter, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_save){
			if (saveFrame) {
				final String fileName = StaticClass.SaveImage(tempBitmap);
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
		EffectsFilterFragment filterFragment = new EffectsFilterFragment();
		pathImage = path;
		return filterFragment;
	}
}
