package sweinc.com.buyvegitables;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import java.util.List;

import sweinc.com.buyvegitables.activity.AddProfile;
import sweinc.com.buyvegitables.activity.CartPage;
import sweinc.com.buyvegitables.activity.FavoritePage;
import sweinc.com.buyvegitables.activity.OrderDetails;
import sweinc.com.buyvegitables.activity.ProductList;
import sweinc.com.buyvegitables.activity.Show_Web;
import sweinc.com.buyvegitables.adapter.ImageViewerAdapter;
import sweinc.com.buyvegitables.database.Cart_Database;
import sweinc.com.buyvegitables.model.ImageViewerModel;


public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    ViewPager viewPager;
    ImageViewerAdapter adapter;
    List<ImageViewerModel> models;

    ProgressDialog loading;
    Cursor res;
    Cart_Database cart_database;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        cart_database = new Cart_Database(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res = cart_database.read();
                if(!res.moveToNext()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setMessage("Your Cart is Empty....Add Products to it now")
                            .setTitle("Empty")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(-2).setBackgroundColor(-1);
                    alert.getButton(-2).setLeft(10);
                    alert.getButton(-1).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    alert.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    alert.getButton(-1).setBackgroundColor(-1);
                } else{
                    Intent intent = new Intent(getApplicationContext(), CartPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        models = new ArrayList<>();
        models.add(new ImageViewerModel(R.drawable.vegetable, "Vegetables"));
        models.add(new ImageViewerModel(R.drawable.fruits, "Fruits"));
        models.add(new ImageViewerModel(R.drawable.leaves, "Green Leaves"));
        models.add(new ImageViewerModel(R.drawable.sprouts, "Cuts & Sprouts"));

        adapter = new ImageViewerAdapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        ImageView imageView = findViewById(R.id.dietImage);
        ImageView viewAllImage = findViewById(R.id.viewAllImage);
        ImageView returnImage = findViewById(R.id.returnImage);
        ImageView profileImage = findViewById(R.id.profileImage);

        Glide.with(this).load("https://i0.wp.com/images-prod.healthline.com/hlcmsresource/images/AN_images/vegetarian-diet-plan-1296x728-feature.jpg").into(imageView);
        Glide.with(this).load("https://cdn.pixabay.com/photo/2017/04/05/10/05/salad-2204505_960_720.jpg").into(viewAllImage);
        Glide.with(this).load("https://cdn.shopify.com/s/files/1/0185/3988/files/Goodordering-returns.jpg").into(returnImage);
        Glide.with(this).load("https://cdn.wallpapersafari.com/9/35/6saAmD.jpg").into(profileImage);

        Button profileButton = findViewById(R.id.profileButton);
        Button viewAllButton = findViewById(R.id.viewAllButton);
        Button dietButton = findViewById(R.id.dietButton);
        Button returnButton = findViewById(R.id.returnButton);

        profileButton.setOnClickListener(this);
        viewAllButton.setOnClickListener(this);
        dietButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/email");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"srideviff@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.setPackage("com.google.android.gm");
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(Intent.createChooser(intent, "Send Feedback:"));
        } else if (id == R.id.nav_cart) {
            res = cart_database.read();
            if(!res.moveToNext()){
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Your Cart is Empty....Add Products to it now")
                        .setTitle("Empty")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(-2).setBackgroundColor(-1);
                alert.getButton(-2).setLeft(10);
                alert.getButton(-1).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                alert.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                alert.getButton(-1).setBackgroundColor(-1);
            } else{
                Intent intent = new Intent(getApplicationContext(), CartPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (id == R.id.nav_fruits) {
            Intent intent = new Intent(getApplicationContext(), ProductList.class);
            intent.putExtra("keyTitle", "Fruits");
            startActivity(intent);
        } else if (id == R.id.nav_vegetable) {
            Intent intent = new Intent(getApplicationContext(), ProductList.class);
            intent.putExtra("keyTitle", "Vegetables");
            startActivity(intent);
        } else if (id == R.id.nav_addProfile) {
            Intent intent = new Intent(getApplicationContext(), AddProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(getApplicationContext(), FavoritePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(getApplicationContext(), OrderDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profileButton:
                intent = new Intent(getApplicationContext(), AddProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            break;
            case R.id.viewAllButton:
                intent = new Intent(getApplicationContext(), ProductList.class);
                intent.putExtra("keyTitle", "All");
                startActivity(intent);
                break;
            case R.id.dietButton:
                intent = new Intent(getApplicationContext(), ProductList.class);
                intent.putExtra("keyTitle", "All");
                startActivity(intent);
                break;
            case R.id.returnButton:
                Intent intent = new Intent(getApplicationContext(), Show_Web.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("keyTitle", "Return Policy");
                intent.putExtra("keyFile", "file:///android_asset/Return_Policy.html");
                startActivity(intent);
                break;

        }
        }
}


