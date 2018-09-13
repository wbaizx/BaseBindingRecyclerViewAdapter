package baseadapter.com.basebindingrecyclerviewadapter;

import baseadapter.com.basebindingrecyclerviewadapter.databinding.NormalRecyclerviewLayoutBinding;
import baseadapter.com.library.BaseBindingRecyclerViewAdapter;

public class NormalAdapter extends BaseBindingRecyclerViewAdapter<NormalRecyclerviewLayoutBinding, BeanViewModel> {

    @Override
    protected int getLayoutViewType(BeanViewModel beanViewModel) {
        return 0;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.normal_recyclerview_layout;
    }

    @Override
    protected void convert(NormalRecyclerviewLayoutBinding dataBinding, BeanViewModel vm, int position, int viewType) {
        dataBinding.setVm(vm);
    }
}
