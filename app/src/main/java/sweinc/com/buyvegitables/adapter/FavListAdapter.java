package sweinc.com.buyvegitables.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.List;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.activity.Show_Details;
import sweinc.com.buyvegitables.model.FavItem;


public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.MyViewHolder> {
    private List<FavItem> cartList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView order_Title;
        private TextView cost;
        RelativeLayout viewBackground;
        RelativeLayout viewForeground;
        private ImageView index_image;

        public MyViewHolder(View view) {
            super(view);
            this.order_Title = view.findViewById(R.id.name);
            this.index_image = view.findViewById(R.id.index_image);
            this.cost = view.findViewById(R.id.cost);
            this.viewBackground = view.findViewById(R.id.view_background);
            this.viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public FavListAdapter(Context context, List<FavItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FavItem item = this.cartList.get(position);
        holder.order_Title.setText(item.getFavName());
        holder.cost.setText(item.getFavCost());
        Glide.with(context).load(item.getFavURL()).into(holder.index_image);

        holder.index_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChangeActivity(item.getFavName(), item.getFavURL(), item.getFavCost());
            }
        });
    }

    public int getItemCount() {
        return this.cartList.size();
    }

    public void removeItem(int position) {
        this.cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(FavItem item, int position) {
        this.cartList.add(position, item);
        notifyItemInserted(position);
    }


    private void ChangeActivity(String title, String url, String price) {
        Intent intent = new Intent(this.context, Show_Details.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        intent.putExtra("keyTitle", title);
        intent.putExtra("keyUrl", url);
        intent.putExtra("keyPrice", price);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
