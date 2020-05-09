package sweinc.com.buyvegitables.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import sweinc.com.buyvegitables.Config;
import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.adapter.RecyclerAdapter;
import sweinc.com.buyvegitables.model.RecyclerItem;

public class ProductList extends AppCompatActivity {

    ProgressDialog loading;
    public RecyclerView recyclerView;
    static List<RecyclerItem> listItem;
    ActionBar toolbar;
    ImageView titleImage;
    TextView title;
    RecyclerAdapter adapter;

    static String titleName = null;
    static String ex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        toolbar = getSupportActionBar();


        title = findViewById(R.id.title);
        titleImage = findViewById(R.id.titleImage);

        Intent intent = getIntent();

        titleName = String.valueOf(intent.getStringExtra("keyTitle"));

        if(titleName.equals("All")){
            title.setText("Vegetables and Fruits");
            toolbar.setTitle("Vegetables and Fruits");
        } else {
            title.setText(titleName);
            toolbar.setTitle(titleName);
        }

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Config.GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        getItems();

    }


    private void getItems() {

        loading = ProgressDialog.show(this,  "please wait", Config.GetRandom.getSlogan(), false, true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyVD-7wLhZTItcZyINYW3FP3keMkAwyv4hCgEQBBRo3t82whnim/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void parseItems(String jsonResposnce) {

        listItem = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String itemName = jo.getString("itemName");
                String url = jo.getString("url");
                String price = jo.getString("price");
                String type = jo.getString("type");

                if (type.equals(titleName)) {
                    listItem.add(new RecyclerItem(itemName, url, price));
                } else if (titleName.equals("All")){
                    listItem.add(new RecyclerItem(itemName, url, price));
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new RecyclerAdapter(listItem, getApplicationContext());
        recyclerView.setAdapter(adapter);

        loading.dismiss();
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics()));
    }
}
