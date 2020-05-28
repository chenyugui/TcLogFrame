package com.taichuan.code.ui.loadmoreview.ui;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.taichuan.code.R;
import com.taichuan.code.ui.loadmoreview.bean.ItemBean;
import com.taichuan.code.ui.loadmoreview.viewholder.SuperViewHolder;

import java.util.List;

/**
 * 集成"加载更多"和多种ItemType的BaseAdapter
 *
 * @param <T> 继承于ItemBean的Bean类，拥有itemType属性
 */
public abstract class CommonAdapter<T extends ItemBean> extends BaseAdapter {
    private static final String TAG = "CommonAdapter";
    /**
     * 实际数据Bean集合（Bean要继承于ItemBean）
     */
    protected List<T> mDatas;
    /**
     * 存放多种类型的item的LayoutID
     */
    private SparseIntArray layoutCache = new SparseIntArray();
    /**
     * footItem的View
     */
    private View footView;
    /**
     * 是否显示footItem
     */
    private boolean isCanLoadMore;
    private boolean superIsCanLoadMore;
    /**
     * footItem的layoutID
     */
    private int footLayoutID;
    /**
     * footItem的itemType
     */
    private int footItemType;

    /**
     * @param datas         数据
     * @param isCanLoadMore 是否能加载更多（为true时，需要配合LoadMoreListView使用，普通的ListView无效）
     * @param itemLayoutID  各种类型item的layoutID数组，支持多种类型。（从0开始计算，第一个代表itemType为0的itemLayoutID，第二个代表itemType为1的itemLayoutID，依此类推 ）
     */
    public CommonAdapter(List<T> datas, boolean isCanLoadMore, Integer... itemLayoutID) {
        init(datas, isCanLoadMore, itemLayoutID);
    }

    private void init(List<T> datas, boolean isCanLoadMore, Integer... layoutID) {
        mDatas = datas;
        for (int i = 0; i < layoutID.length; i++) {
            layoutCache.put(i, layoutID[i]);
        }
        this.superIsCanLoadMore = isCanLoadMore;
        this.isCanLoadMore = isCanLoadMore;
        footItemType = layoutID.length;
        footLayoutID = R.layout.loadmore_footer;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    protected void setIsShowFooter(boolean isShowFooter) {
        if (this.isCanLoadMore != isShowFooter) {
            if (superIsCanLoadMore) {
                this.isCanLoadMore = isShowFooter;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        if (isCanLoadMore)
            return mDatas.size() + 1;
        else
            return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (mDatas == null)
            return null;
        if (mDatas.size() > position)
            return mDatas.get(position);
        else
            return footView;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        if (isCanLoadMore) {
            return layoutCache.size() + 1;
        }
        return layoutCache.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() > position) {// not footItem
            int itemType = mDatas.get(position).getItemType();
            if (itemType >= layoutCache.size()) {//itemType不符合，设置第一个ItemType
                try {
                    throw new Exception("itemType 不符合，itemType从0开始计算，只能小于item种类。此itemType=" + itemType + ",但item种类数为" + layoutCache.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                itemType = 0;
            }
            return itemType;
        } else if (isCanLoadMore)// is footItem
            return footItemType;
        else
            return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SuperViewHolder viewHolder;
        if (mDatas.size() > position) {// not footItem
            int itemType = mDatas.get(position).getItemType();
            if (view == null) {
                int layoutID = layoutCache.get(0);
                if (layoutCache.size() > itemType)
                    layoutID = layoutCache.get(itemType);

                view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutID, viewGroup, false);
                viewHolder = new SuperViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (SuperViewHolder) view.getTag();
            }
            showView(viewHolder, mDatas.get(position), position, itemType);
        } else {// is footItem
            if (footView == null) {
                footView = LayoutInflater.from(viewGroup.getContext()).inflate(footLayoutID, viewGroup, false);
            }
            view = footView;
        }
        return view;
    }

    /**
     * 集成了ViewHolder，直接利用viewHolder.setText("")、viewHolder.setOnClickListener() 等方法进行视图显示配置即可。
     *
     * @param viewHolder
     * @param data
     * @param position
     * @param itemType
     */
    public abstract void showView(SuperViewHolder viewHolder, T data, int position, int itemType);
}
