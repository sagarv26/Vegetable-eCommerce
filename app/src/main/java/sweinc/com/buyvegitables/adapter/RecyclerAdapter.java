package sweinc.com.buyvegitables.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.activity.Show_Details;
import sweinc.com.buyvegitables.model.RecyclerItem;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {

    private List<RecyclerItem> listItem;
    private Context mContext;

    public class viewHolder extends RecyclerView.ViewHolder {
        public ImageView index_image;
        public TextView index_name, index_price;

        viewHolder(View itemView) {
            super(itemView);
            index_name = itemView.findViewById(R.id.index_name);
            index_price = itemView.findViewById(R.id.index_price);
            index_image = itemView.findViewById(R.id.index_image);

        }
    }

    public RecyclerAdapter(List<RecyclerItem> listItem, Context mContext) {
        this.listItem = listItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_view, parent, false);
        return new viewHolder(v);
    }

    public void onBindViewHolder(viewHolder holder, int position) {
        final RecyclerItem item = this.listItem.get(position);
        holder.index_name.setText(item.getIndex_name());
        String rupee = "₹" + item.getIndex_price() + " per KG";
        holder.index_price.setText(rupee);
        Glide.with(mContext).load(item.getIndex_thumbnail()).into(holder.index_image);

//        new DownloadImageFromInternet(holder.index_image).execute(item.getIndex_thumbnail());

        holder.index_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChangeActivity(item.getIndex_name(), item.getIndex_thumbnail(), item.getIndex_price());
            }
        });

    }

    private void ChangeActivity(String title, String url, String price) {
        Intent intent = new Intent(this.mContext, Show_Details.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        intent.putExtra("keyTitle", title);
        intent.putExtra("keyUrl", url);
        intent.putExtra("keyPrice", price);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public int getItemCount() {
        return this.listItem.size();
    }

}
