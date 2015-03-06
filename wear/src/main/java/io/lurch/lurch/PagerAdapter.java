package io.lurch.lurch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * Created by jens on 15-03-06.
 */
public class PagerAdapter extends GridPagerAdapter {

    private String[][] pages = new String[1][2];
    private Context context;

    public PagerAdapter(Context context) {
        pages[0][0] = "test";
        pages[0][1] = "test2";

        this.context = context;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public int getCurrentColumnForRow(int row, int currentColumn) {
        return currentColumn;
    }

    @Override
    protected Object instantiateItem(ViewGroup viewGroup, int i, int i2) {
        /*TextView textView = new TextView(context);
        textView.setText(pages[i][i2]);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setGravity(Gravity.CENTER);
        viewGroup.addView(textView);*/

        ImageView imageView = new ImageView(context);
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), R.raw.android);
        imageView.setImageDrawable(svg.createPictureDrawable());

        /*LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(50, 50);
        layoutParams.gravity=Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);*/

        viewGroup.addView(imageView);

        return imageView;

        /*textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(":DDD", "CLICKED THE MF TEXT!!");
            }
        });*/

        //return textView;
    }

    @Override
    protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {
        viewGroup.removeView((View) o);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }
}
