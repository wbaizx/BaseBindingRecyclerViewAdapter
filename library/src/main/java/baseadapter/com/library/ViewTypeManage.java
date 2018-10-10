package baseadapter.com.library;

public class ViewTypeManage {
    /**
     * viewType从-100开始，所以adapter中的viewType不能小于-100
     */
    private static int VIEWTYPE = -100;

    /**
     * 每次获取 VIEWTYPE 后减一 防止重复
     */
    public synchronized static int getViewType() {
        if (VIEWTYPE < -19999) {
            VIEWTYPE = -100;
        }
        return VIEWTYPE--;
    }
}
