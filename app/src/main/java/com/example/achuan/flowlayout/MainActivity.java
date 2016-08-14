package com.example.achuan.flowlayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.achuan.flowlayout.view.FlowLayout;
public class MainActivity extends AppCompatActivity {
    private FlowLayout mFlowLayout;
    private String[] mVals=new String[]
            {
               "C","Java","C++","CSS","JSP","PHP","MYSQL",
                    "C","Java","C++","CSS","JSP","PHP","MYSQL",
                    "C","Java","C++","CSS","JSP","PHP","MYSQL",
            };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout= (FlowLayout) findViewById(R.id.id_flowlayout);//加载流式布局
        initData();//初始化数据
    }
    /*初始化数据*/
    private void initData()
    {
        /*//循环创建按钮
        for(int i=0;i<mVals.length;i++)
        {
            Button btn=new Button(this);//新建按钮
            //初始化设置ViewGroup的高和宽的布局模式
            ViewGroup.MarginLayoutParams lp=new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
            );
            btn.setText(mVals[i]);//为按钮添加文本
            mFlowLayout.addView(btn,lp);//添加按钮及模式到流式布局中
        }*/
        //通过LayoutInflater将tv.xml中的布局找出来
        LayoutInflater mInflater=LayoutInflater.from(this);
        //循环创建文本
        for(int i=0;i<mVals.length;i++)
        {
            //将布局找出来并放到流式布局中去
         TextView tv= (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
            tv.setText(mVals[i]);
          mFlowLayout.addView(tv);
        }
    }
}
