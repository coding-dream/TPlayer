package com.less.tplayer.mvp.feature;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.less.tplayer.R;
import com.less.tplayer.mvp.feature.data.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author deeper
 * @date 2017/11/25
 */

public class FeatureAdapter extends RecyclerView.Adapter {

    protected List<Feature> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

    protected int mSelectedPosition = -0;
    public static final int STATE_NO_MORE = 1;
    public static final int STATE_LOAD_MORE = 2;
    public static final int STATE_INVALID_NETWORK = 3;
    public static final int STATE_HIDE = 5;
    private static final int STATE_REFRESHING = 6;
    public static final int STATE_LOAD_ERROR = 7;
    public static final int STATE_LOADING = 8;

    /**
     * 行为模式: NEITHER,ONLY_HEADER,ONLY_FOOTER,BOTH_HEADER_FOOTER
     */
    private final int BEHAVIOR_MODE;

    /**
     * 状态: STATE_LOADING,STATE_HIDE,STATE_NO_MORE....
     */
    protected int mState;

    public static final int NEITHER = 0;
    public static final int ONLY_HEADER = 1;
    public static final int ONLY_FOOTER = 2;
    public static final int BOTH_HEADER_FOOTER = 3;

    public static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_FOOTER = -2;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private MyOnClickListener onClickListener;
    private MyOnLongClickListener onLongClickListener;


    protected View mHeaderView;

    private OnLoadingHeaderCallBack onLoadingHeaderCallBack;

    private Handler handler = new Handler();

    public FeatureAdapter(Context context, int mode) {
        this.mItems = new ArrayList<>();
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.BEHAVIOR_MODE = mode;
        this.mState = STATE_HIDE;
        initListener();
    }

