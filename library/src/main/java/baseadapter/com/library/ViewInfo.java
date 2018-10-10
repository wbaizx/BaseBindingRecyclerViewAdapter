package baseadapter.com.library;

import android.view.View;

public class ViewInfo {
    /**
     * 保存HeaderView或FooterView
     */
    private View view;

    /**
     * 保存HeaderView或FooterView对应的viewType。
     */
    private int itemViewType;

    public ViewInfo(View view, int itemViewType) {
        this.view = view;
        this.itemViewType = itemViewType;
    }

    public View getView() {
        return view;
    }

    public int getItemViewType() {
        return itemViewType;
    }
}
