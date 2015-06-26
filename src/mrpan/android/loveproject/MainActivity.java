package mrpan.android.loveproject;

import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

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

		// 设置抽屉菜单
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单
		slidingMenu.setMenu(R.layout.slidingmenu_left);
		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 将抽屉菜单与主页面关联起来
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		findViewById();
	}

	void findViewById() {
		findViewById(R.id.bNew).setOnClickListener(this);
		findViewById(R.id.bPersonal).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.left_menu1))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.left_menu2))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.left_menu3))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.left_menu4))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.left_menu5))
				.setOnClickListener(this);
		// ((TextView) findViewById(R.id.tvTag1)).setOnClickListener(this);
		// ((TextView) findViewById(R.id.tvTag2)).setOnClickListener(this);
		// ((TextView) findViewById(R.id.tvTag3)).setOnClickListener(this);
	}

	/**
	 * 根据tag的数量初始化游标的位置
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
		case R.id.left_menu1:
			Log.d("Main", "Menu1_Clicked");
			break;
		case R.id.left_menu2:
			Log.d("Main", "Menu2_Clicked");
			break;
		case R.id.left_menu3:
			Log.d("Main", "Menu3_Clicked");
			break;
		case R.id.left_menu4:
			Log.d("Main", "Menu4_Clicked");
			break;
		case R.id.left_menu5:
			Log.d("Main", "Menu5_Clicked");
			break;
		case R.id.bPersonal:
			slidingMenu.showSecondaryMenu();
			break;
		}
	}
}
