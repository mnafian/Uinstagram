package net.mnafian.uinstagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mnafian.uinstagram.R;

/**
 * Created by mnafian on 6/10/15.
 */
public class GetImageAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    public GetImageAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.uin_dialog_choose_imagesource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            viewHolder.viewList = (LinearLayout) view.findViewById(R.id.viewListOption);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.textView.setText("Foto Galeri");
                viewHolder.imageView.setImageResource(R.drawable.ic_takephoto);
                viewHolder.viewList.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                break;
            case 1:
                viewHolder.textView.setText("Foto Kamera");
                viewHolder.imageView.setImageResource(R.drawable.ic_camera);
                viewHolder.viewList.setBackgroundColor(context.getResources().getColor(R.color.accentColor));
                break;
            case 2:
                viewHolder.textView.setText("Foto Facebook");
                viewHolder.imageView.setImageResource(R.drawable.ic_facebook);
                viewHolder.viewList.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                break;
            case 3:
                viewHolder.textView.setText("Foto Instagram");
                viewHolder.imageView.setImageResource(R.drawable.ic_instagram);
                viewHolder.viewList.setBackgroundColor(context.getResources().getColor(R.color.accentColor));
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout viewList;
    }
}
