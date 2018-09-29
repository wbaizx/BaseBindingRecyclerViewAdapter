package baseadapter.com.library;

import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;

import baseadapter.com.library.helper.OnItemDragListener;
import baseadapter.com.library.helper.OnItemSwipeListener;

public abstract class BaseBindingDraggableAdapter<DB extends ViewDataBinding, T> extends BaseBindingRecyclerViewAdapter<DB, T> {
    /**
     * 拖拽监听器
     */
    private OnItemDragListener onItemDragListener;
    /**
     * 侧滑监听器
     */
    private OnItemSwipeListener onItemSwipeListener;
    /**
     * 拖拽开关
     */
    private boolean isDragEnabled = false;
    /**
     * 侧滑开关
     */
    private boolean isSwipeEnabled = false;

    /**
     * 复写构造以便子类能够复写调用
     */
    public BaseBindingDraggableAdapter() {
    }

    /**
     * 复写构造以便子类能够复写调用
     */
    public BaseBindingDraggableAdapter(ObservableArrayList<T> list) {
        super(list);
    }

    /**
     * 设置自动刷新监听器状态，主要针对拖拽item刷新问题
     */
    public void enableChangedCallback(boolean isEnable) {
        listChangedCallback.enableChangedCallback(isEnable);
    }

    public void onItemDragStart(int listPosition) {
        if (isDragEnabled && onItemDragListener != null) {
            onItemDragListener.onItemDragStart(listPosition);
        }
    }

    public void onItemDraging(int fromListPosition, int toListPosition) {
        if (fromListPosition < toListPosition) {
            for (int i = fromListPosition; i < toListPosition; i++) {
                Collections.swap(getList(), i, i + 1);
            }
        } else {
            for (int i = fromListPosition; i > toListPosition; i--) {
                Collections.swap(getList(), i, i - 1);
            }
        }
        notifyItemMoved(fromListPosition + getHeadersCount(), toListPosition + getHeadersCount());
        if (isDragEnabled && onItemDragListener != null) {
            onItemDragListener.onItemDraging(fromListPosition, toListPosition);
        }
    }

    public void onItemDragEnd(int listPosition) {
        if (isDragEnabled && onItemDragListener != null) {
            onItemDragListener.onItemDragEnd(listPosition);
        }
    }

    public void onItemSwipeStart(int listPosition) {
        if (isSwipeEnabled && onItemSwipeListener != null) {
            onItemSwipeListener.onItemSwipeStart(listPosition);
        }
    }

    public void onItemSwipeing(RecyclerView.ViewHolder viewHolder, int listPosition) {
        if (isSwipeEnabled && onItemSwipeListener != null) {
            onItemSwipeListener.onItemSwipeing(viewHolder, listPosition);
        }
    }

    /**
     * 侧滑但未删除，需要恢复item状态
     */
    public void recoverItem(RecyclerView.ViewHolder viewHolder, int listPosition) {
        viewHolder.itemView.scrollTo(0, 0);
        viewHolder.itemView.setAlpha(1.0f);
        notifyItemChanged(listPosition + getHeadersCount());
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }

    public void setOnItemSwipeListener(OnItemSwipeListener onItemSwipeListener) {
        this.onItemSwipeListener = onItemSwipeListener;
    }

    public boolean getDragEnabled() {
        return isDragEnabled;
    }

    public boolean SwipeEnabled() {
        return isSwipeEnabled;
    }

    /**
     * 设置拖拽开关
     */
    public void setDragEnabled(boolean dragEnabled) {
        isDragEnabled = dragEnabled;
    }

    /**
     * 设置侧滑开关
     */
    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }
}
