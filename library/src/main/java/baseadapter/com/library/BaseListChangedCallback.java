package baseadapter.com.library;

import android.databinding.ObservableArrayList;

/**
 * 计算时需要考虑头部数量 adapter.getHeadersCount()
 */
public class BaseListChangedCallback<T> extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<T>> {
    private BaseBindingRecyclerViewAdapter adapter;
    private boolean isEnable = false;

    public BaseListChangedCallback(BaseBindingRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged(ObservableArrayList<T> sender) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        //isEnable 为控制标志，在拖拽监听器onMove中会调用Collections.swap调换item位置，其内部实际是调用list的set方法
        //set方法会触发当前回调，然而实际onMove中需要调用是notifyItemMoved，所以此处根据标记禁止notifyItemRangeChanged
        //并在需要的地方手动调用notifyItemMoved或其他方法
        //注意在ItemTouchHelperCallback中处理好状态恢复
        if (!isEnable) {
            adapter.notifyItemRangeChanged(positionStart + adapter.getHeadersCount(), itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        adapter.notifyItemRangeInserted(positionStart + adapter.getHeadersCount(), itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableArrayList<T> sender, int fromPosition, int toPosition, int itemCount) {
        if (itemCount == 1) {
            adapter.notifyItemMoved(fromPosition + adapter.getHeadersCount(), toPosition);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart + adapter.getHeadersCount(), itemCount);
    }

    public void enableChangedCallback(boolean isEnable) {
        this.isEnable = isEnable;
    }
}
