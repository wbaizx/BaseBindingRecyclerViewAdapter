package baseadapter.com.library.helper;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import baseadapter.com.library.BaseBindingDraggableAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private BaseBindingDraggableAdapter adapter;

    public ItemTouchHelperCallback(BaseBindingDraggableAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return adapter.getDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return adapter.SwipeEnabled();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isListItem(viewHolder.getAdapterPosition())) {
            //拖拽时设置自动刷新监听器状态
            adapter.enableChangedCallback(true);
            adapter.onItemDragStart(viewHolder.getAdapterPosition() - adapter.getHeadersCount());
            viewHolder.itemView.setTag("drag");

        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isListItem(viewHolder.getAdapterPosition())) {
            adapter.onItemSwipeStart(viewHolder.getAdapterPosition() - adapter.getHeadersCount());
            viewHolder.itemView.setTag("swipe");
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isListItem(viewHolder.getAdapterPosition())) {
            if (String.valueOf(viewHolder.itemView.getTag()).equals("drag")) {
                //拖拽结束透明度恢复
                viewHolder.itemView.setAlpha(1.0f);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                adapter.onItemDragEnd(viewHolder.getAdapterPosition() - adapter.getHeadersCount());
            }
            viewHolder.itemView.setTag("");
        }
        //设置自动刷新监听器状态
        adapter.enableChangedCallback(false);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (isListItem(viewHolder.getAdapterPosition())) {
            int dragFlag;
            int swipeFlag;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                swipeFlag = 0;
            } else {
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
                swipeFlag = ItemTouchHelper.LEFT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        } else {
            //不是列表内容（头和尾）不允许侧滑和拖拽
            return makeMovementFlags(0, 0);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder from, @NonNull RecyclerView.ViewHolder to) {
        int fromPosition = from.getAdapterPosition();
        int toPosition = to.getAdapterPosition();
        if (isListItem(fromPosition) && isListItem(toPosition)) {
            adapter.onItemDraging(fromPosition - adapter.getHeadersCount(), toPosition - adapter.getHeadersCount());
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        if (isListItem(position)) {
            adapter.onItemSwipeing(viewHolder, position - adapter.getHeadersCount());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //侧滑时改变Item的透明度
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //拖拽时改变Item的透明度
            viewHolder.itemView.setAlpha(0.7f);
        }
    }

    /**
     * 检查adapter中位置是否属于列表数据
     */
    private boolean isListItem(int position) {
        return (!adapter.isHeader(position)) && (!adapter.isFooter(position));
    }
}
