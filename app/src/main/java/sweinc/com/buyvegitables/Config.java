package sweinc.com.buyvegitables;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Random;

public class Config {

    public static final String URL = "https://script.google.com/macros/s/AKfycbyVD-7wLhZTItcZyINYW3FP3keMkAwyv4hCgEQBBRo3t82whnim/exec";
    public static final String[] slogan = {
            "Accepting your own mortality is like eating your vegetables: You may not want to do it, but it's good for you.",
            "Vegetable, Herbs and Spices, you can combine those ingredient, that would be the best dish you'd ever cook",
            "You have 2 Homes: Earth and Your Body Take care of them",
            "Healthy Outside Starts from Inside",
            "A dealer in onions is a good judge of scallions.",
            "Vegetable gardening might be considered one of the great conservative rituals.",
            "By eating many fruits and vegetables in place of fast food and junk food, people could avoid obesity."
    };



    public static class GetRandom{

        public static String getSlogan(){
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

}
