package sweinc.com.buyvegitables.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import sweinc.com.buyvegitables.Config;
import sweinc.com.buyvegitables.HomeActivity;
import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.database.Profile_Database;


public class AddProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextMail, editTextName, editTextMob, editTextUPI, editTextAddress;
    private EditText editTextMap;
    Button buttonSend;
    boolean connected = false;
    AlertDialog.Builder builder;

    Profile_Database database;

    static String name = "", mail = "", mob = "", map = "", address = "", upi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextMail = findViewById(R.id.editTextMail);
        editTextMob = findViewById(R.id.editTextMob);
        editTextMap = findViewById(R.id.editTextMap);
        editTextUPI = findViewById(R.id.editTextUPI);
        editTextAddress = findViewById(R.id.editTextAddress);

        database = new Profile_Database(getApplicationContext());
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(this);
        builder = new AlertDialog.Builder(this);

    }


    private void addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this, "Profile Updating..", "Please wait");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile Updated " + response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                parmas.put("action", "addItem");
                parmas.put("name", name);
                parmas.put("mail", mail);
                parmas.put("mob", mob);
                parmas.put("map", map);
                parmas.put("address", address);
                parmas.put("upi", upi);

                return parmas;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }


    @Override
    public void onClick(View v) {

        if (v == buttonSend) {
            name = editTextName.getText().toString().trim();
            mail = editTextMail.getText().toString().trim();
            mob = editTextMob.getText().toString().trim();
            map = editTextMap.getText().toString().trim();
            address = editTextAddress.getText().toString().trim();
            upi = editTextUPI.getText().toString().trim();

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

            Log.e("Value: ", name + " " + mob.length() + " " + address + " " + upi + " " + map + " ");

            if (address.equals("")) {
                editTextAddress.setError("Field should not be blank");
            }
            if (mail.equals("")) {
                editTextMail.setError("Field should not be blank");
            }
            if (mob.equals("")) {
                editTextMob.setError("Field should not be blank");
            }
            if (mail.equals("")) {
                editTextMail.setError("Field should not be blank");
            }
            if (name.equals("")) {
                editTextName.setError("Field should not be blank");
            }
            if (upi.equals("")) {
                editTextUPI.setError("Field should not be blank");
            } else {
                if (mob.length() < 9) {
                    editTextMob.setError("Please check number");
                    Toast.makeText(getApplicationContext(), "Invalid Mobile Number..", Toast.LENGTH_LONG).show();
                } else {
                    if (!connected) {
                        builder.setMessage("Please check your network").setTitle("Please check your network");

                        builder.setMessage("Please Switch On your your internet/wifi")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();

                        alert.setTitle("Please check your network");
                        alert.show();
                    } else {
                        if (Profile_Database.validate(name) != 0) {
                            if (Long.valueOf(AddProfile.this.database.update(name, mail, mob, address, upi, map)).longValue() == -1) {
                                Log.e("Call:","Not Updated");
                                Toast.makeText(AddProfile.this.getApplicationContext(), Show_Details.recipe_title + " : Not Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Call:","Updated");
                                Toast.makeText(AddProfile.this.getApplicationContext(), Show_Details.recipe_title + " Value Updated", Toast.LENGTH_SHORT).show();
                            }
                        } else if (Long.valueOf(AddProfile.this.database.create(name, mail, mob, address, upi, map)).longValue() == -1) {
                            Log.e("Call:","Added");
                            Toast.makeText(AddProfile.this.getApplicationContext(), "Profile Not Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Call:","Not Added");
                            Toast.makeText(AddProfile.this.getApplicationContext(), "Profile Added", Toast.LENGTH_SHORT).show();
                        }
                        addItemToSheet();
                    }
                }
            }


        }
    }

}
