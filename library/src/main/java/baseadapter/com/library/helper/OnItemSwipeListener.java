package baseadapter.com.library.helper;

import android.support.v7.widget.RecyclerView;

public interface OnItemSwipeListener {
    void onItemSwipeStart(int listPosition);

    void onItemSwipeing(RecyclerView.ViewHolder viewHolder, int listPosition);
}
