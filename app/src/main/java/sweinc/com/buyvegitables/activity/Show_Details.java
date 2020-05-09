package sweinc.com.buyvegitables.activity;


import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.database.Cart_Database;
import sweinc.com.buyvegitables.database.Favorite_Database;

public class Show_Details extends AppCompatActivity {
    static String price = null;
    static String recipe_title = null;
    static String url;
    String rupee = "₹";

    Cart_Database cart_database;
    Favorite_Database fav_database;
    TextView input_quantity, quantityTag1;

    String[] quantityIn = {"kgs", "grams", "numbers"};

    static String quantityToAdd = "";
    static double quantity = 0;

    class AddCart implements View.OnClickListener {
        AddCart() {
        }

        public void onClick(View v) {
            cart_database = Show_Details.this.cart_database;
            if (input_quantity.getText().toString().matches("") || input_quantity.getText().toString().matches("0") || input_quantity.getText().toString().matches("0\\.0") || input_quantity.getText().toString().matches("Quantity") || !input_quantity.getText().toString().matches("\\d{0,3}\\.\\d{0,3}")) {
                Toast.makeText(Show_Details.this.getApplicationContext(), "Please provide required Quantity", Toast.LENGTH_SHORT).show();
            } else {
                if (Cart_Database.validate(Show_Details.recipe_title) != 0) {
                    if (Long.valueOf(Show_Details.this.cart_database.update(Show_Details.recipe_title, input_quantity.getText().toString().trim(), String.valueOf(parse(price.trim()) * parse(input_quantity.getText().toString().trim())))).longValue() == -1) {
                        Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " : Not Updated to Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " Value Updated to Cart", Toast.LENGTH_SHORT).show();
                    }
                } else if (Long.valueOf(Show_Details.this.cart_database.create(Show_Details.recipe_title, input_quantity.getText().toString().trim(), String.valueOf(parse(price.trim()) * parse(input_quantity.getText().toString().trim())))).longValue() == -1) {
                    Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " : Not Added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    double parse(String ratio) {

        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }

    }

    class AddWishlist implements View.OnClickListener {
        AddWishlist() {
        }

        public void onClick(View v) {
            fav_database = Show_Details.this.fav_database;
            if (Favorite_Database.validate(Show_Details.recipe_title) != 0) {
                if (Long.valueOf(fav_database.update(Show_Details.recipe_title, Show_Details.url, Show_Details.price)).longValue() == -1) {
                    Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " : Not Added to Wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + "Added to Wishlist", Toast.LENGTH_SHORT).show();
                }
            } else if (Long.valueOf(fav_database.create(Show_Details.recipe_title, Show_Details.url, Show_Details.price)).longValue() == -1) {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " : Not Added to Wishlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Show_Details.this.getApplicationContext(), Show_Details.recipe_title + " Added to Wishlist", Toast.LENGTH_SHORT).show();
            }


        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_product);


        input_quantity = findViewById(R.id.input_quantity);
        quantityTag1 = findViewById(R.id.quantityTag1);

        Spinner spin = findViewById(R.id.spinnerQuantity);

        spin.setPrompt("Quantity");
        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, quantityIn);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    quantityToAdd = "kgs";
                    quantity =0;
                    quantityTag1.setText(String.valueOf("Quantity in KGs"));
                }
                if (id == 1) {
                    quantityToAdd = "grams";
                    quantity =0;
                    quantityTag1.setText(String.valueOf("Quantity in grams"));
                }
                if (id == 2) {
                    quantityToAdd = "numbers";
                    quantity =0;
                    quantityTag1.setText(String.valueOf("Quantity in Numbers"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cart_database = new Cart_Database(getApplicationContext());
        fav_database = new Favorite_Database(getApplicationContext());

        ImageView image = findViewById(R.id.detail_image);
        TextView title = findViewById(R.id.title);
        TextView priceTag = findViewById(R.id.priceTag);

        Button add_cart = findViewById(R.id.add_cart);
        Button add_fav = findViewById(R.id.add_fav);

        Button addQuantity = findViewById(R.id.addQuantity);
        Button minusQuantity = findViewById(R.id.minusQuantity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityToAdd.equals("kgs")) {
                    quantity += 1;
                    input_quantity.setText(String.valueOf(quantity));
                }
                if (quantityToAdd.equals("grams")) {
                    quantity += 0.250;
                    input_quantity.setText(String.valueOf(quantity));
                }
                if (quantityToAdd.equals("numbers")) {
                    quantity += 1;
                    input_quantity.setText(String.valueOf(quantity));
                }
            }
        });

        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityToAdd.equals("kgs")) {
                    if(quantity > 0){
                        quantity -= 1;
                        input_quantity.setText(String.valueOf(quantity));
                    } else {
                        Toast.makeText(getApplicationContext(), "Reached Minimum", Toast.LENGTH_SHORT).show();
                    }

                }
                if (quantityToAdd.equals("grams")) {
                    if(quantity > 0){
                        quantity -= 0.250;
                        input_quantity.setText(String.valueOf(quantity));
                    } else {
                        Toast.makeText(getApplicationContext(), "Reached Minimum", Toast.LENGTH_SHORT).show();
                    }
                }
                if (quantityToAdd.equals("numbers")) {
                    if(quantity > 0){
                        quantity -= 1;
                        input_quantity.setText(String.valueOf(quantity));
                    } else {
                        Toast.makeText(getApplicationContext(), "Reached Minimum", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        Intent intent = getIntent();
        price = String.valueOf(intent.getStringExtra("keyPrice"));
        recipe_title = String.valueOf(intent.getStringExtra("keyTitle"));
        url = String.valueOf(intent.getStringExtra("keyUrl"));

        Glide.with(this).load(url).into(image);

//        new DownloadImageFromInternet(image).execute(url);
        priceTag.setText(rupee + price + " /KG");
        title.setText(recipe_title);
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle(recipe_title);

        add_cart.setOnClickListener(new AddCart());
        add_fav.setOnClickListener(new AddWishlist());

    }

    protected void onStart() {
        super.onStart();
        this.cart_database.openDB();
        this.fav_database.openDB();
    }

    protected void onStop() {
        super.onStop();
        this.cart_database.closeDB();
        this.fav_database.closeDB();
    }
}
