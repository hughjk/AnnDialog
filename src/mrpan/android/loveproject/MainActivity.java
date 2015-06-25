package mrpan.android.loveproject;

import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private ViewPager vpViewPager = null;
	private List<View> views = null;
	
	private int offset; // 间隔
	private int cursorWidth; // 游标的长度
	private int originalIndex = 0;
	private ImageView cursor = null;
	
	private AdvViewPager vpAdv = null;
	private ViewGroup vg = null;
	private ImageView[] imageViews = null;
	private List<View> advs = null;
	private int currentPage = 0;
	
	private SlidingMenu slidingMenu = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        	// 设置抽屉菜单
     		slidingMenu = new SlidingMenu(this);
     		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
     		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单
     		slidingMenu.setMenu(R.layout.slidingmenu_left);
     		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
     		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
     		// 将抽屉菜单与主页面关联起来
     		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        }
    
    void findViewById(){
    	findViewById(R.id.bNew).setOnClickListener(this);
		//findViewById(R.id.bPersonal).setOnClickListener(this);
		//((TextView) findViewById(R.id.tvTag1)).setOnClickListener(this);
		//((TextView) findViewById(R.id.tvTag2)).setOnClickListener(this);
		//((TextView) findViewById(R.id.tvTag3)).setOnClickListener(this);
		
    }
    
    /**
	 * 根据tagd的数量初始化游标的位置
	 * 
	 * @param tagNum
	 */
	public void initCursor(int tagNum) {
		cursorWidth = BitmapFactory.decodeResource(getResources(),
				R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		offset = ((dm.widthPixels / tagNum) - cursorWidth) / 2;

		cursor = (ImageView) findViewById(R.id.ivCursor);
		Matrix matrix = new Matrix();
		matrix.setTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bNew:
			slidingMenu.showMenu();
			break;
//		case R.id.bPersonal:
//			slidingMenu.showSecondaryMenu();
//			break;
		}
	}
}
