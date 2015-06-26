package mrpan.android.loveproject.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author MrPan
 * @since 2015-06-26
 * @email wslongchen@vip.qq.com
 */
public class AdViewPager extends ViewPager {

	public AdViewPager(Context context) {
		super(context);
	}	
	
	public AdViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(arg0);
	}
	
}
