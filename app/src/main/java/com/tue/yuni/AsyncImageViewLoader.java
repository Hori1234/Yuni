package com.tue.yuni;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * To Be Improved upon, potentially handle image downloading.
 */
public class AsyncImageViewLoader extends AsyncTask<Void, Void, Bitmap> {
    private Context ctx;
    private Product product;
    private ImageView imageView;
    private String path;

    public AsyncImageViewLoader(Context ctx, Product product, ImageView imageView) {
        this.ctx = ctx;
        this.product = product;
        this.imageView = imageView;
        this.path = imageView.getTag().toString();
    }

    public Product getProduct() {
        return product;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), product.Picture);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (!imageView.getTag().toString().equals(path)) return;
        imageView.setImageBitmap(bitmap);
    }
}
