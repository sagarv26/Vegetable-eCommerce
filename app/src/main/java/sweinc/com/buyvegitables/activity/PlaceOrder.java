package sweinc.com.buyvegitables.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sweinc.com.buyvegitables.Config;
import sweinc.com.buyvegitables.HomeActivity;
import sweinc.com.buyvegitables.Manifest;
import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.database.Cart_Database;
import sweinc.com.buyvegitables.database.Order_Database;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener {

    final int UPI_PAYMENT = 0;
    String totalAmount = "", addrDetails = "", itemDetails = "";
    boolean connected = false;

    Button codPayment;
    Cart_Database db;
    Order_Database orderDB;
    private final static int SEND_SMS_PERMISSION_REQ = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order);

        db = new Cart_Database(getApplicationContext());
        orderDB = new Order_Database(getApplicationContext());

        codPayment = findViewById(R.id.codPayment);
        Button upiPayment = findViewById(R.id.upiPayment);
        TextView priceTotal = findViewById(R.id.priceTotal);

        Intent intent = getIntent();

        totalAmount = String.valueOf(intent.getStringExtra("keyAmount"));
        priceTotal.setText(totalAmount);

        addrDetails = String.valueOf(intent.getStringExtra("keyAddress"));
        itemDetails = String.valueOf(intent.getStringExtra("keyItemDetails"));

        if (checkPermission(android.Manifest.permission.SEND_SMS)) {
            upiPayment.setEnabled(true);
            codPayment.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        }

        upiPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                payUsingUpi(Config.UPIName, Config.AppUPI,
                        getString(R.string.app_name) + " Payment", totalAmount);
            }
        });

        codPayment.setOnClickListener(this);

    }

    private boolean checkPermission(String sendSms) {
        int checkpermission = ContextCompat.checkSelfPermission(this, sendSms);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case SEND_SMS_PERMISSION_REQ:
                if(grantResults.length>0 &&(grantResults[0]==PackageManager.PERMISSION_GRANTED))
                {
                    codPayment.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if (v == codPayment) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

            if (!connected) {
                Config.BuildAlert.buildAlert(PlaceOrder.this, "Please Switch On your your internet/wifi", "Please check your network");
            } else {
                addItemToSheet();
            }

        }
    }

    void payUsingUpi(String name, String upiId, String note, String amount) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PlaceOrder.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getApplicationContext())) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                addItemToSheet();
                Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successful: " + approvalRefNo);
                orderDB.create(addrDetails + "\n" + itemDetails);
                if(checkPermission(android.Manifest.permission.SEND_SMS))
                {
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(Config.AppNumber,null,addrDetails + "\n" + itemDetails,null,null);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }

                db.deleteAll();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getApplicationContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(getApplicationContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(getApplicationContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    private void addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this, "Order Placing..", "Please wait");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        orderDB.create(addrDetails + "\n" + itemDetails);
                        if(checkPermission(android.Manifest.permission.SEND_SMS))
                        {
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(Config.AppNumber,null,addrDetails + "\n" + itemDetails,null,null);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                        db.deleteAll();
                        Toast.makeText(getApplicationContext(), "Order Placed " + response, Toast.LENGTH_LONG).show();
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

                parmas.put("action", "addDetails");
                parmas.put("user", addrDetails);
                parmas.put("itemdetails", itemDetails);


                return parmas;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }

    protected void onStart() {
        super.onStart();
        orderDB.openDB();
    }

    protected void onStop() {
        super.onStop();
        orderDB.closeDB();
    }

}


