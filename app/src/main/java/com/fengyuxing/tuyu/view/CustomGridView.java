package com.fengyuxing.tuyu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/****
 * 自定义CustomGridView  避免和SrollView一起使用滑动冲突
 * 社区发表看法 选择选项
 */
public class CustomGridView extends GridView {

	public CustomGridView(Context context) {
		super(context);
	}

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}*/

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec;
//		L.d("centerAAA", "heightMeasureSpec = " + Integer.toBinaryString(heightMeasureSpec));
		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
			heightSpec = MeasureSpec.makeMeasureSpec(
					0x3fffffff, MeasureSpec.AT_MOST);
		}
		else {
			heightSpec = heightMeasureSpec;
		}
		super.onMeasure(widthMeasureSpec, heightSpec);
	}

	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
*/
}
