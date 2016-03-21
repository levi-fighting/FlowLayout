package com.levi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levi.flowlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levi on 16/3/19.
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private Context mContext;
    private boolean mMeasured = false;
    private boolean mLayouted = false;
    private List<View> mChildrenViews;
    private OnFlowItemCallback mItemCallback;
    private int mDefaultFlowItemLayout = R.layout.tv_flow;

    /**
     * the actual width and height of flow layout
     */
    private int width = 0;
    private int height = 0;

    /**
     * store all children view in flow layout
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();

    /**
     * height of each row in flow layout
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMeasured) {
            setMeasuredDimension(width, height);
            return;
        }

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        mMeasured = true;

        //width and height of each row in flow layout
        int lineWidth = 0, lineHeight = 0;

        //get children count of the FlowLayout
        int mCount = getChildCount();
        if (mCount <= 0) {
            try {
                throw new Exception("FlowLayout's sub view is empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //collection of each row
        List<View> mLineViews = new ArrayList<View>();

        for (int i = 0; i < mCount; i++) {
            View child = getChildAt(i);

            //measure width and height of the child view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //get LayoutParams of child
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //calculate the actual width and height of child view
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > viewWidth - getPaddingLeft() - getPaddingRight()) {
                mAllViews.add(mLineViews);
                mLineHeight.add(lineHeight);

                //get max width
                width = Math.max(width, lineWidth);
                //add up lineHeight
                height += lineHeight;

                //reset
                lineWidth = 0;
                lineHeight = 0;
                mLineViews = new ArrayList<View>();
            }
            mLineViews.add(child);
            lineHeight = Math.max(lineHeight, childHeight);
            lineWidth += childWidth;
        }
        //add the last row
        mAllViews.add(mLineViews);
        mLineHeight.add(lineHeight);
        width = Math.max(lineWidth, width);
        height += lineHeight;

        Log.i(TAG, "width=" + width + ",height=" + height);
        if (modeWidth == MeasureSpec.EXACTLY) {
            width = viewWidth;
        } else {
            width = width + getPaddingLeft() + getPaddingRight();
        }
        if (modeHeight == MeasureSpec.EXACTLY) {
            height = viewHeight;
        } else {
            height = height + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mLayouted) {
            return;
        }
        mLayouted = true;

        int lineHeight;
        List<View> lineViews;

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNumber = mAllViews.size();

        for (int i = 0; i < lineNumber; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * set flowlayout content
     *
     * @param content
     */
    public FlowLayout setFlowContent(String[] content) {
        if (content == null || content.length == 0) {
            try {
                throw new Exception("the argument of setFlowContent method is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mChildrenViews = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < content.length; i++) {
            TextView tv = (TextView) inflater.inflate(mDefaultFlowItemLayout, this, false);
            tv.setText(content[i]);
            mChildrenViews.add(tv);
        }
        return this;
    }

    /**
     * this method is used with setFlowContent(String[]), if you want to have a custom item view,
     * you must call setCustomItemLayout(int) first, otherwise,flow item view will use default layout.
     *
     * @param layoutId
     * @return
     */
    public FlowLayout setCustomItemLayout(int layoutId) {
        this.mDefaultFlowItemLayout = layoutId;
        return this;
    }

    /**
     * set flowlayout's children views
     *
     * @param children
     */
    public FlowLayout setFlowChildrenViews(List<View> children) {
        if (children == null || children.size() == 0) {
            try {
                throw new Exception("the argument of setFlowChildrenViews method is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mChildrenViews = children;
        return this;
    }

    /**
     * set listener
     *
     * @param callback
     * @return
     */
    public FlowLayout setFlowItemListener(OnFlowItemCallback callback) {
        this.mItemCallback = callback;
        return this;
    }

    /**
     * must call build() at last
     */
    public void build() {
        if (mChildrenViews == null || mChildrenViews.size() == 0) {
            try {
                throw new Exception("FlowLayout's sub view is empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (View v : mChildrenViews) {
            setViewEvent(v);
            this.addView(v);
        }
    }

    /**
     * set view event, can expand by yourself
     *
     * @param v
     */
    private void setViewEvent(View v) {
        if (v == null) {
            return;
        }
        if (mItemCallback != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick");
                    mItemCallback.onItemClick(v);
                }
            });
            v.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemCallback.onItemLongClick(v);
                    return true;
                }
            });
        }
    }

    /**
     * flow item callback, can expands it
     */
    public interface OnFlowItemCallback {
        void onItemClick(View v);

        void onItemLongClick(View v);
    }
}
