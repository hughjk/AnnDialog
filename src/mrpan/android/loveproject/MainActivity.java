package mrpan.android.loveproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrpan.android.loveproject.DB.DatabaseHelper;
import mrpan.android.loveproject.view.AdViewPager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity implements OnClickListener {

	private ViewPager vpViewPager = null;
	private List<View> views = null;

	private Animation animation = null;

	private int offset; // 间隔
	private int cursorWidth; // 游标的长度
	private int originalIndex = 0;
	private ImageView cursor = null;

	private PullToRefreshListView ptrlvHeadLineNews = null;
	private NewListAdapter newAdapter = null;

	private AdViewPager vpAdv = null;
	private ViewGroup vg = null;
	private ImageView[] imageViews = null;
	private List<View> advs = null;
	private int currentPage = 0;

	private SlidingMenu slidingMenu = null;
	
	private DatabaseHelper db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = DatabaseHelper.getDataBase(this);
		db.getReadableDatabase();
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
		((LinearLayout) findViewById(R.id.loginNow))
		.setOnClickListener(this);
		((TextView) findViewById(R.id.tvTag1)).setOnClickListener(this);
		((TextView) findViewById(R.id.tvTag2)).setOnClickListener(this);
		((TextView) findViewById(R.id.tvTag3)).setOnClickListener(this);
		views = new ArrayList<View>();
		views.add(LayoutInflater.from(this).inflate(R.layout.layout1, null));
		views.add(LayoutInflater.from(this).inflate(R.layout.layout3, null));
		views.add(LayoutInflater.from(this).inflate(R.layout.layout2, null));

		vpViewPager = (ViewPager) findViewById(R.id.vpViewPager1);
		vpViewPager.setAdapter(new MyPagerAdapter(views));
		vpViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		initCursor(views.size());

		MyPagerAdapter myPagerAdapter = (MyPagerAdapter) vpViewPager
				.getAdapter();
		View v1 = myPagerAdapter.getItemAtPosition(0);
		// View v2 = myPagerAdapter.getItemAtPosition(1);
		// View v3 = myPagerAdapter.getItemAtPosition(2);
		ptrlvHeadLineNews = (PullToRefreshListView) v1
				.findViewById(R.id.ptrlvHeadLineNews);
		// ptrlvEntertainmentNews = (PullToRefreshListView) v2
		// .findViewById(R.id.ptrlvEntertainmentNews);
		// ptrlvFinanceNews = (PullToRefreshListView) v3
		// .findViewById(R.id.ptrlvFinanceNews);
		newAdapter = new NewListAdapter(this, getSimulationNews(10));
		initPullToRefreshListView(ptrlvHeadLineNews, newAdapter);
		// initPullToRefreshListView(ptrlvEntertainmentNews, newAdapter);
		// initPullToRefreshListView(ptrlvFinanceNews, newAdapter);
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
		case R.id.loginNow:
			Intent intent=new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivity(intent);
			finish();
			break;
		case R.id.tvTag1:
			vpViewPager.setCurrentItem(0);
			break;
		case R.id.tvTag2:
			vpViewPager.setCurrentItem(1);
			break;
		case R.id.tvTag3:
			vpViewPager.setCurrentItem(2);
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

	/**
	 * 获取N条模拟的新闻数据<br>
	 * 打包成ArrayList返回
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getSimulationNews(int n) {
		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hm;
		for (int i = 0; i < n; i++) {
			hm = new HashMap<String, String>();
			if (i % 2 == 0) {
				hm.put("uri",
						"http://images.china.cn/attachement/jpg/site1000/20131029/001fd04cfc4813d9af0118.jpg");
			} else {
				hm.put("uri",
						"http://photocdn.sohu.com/20131101/Img389373139.jpg");
			}
			hm.put("title", "国内成品油价两连跌几成定局");
			hm.put("content", "国内成品油今日迎调价窗口，机构预计每升降价0.1元。");
			hm.put("review", i + "跟帖");
			ret.add(hm);
		}
		return ret;
	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			int one = 2 * offset + cursorWidth;
			int two = one * 2;

			switch (originalIndex) {
			case 0:
				if (arg0 == 1) {
					animation = new TranslateAnimation(0, one, 0, 0);
				}
				if (arg0 == 2) {
					animation = new TranslateAnimation(0, two, 0, 0);
				}
				break;
			case 1:
				if (arg0 == 0) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
				if (arg0 == 2) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			case 2:
				if (arg0 == 1) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				if (arg0 == 0) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			}
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);

			originalIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

	}

	/**
	 * 初始化PullToRefreshListView<br>
	 * 初始化在PullToRefreshListView中的ViewPager广告栏
	 * 
	 * @param rtflv
	 * @param adapter
	 */
	public void initPullToRefreshListView(PullToRefreshListView rtflv,
			NewListAdapter adapter) {
		rtflv.setMode(Mode.BOTH);
		rtflv.setOnRefreshListener(new MyOnRefreshListener2(rtflv));
		rtflv.setAdapter(adapter);

		if (rtflv.getId() == R.id.ptrlvHeadLineNews) {
			RelativeLayout rlAdv = (RelativeLayout) LayoutInflater.from(this)
					.inflate(R.layout.sliding_ad, null);
			vpAdv = (AdViewPager) rlAdv.findViewById(R.id.vpAdv);
			vg = (ViewGroup) rlAdv.findViewById(R.id.viewGroup);

			advs = new ArrayList<View>();
			ImageView iv;
			iv = new ImageView(this);
			iv.setBackgroundResource(R.drawable.ic_launcher);
			advs.add(iv);

			// iv = new ImageView(this);
			// iv.setBackgroundResource(R.drawable.new_img2);
			// advs.add(iv);
			//
			// iv = new ImageView(this);
			// iv.setBackgroundResource(R.drawable.new_img3);
			// advs.add(iv);
			//
			// iv = new ImageView(this);
			// iv.setBackgroundResource(R.drawable.new_img4);
			// advs.add(iv);

			vpAdv.setAdapter(new AdAdapter());
			vpAdv.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					currentPage = arg0;
					for (int i = 0; i < advs.size(); i++) {
						if (i == arg0) {
							imageViews[i]
									.setBackgroundResource(R.drawable.banner_dian_focus);
						} else {
							imageViews[i]
									.setBackgroundResource(R.drawable.banner_dian_blur);
						}
					}
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});

			imageViews = new ImageView[advs.size()];
			ImageView imageView;
			for (int i = 0; i < advs.size(); i++) {
				imageView = new ImageView(this);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageViews[i] = imageView;
				if (i == 0) {
					imageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_focus);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
				vg.addView(imageViews[i]);
			}

			rtflv.getRefreshableView().addHeaderView(rlAdv, null, false);

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					vpAdv.setCurrentItem(msg.what);
					super.handleMessage(msg);
				}
			};
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(5000);
							currentPage++;
							if (currentPage > advs.size() - 1) {
								currentPage = 0;
							}
							handler.sendEmptyMessage(currentPage);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {

		private PullToRefreshListView mPtflv;

		public MyOnRefreshListener2(PullToRefreshListView ptflv) {
			this.mPtflv = ptflv;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			String label = DateUtils.formatDateTime(getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetNewsTask(mPtflv).execute();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 上拉加载
			new GetNewsTask(mPtflv).execute();
		}

	}

	class AdAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return advs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(advs.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(advs.get(position));
			return advs.get(position);
		}

	}

	/**
	 * 请求网络获得新闻信息
	 * 
	 * @author Louis
	 * 
	 */
	class GetNewsTask extends AsyncTask<String, Void, Integer> {
		private PullToRefreshListView mPtrlv;

		public GetNewsTask(PullToRefreshListView ptrlv) {
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Integer doInBackground(String... params) {

			return 1;
			// if (CommonUtil.isWifiConnected(MainActivity.this)) {
			// try {
			// Thread.sleep(1000);
			// return HTTP_REQUEST_SUCCESS;
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			// return HTTP_REQUEST_SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:// HTTP_REQUEST_SUCCESS:
				newAdapter.addNews(getSimulationNews(10));
				newAdapter.notifyDataSetChanged();
				break;
			case 2:// HTTP_REQUEST_ERROR:
				Toast.makeText(MainActivity.this, "请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
}
