package baseadapter.com.library.helper;

public interface OnItemDragListener {
    void onItemDragStart(int listPosition);

    void onItemDraging(int fromListPosition, int toListPosition);

    void onItemDragEnd(int listPosition);
}
