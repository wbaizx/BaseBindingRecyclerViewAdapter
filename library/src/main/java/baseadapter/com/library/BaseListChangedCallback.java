package baseadapter.com.library;

import android.databinding.ObservableArrayList;

public class BaseListChangedCallback<T> extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<T>> {
    private BaseBindingRecyclerViewAdapter adapter;

    public BaseListChangedCallback(BaseBindingRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged(ObservableArrayList<T> sender) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart + adapter.getHeaderSCount(), itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        adapter.notifyItemRangeInserted(positionStart + adapter.getHeaderSCount(), itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableArrayList<T> sender, int fromPosition, int toPosition, int itemCount) {
        if (itemCount == 1) {
            adapter.notifyItemMoved(fromPosition + adapter.getHeaderSCount(), toPosition);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableArrayList<T> sender, int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart + adapter.getHeaderSCount(), itemCount);
    }
}
