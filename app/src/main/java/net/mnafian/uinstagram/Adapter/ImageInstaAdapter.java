package net.mnafian.uinstagram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import net.mnafian.uinstagram.Activity.MainActivity;
import net.mnafian.uinstagram.Model.FilesItem;
import net.mnafian.uinstagram.R;

import java.io.File;
import java.util.List;

/**
 * Created by mnafian on 29/10/15.
 */
public class ImageInstaAdapter extends RecyclerView.Adapter<ImageInstaAdapter.FilesHolder> {

    private List<FilesItem> filesList;
    private Context mContext;
    private MainActivity activity;

    public ImageInstaAdapter(List<FilesItem> filesList, Context mContext, MainActivity activity) {
        this.filesList = filesList;
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public FilesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.sm_item_layout_files, parent, false);
        return new FilesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FilesHolder holder, int position) {
        final FilesItem filesItem = filesList.get(position);

        final String file = filesItem.getFileName();
        String path = filesItem.getFilePath();
        holder._smFilesJudul.setText(file);

        Uri uri = Uri.fromFile(new File(path));
        Glide.with(mContext).load(uri).into(holder._smImageFiles);
        Glide.with(mContext).load(uri).into(holder._smImageDetail);

        holder._smShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mediaPath = filesItem.getFilePath();;
                String caption = "Share gambar kamu";

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");

                File media = new File(mediaPath);
                Uri uri = Uri.fromFile(media);

                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, caption);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(share);
            }
        });

        holder._smDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String root = Environment.getExternalStorageDirectory().toString();
                final File imageFile = new File(root + "/Uinstagram/" + file);
                if(imageFile.exists()){

                    new MaterialDialog.Builder(mContext)
                            .title("Konfirmasi")
                            .content("Anda yakin ingin menghapus gambar ini?")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    imageFile.delete();
                                    activity.doClear();
                                    dialog.dismiss();
                                    holder.dialog.dismiss();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    dialog.dismiss();
                                }
                            })
                            .positiveText("Ya")
                            .negativeText("Tidak")
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != filesList ? filesList.size() : 0);
    }

    public class FilesHolder extends RecyclerView.ViewHolder {

        TextView _smFilesJudul;
        ImageView _smImageFiles, _smImageDetail;
        ImageButton _smShare, _smDelete;
        View view;
        DialogPlus dialog;

        public FilesHolder(View itemView) {
            super(itemView);
            _smFilesJudul = (TextView) itemView.findViewById(R.id.sm_files_judul);
            _smImageFiles = (ImageView) itemView.findViewById(R.id.sm_img_thumbnail);

            dialog = DialogPlus.newDialog(mContext)
                    .setContentHolder(new ViewHolder(R.layout.sm_item_detail_image))
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setGravity(Gravity.BOTTOM)
                    .setCancelable(true)
                    .setExpanded(false)
                    .create();
            view = dialog.getHolderView();
            _smImageDetail = (ImageView) view.findViewById(R.id.uin_detailimage);
            _smShare = (ImageButton) view.findViewById(R.id.sm_btn_share);
            _smDelete = (ImageButton) view.findViewById(R.id.sm_btn_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }

    }
}
