package sweinc.com.buyvegitables.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import com.bumptech.glide.Glide;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.database.Cart_Database;
import sweinc.com.buyvegitables.database.Profile_Database;

public class CartBill extends AppCompatActivity {

    Cart_Database database;
    Profile_Database user_database;
    Cursor res, user;
    private int countOrderedList = 0, totalCost = 0;
    static String name = "", upi = "", addr = "", mob = "";
    static StringBuilder itemDetails;
    String rupee = "₹";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_bill);

        TextView priceTotal = findViewById(R.id.priceTotal);
        TextView item = findViewById(R.id.item);
        TextView itemCost = findViewById(R.id.itemCost);
        TextView address = findViewById(R.id.address);
        Button placeOrder = findViewById(R.id.placeOrder);
        Button updateAddress = findViewById(R.id.updateAddress);

        ImageView addressImage = findViewById(R.id.addressImage);

        Glide.with(this).load("https://letterhub.com/wp-content/uploads/2018/02/location-1200x675.jpg").into(addressImage);

        TextView dottedLine1 = findViewById(R.id.dottedLine1);
        TextView dottedLine2 = findViewById(R.id.dottedLine2);
        TextView dottedLine3 = findViewById(R.id.dottedLine3);
        TextView dottedLine4 = findViewById(R.id.dottedLine4);

        String dottedLine = "  --------------------------------------------------------------------- ";
        dottedLine1.setText(dottedLine);
        dottedLine2.setText(dottedLine);
        dottedLine3.setText(dottedLine);
        dottedLine4.setText(dottedLine);

        database = new Cart_Database(getApplicationContext());
        user_database = new Profile_Database(getApplicationContext());
        StringBuilder itemList = new StringBuilder();
        StringBuilder itemCostList = new StringBuilder();
        itemDetails = new StringBuilder().append("Items Details: \n\n");

        res = this.database.read();
        while (res.moveToNext()) {
            this.countOrderedList++;
            String items = res.getString(res.getColumnIndex("name"));
            String cost = res.getString(res.getColumnIndex("cost"));
            String quantity = res.getString(res.getColumnIndex("quantity"));
            itemList = itemList.append(items).append(" (").append(quantity).append("kg)\n");
            itemCostList = itemCostList.append(rupee).append(cost).append("\n");
            itemDetails = itemDetails.append(items).append(" (").append(quantity).append("kg)").append("\t : ").append(rupee).append(cost).append("\n");
            totalCost += Double.valueOf(cost.trim());
        }



        itemList = itemList.append("\nTotal Items (" + countOrderedList + ")");
        itemDetails.append("\n").append("\nTotal Items (" + countOrderedList + ")").append("\nTotal Amount: ").append(rupee + totalCost);

        priceTotal.setText(rupee + totalCost);
        itemCost.setText(itemCostList);
        item.setText(itemList);

        user = user_database.read();
        while (user.moveToNext()) {
            name = user.getString(user.getColumnIndex("name"));
            upi = user.getString(user.getColumnIndex("upi"));
            addr = user.getString(user.getColumnIndex("address"));
            mob = user.getString(user.getColumnIndex("mobile"));
        }

        if (addr.equals("")) {
            addr = "Please Update Address...";
        } else if(mob.equals("")) {
            addr = "Please Update Mobile Number...";
        }

        final String addrDetails = "Delivery Details:\n\n" + name + "\n" + mob + "\n" + upi + "\n" + addr + "\n";
        address.setText(addrDetails);


        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addr.equals("") || addr.equals("Please Update Address...")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartBill.this);
                    builder.setMessage("Please Add Delivery Details..")
                            .setTitle("Address")
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
                } else {
                    Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
                    intent.putExtra("keyAmount", rupee + totalCost);
                    intent.putExtra("keyAddress", addrDetails);
                    intent.putExtra("keyItemDetails", itemDetails+"");
                    startActivity(intent);
                }

            }
        });


    }

}