    /**
     * 初始化listener
     */
    private void initListener() {
        onClickListener = new MyOnClickListener() {
            @Override
            public void onClick(int position, long itemId) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, itemId);
                }
            }
        };

        onLongClickListener = new MyOnLongClickListener() {
            @Override
            public boolean onLongClick(int position, long itemId) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onLongClick(position, itemId);
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null) {
                    return onLoadingHeaderCallBack.onCreateHeaderHolder(parent);
                } else {
                    throw new IllegalArgumentException("you have to impl the interface when using this viewType");
                }
            case VIEW_TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.recycler_footer_view, parent, false));
            default:
                final RecyclerView.ViewHolder holder = onCreateDefaultViewHolder(parent, viewType);
                if (holder != null) {
                    holder.itemView.setTag(holder);
                    holder.itemView.setOnLongClickListener(onLongClickListener);
                    holder.itemView.setOnClickListener(onClickListener);
                }
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null) {
                    onLoadingHeaderCallBack.onBindHeaderHolder(holder, position);
                }
                break;
            case VIEW_TYPE_FOOTER:
                FooterViewHolder fvh = (FooterViewHolder) holder;
                fvh.itemView.setVisibility(View.VISIBLE);
                switch (mState) {
                    case STATE_INVALID_NETWORK:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_network_error));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_MORE:
                    case STATE_LOADING:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_loading));
                        fvh.pb_footer.setVisibility(View.VISIBLE);
                        break;
                    case STATE_NO_MORE:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_not_more));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_REFRESHING:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_refreshing));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_ERROR:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_load_error));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_HIDE:
                        fvh.itemView.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                break;
            default:
                onBindDefaultViewHolder(holder, getItems().get(getIndex(position)), position);
                break;
        }
    }

    /**
     * 当添加到RecyclerView时获取GridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param recyclerView the mRecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType(position) == VIEW_TYPE_FOOTER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 当RecyclerView在windows活动时获取StaggeredGridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param holder the RecyclerView.ViewHolder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (BEHAVIOR_MODE == ONLY_HEADER) {
                p.setFullSpan(holder.getLayoutPosition() == 0);
            } else if (BEHAVIOR_MODE == ONLY_FOOTER) {
                p.setFullSpan(holder.getLayoutPosition() == mItems.size() + 1);
            } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
                if (holder.getLayoutPosition() == 0 || holder.getLayoutPosition() == mItems.size() + 1) {
                    p.setFullSpan(true);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && (BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER)) {
            return VIEW_TYPE_HEADER;
        }
        if (position + 1 == getItemCount() && (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER)) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public Feature getSelectedItem() {
        if (mSelectedPosition < 0 || mSelectedPosition >= mItems.size()) {
            return null;
        }
        return mItems.get(mSelectedPosition);
    }

    /**
     * 单选
     */
    public void setSelectedPosition(int selectedPosition) {
        if (selectedPosition != mSelectedPosition) {
            updateItem(mSelectedPosition);
            mSelectedPosition = selectedPosition;
            updateItem(mSelectedPosition);
        }
        this.mSelectedPosition = selectedPosition;
        updateItem(selectedPosition);
    }

    protected int getIndex(int position) {
        return BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER ? position - 1 : position;
    }

    /**
     * 获取ItemCount,包括headerView或footerView
     *
     * @return RecycleView的所有item_view 项数
     */
    @Override
    public int getItemCount() {
        if (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == ONLY_HEADER) {
            return mItems.size() + 1;
        } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
            return mItems.size() + 2;
        } else {
            return mItems.size();
        }
    }

    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type){
        return new ProjectViewHolder(mInflater.inflate(R.layout.item_list_feature, parent, false));
    }

    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Feature item, int position){
        ProjectViewHolder h = (ProjectViewHolder) holder;
        h.mTextName.setText(item.getName());
        h.mTextAge.setText(item.getAge());
    }

    public final View getHeaderView() {
        return this.mHeaderView;
    }

    public final void setHeaderView(View view) {
        this.mHeaderView = view;
    }

    /**
     * 获取List list 项数
     *
     * @return
     */
    public final List<Feature> getItems() {
        return mItems;
    }


    public void addAll(final List<Feature> items) {
        if (items != null) {
            this.mItems.addAll(items);
            notifyItemRangeInserted(mItems.size(), items.size());
        }
    }

    public final void addItem(Feature item) {
        if (item != null) {
            this.mItems.add(item);
            notifyItemChanged(mItems.size());
        }
    }

    public void addItem(int position, Feature item) {
        if (item != null) {
            this.mItems.add(getIndex(position), item);
            notifyItemInserted(position);
        }
    }

    public void replaceItem(int position, Feature item) {
        if (item != null) {
            this.mItems.set(getIndex(position), item);
            notifyItemChanged(position);
        }
    }

    public void updateItem(final int position) {
        if (getItemCount() > position) {
            // issue: Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position);
                }
            });
        }
    }


    public final void removeItem(Feature item) {
        if (this.mItems.contains(item)) {
            int position = mItems.indexOf(item);
            this.mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.mItems.remove(getIndex(position));
            notifyItemRemoved(position);
        }
    }

    public final Feature getItem(int position) {
        int p = getIndex(position);
        if (p < 0 || p >= mItems.size()) {
            return null;
        }
        return mItems.get(getIndex(position));
    }

    public final void resetItem(List<Feature> items) {
        if (items != null) {
            clear();
            addAll(items);
        }
    }

    public final void clear() {
        this.mItems.clear();
        setState(STATE_HIDE, false);
        notifyDataSetChanged();
    }

    /**
     * 设置当前状态: 据此来处理onBindViewHolder中footer的显示.
     *
     * @param mState
     * @param isUpdate
     */
    public void setState(int mState, boolean isUpdate) {
        this.mState = mState;
        if (isUpdate) {
            updateItem(getItemCount() - 1);
        }
    }

    public int getState() {
        return mState;
    }

    /**
     * 添加项点击事件
     *
     * @param onItemClickListener the RecyclerView item click listener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加项点长击事件
     *
     * @param onItemLongClickListener the RecyclerView item long click listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected void setOnLoadingHeaderCallBack(OnLoadingHeaderCallBack listener) {
        onLoadingHeaderCallBack = listener;
    }


    /**
     * 可以共用同一个listener，相对高效
     */
    public static abstract class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            onClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract void onClick(int position, long itemId);
    }


    /**
     * 可以共用同一个listener，相对高效
     */
    public static abstract class MyOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            return onLongClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract boolean onLongClick(int position, long itemId);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, long itemId);
    }


    public interface OnItemLongClickListener {
        void onLongClick(int position, long itemId);
    }

    /**
     * for load header view
     */
    public interface OnLoadingHeaderCallBack {
        RecyclerView.ViewHolder onCreateHeaderHolder(ViewGroup parent);

        void onBindHeaderHolder(RecyclerView.ViewHolder holder, int position);
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pb_footer;
        TextView tv_footer;

        FooterViewHolder(View view) {
            super(view);
            pb_footer = (ProgressBar) view.findViewById(R.id.pb_footer);
            tv_footer = (TextView) view.findViewById(R.id.tv_footer);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView mTextName,mTextAge;

        ProjectViewHolder(View itemView) {
            super(itemView);
            mTextName = (TextView) itemView.findViewById(R.id.tv_name);
            mTextAge = (TextView) itemView.findViewById(R.id.tv_age);
        }
    }
}