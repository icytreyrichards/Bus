package adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Richard.Ezama on 08/06/2016.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();

        if (position %2!=0) {
            if (spanIndex == 1) {
                outRect.left = space;
            } else {
                outRect.right = space;
            }
            outRect.bottom = space * 15;

            //outRect.top=space * 20;
        }
        else
        {
            if (spanIndex == 1) {
                outRect.left = space;
            } else {
                outRect.right = space;
            }
            outRect.bottom = space;
        }
    }
}