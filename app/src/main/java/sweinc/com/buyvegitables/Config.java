package sweinc.com.buyvegitables;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Random;

import sweinc.com.buyvegitables.activity.CartBill;
import sweinc.com.buyvegitables.activity.ProductList;

public class Config {

    public static final String URL = "https://script.google.com/macros/s/AKfycbyVD-7wLhZTItcZyINYW3FP3keMkAwyv4hCgEQBBRo3t82whnim/exec";
    private static final String[] slogan = {
            "Accepting your own mortality is like eating your vegetables: You may not want to do it, but it's good for you.",
            "Vegetable, Herbs and Spices, you can combine those ingredient, that would be the best dish you'd ever cook",
            "You have 2 Homes: Earth and Your Body Take care of them",
            "Healthy Outside Starts from Inside",
            "A dealer in onions is a good judge of scallions.",
            "Vegetable gardening might be considered one of the great conservative rituals.",
            "By eating many fruits and vegetables in place of fast food and junk food, people could avoid obesity."
    };

    public static final String productURL = "https://script.google.com/macros/s/AKfycbyVD-7wLhZTItcZyINYW3FP3keMkAwyv4hCgEQBBRo3t82whnim/exec?action=getItems";
    public static final String headerImage = "https://res.cloudinary.com/dlnt9gjfd/image/upload/v1589010745/header_es48sb.jpg";

    public static final String UPIName = "Sagar Hande";
    public static final String AppUPI = "8722548007@upi";
    public static final String AppNumber = "9008403222";


    public static class GetRandom {

        public static String getSlogan() {
            Random rand = new Random();
            int randomNum = rand.nextInt(slogan.length);

            return slogan[randomNum];
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % this.spanCount;
            if (this.includeEdge) {
                outRect.left = this.spacing - ((this.spacing * column) / this.spanCount);
                outRect.right = ((column + 1) * this.spacing) / this.spanCount;
                if (position < this.spanCount) {
                    outRect.top = this.spacing;
                }
                outRect.bottom = this.spacing;
                return;
            }
            outRect.left = (this.spacing * column) / this.spanCount;
            outRect.right = this.spacing - (((column + 1) * this.spacing) / this.spanCount);
            if (position >= this.spanCount) {
                outRect.top = this.spacing;
            }
        }
    }

    public static class BuildAlert {
        public static void buildAlert(Context context, String msg, String title) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setTitle(title)
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
        }
    }

    public static class BuildIntent {

        public static void buildIntent(Context context, Class classFile, String title) {
            Intent intent = new Intent(context, classFile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("keyTitle", title);
            context.startActivity(intent);
        }

    }

}
