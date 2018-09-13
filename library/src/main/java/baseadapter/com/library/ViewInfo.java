package baseadapter.com.library;

import android.view.View;

public class ViewInfo {
    /**
     * viewType从-100开始，所以adapter中的viewType不能小于-100
     */
    public static int VIEWTYPE = -100;

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

    /**
     * 每次获取 VIEWTYPE 后减一 防止重复
     */
    public static int getVIEWTYPE() {
        if (VIEWTYPE < -9999) {
            VIEWTYPE = -100;
        }
        return VIEWTYPE--;
    }
}
