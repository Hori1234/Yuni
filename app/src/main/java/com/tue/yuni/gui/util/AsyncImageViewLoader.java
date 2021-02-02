package com.tue.yuni.gui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.tue.yuni.R;
import com.tue.yuni.models.MenuItem;

/**
 * To Be Improved upon, potentially handle image downloading.
 */
public class AsyncImageViewLoader extends AsyncTask<Void, Void, Bitmap> {
    private Context ctx;
    private MenuItem menuItem;
    private ImageView imageView;
    private String path;

    public AsyncImageViewLoader(Context ctx, MenuItem menuItem, ImageView imageView) {
        this.ctx = ctx;
        this.menuItem = menuItem;
        this.imageView = imageView;
        this.path = imageView.getTag().toString();
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.hot_chocolate); // ToDo
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (!imageView.getTag().toString().equals(path)) return;
        imageView.setImageBitmap(bitmap);
    }
}
