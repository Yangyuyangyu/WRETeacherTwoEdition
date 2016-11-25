package com.teacherhelp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGridView extends GridView { 
	public boolean hasScrollBar = true;
public NoScrollGridView(Context context) { 
super(context); 

} 
public NoScrollGridView(Context context, AttributeSet attrs) { 
super(context, attrs); 
} 

@Override 
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	int expandSpec = heightMeasureSpec;
    if (hasScrollBar) {
        expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);//
    } else {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
} 
} 
