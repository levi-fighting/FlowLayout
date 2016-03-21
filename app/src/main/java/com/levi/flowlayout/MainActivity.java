package com.levi.flowlayout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.levi.views.FlowLayout;

public class MainActivity extends Activity {
    private FlowLayout mFlowLayout;
    private FlowLayout mCustomFlowLayout;
    private Button btnGenerate;
    private LinearLayout mContainer;
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome to android", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "studio"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowLayout = (FlowLayout) findViewById(R.id.my_flow_layout);
        mFlowLayout.setFlowContent(mVals)
                .setFlowItemListener(new FlowLayout.OnFlowItemCallback() {
                    @Override
                    public void onItemClick(View v) {
                        Toast.makeText(MainActivity.this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        Toast.makeText(MainActivity.this, ((TextView) v).getText() + " --> long click", Toast.LENGTH_SHORT).show();
                    }
                }).build();

        
        mContainer = (LinearLayout) findViewById(R.id.container);
        btnGenerate = (Button) findViewById(R.id.btn_generate);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomFlowLayout = new FlowLayout(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mCustomFlowLayout.setLayoutParams(lp);

                mCustomFlowLayout.setBackgroundColor(Color.parseColor("#e5e5f5"));
                mCustomFlowLayout.setCustomItemLayout(R.layout.flow_item)
                        .setFlowContent(mVals)
                        .setFlowItemListener(new FlowLayout.OnFlowItemCallback() {
                            @Override
                            public void onItemClick(View v) {
                                Toast.makeText(MainActivity.this, ((TextView) v).getText() + " --> clicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemLongClick(View v) {

                            }
                        }).build();
                mContainer.addView(mCustomFlowLayout);
            }
        });
    }

}
