package com.example.achuan.flowlayout.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by achuan on 16-2-16.
 * 功能：实现自定义流式布局,注意考虑有边距时的情况
 */
public class FlowLayout extends ViewGroup
{
    /*******************使用了自定义属性时调用该方法******************/
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /*未使用自定义属性时调用该方法*/
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    /*基本的文本创建时调用该方法*/
    public FlowLayout(Context context) {
        this(context, null);
    }
    /****************测量子View的宽和高，设置自己的宽和高************/
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth=MeasureSpec.getSize(widthMeasureSpec);//获得主布局的宽度
        int sizeHeight=MeasureSpec.getSize(heightMeasureSpec);//获得高度
        int modeWidth=MeasureSpec.getMode(widthMeasureSpec);//获得宽度的模式
        int modeHeight=MeasureSpec.getMode(heightMeasureSpec);//获得高度的模式
        //warp_content时设置属性值
        int width=0;//记录界面中的元素占有的最大宽度和高度
        int height=0;
        //记录每行总占有元素的宽和高
        int lineWidth=0;
        int lineHeight=0;
        //得到内部元素的个数
        int count=getChildCount();
        for(int i=0;i<count;i++)
        {
            View child=getChildAt(i);
            //测量子View的宽和高
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            //子View占据的宽度和高度
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight=child.getHeight()+lp.topMargin+lp.bottomMargin;
            if((lineWidth+childWidth)>(sizeWidth-getPaddingLeft()-getPaddingRight()))
            {
                //换行处理
                width=Math.max(width,lineWidth);//对比得到当前界面中的最大宽度
                height+=lineHeight;//记录当前界面中总元素的高度
                lineWidth=childWidth;//重置行宽
                lineHeight=childHeight;//重置行高
            }
            else {
                //未换行处理
                lineWidth+=childWidth;//叠加宽度为行宽
                lineHeight=Math.max(childHeight,lineHeight);//行高为该行最高元素的高
            }
        }
        //最后一个元素的处理
        width=Math.max(width,lineWidth);
        height+=lineHeight;
        //判断模式来设置宽和高
        setMeasuredDimension(
                modeWidth==MeasureSpec.AT_MOST?width+getPaddingRight()+getPaddingLeft():sizeWidth,
                modeHeight==MeasureSpec.AT_MOST?Math.min(height+getPaddingTop()+getPaddingBottom(),sizeHeight):sizeHeight);
    }
    //分行存储View,单个view>>一行view>>总view
    private List<List<View>> mAllViews=new ArrayList<List<View>>();
    //每一行的高度,Integer型
    private List<Integer> mLineHeight= new ArrayList<Integer>();
    /****************设置子View 的位置************/
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
         //当前ViewGroup（FlowLayout）的宽和高
        int width=getWidth();
        int height=getHeight();
        //行宽和高
        int lineWidth=0;
        int lineHeight=0;
        //存储每一行的View
        List<View> lineViews=new ArrayList<>();
        int cCount=getChildCount();
        //遍历子View
        for(int i=0;i<cCount;i++)
        {
            View child=getChildAt(i);
            //得到LayoutParams
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            //子View占据的宽度和高度
            int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight=child.getHeight()+lp.topMargin+lp.bottomMargin;
            if((lineWidth+childWidth)>(width-getPaddingRight()-getPaddingLeft()))
            {
                //换行处理
                mLineHeight.add(lineHeight);//记录lineHeight
                mAllViews.add(lineViews);//记录当前行的所有的View
                lineWidth=childWidth;//重置行宽
                lineHeight=childHeight;//重置行高
                //重置行的View集合
                lineViews=new ArrayList<View>();
            }
            else {
                //未换行处理
                lineWidth+=childWidth;//叠加宽度为行宽
                lineHeight=Math.max(childHeight,lineHeight);//行高为该行最高元素的高
            }
            lineViews.add(child);//添加子View
        }
        //最后一个元素的处理
        mLineHeight.add(lineHeight);//记录lineHeight
        mAllViews.add(lineViews);
        //设置子View的位置
        int left=getPaddingLeft();
        int top=getPaddingTop();
        int lineNum=mAllViews.size();//行数
        //遍历所有行
        for(int i=0;i<lineNum;i++)
        {
            lineViews=mAllViews.get(i);//得到当前行的View集合
            lineHeight=mLineHeight.get(i);//得到当前行的高度
            //遍历一行
            for(int j=0;j<lineViews.size();j++)
            {
                View child=lineViews.get(j);//拿到子View
                //判断child的状态
                if(child.getVisibility()==View.GONE)
                {
                    continue;
                }
                //得到LayoutParams
                MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
                //确定位置
                int lx=left+lp.leftMargin;//左上角的横坐标
                int ly=top+lp.topMargin;//左上角的纵坐标
                int rx=lx+child.getMeasuredWidth();//右下角的横坐标
                int ry=ly+child.getMeasuredHeight();//右下角的纵坐标
                //显示出子View元素
                //System.out.println("lx:"+lx+",ly:"+ly);
                child.layout(lx,ly,rx,ry);
                left+=lp.leftMargin+lp.rightMargin+child.getMeasuredWidth();//横向移动布局
            }
            left=getPaddingLeft();//一行结束后重置横向位置
            top+=lineHeight;//纵向移动布局
        }
    }
    /*设置与当前ViewGroup对应的LayoutParams*/
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
        //重写父类的该方法，返回MarginLayoutParams的实例
    }
}
