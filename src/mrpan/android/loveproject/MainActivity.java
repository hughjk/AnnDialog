package mrpan.android.loveproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import mrpan.android.loveproject.DB.DatabaseHelper;
import mrpan.android.loveproject.bean.Dialog;
import mrpan.android.loveproject.bean.User;
import mrpan.android.loveproject.bean.Util;
import mrpan.android.loveproject.sdk.QQShareDemo;
import mrpan.android.loveproject.view.AdViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private ImageView cursor = null, photo, sex;

	private PullToRefreshListView ptrlvHeadLineNews = null;
	private NewListAdapter newAdapter = null;

	private TextView sign, info, nick;

	private boolean log_database;

	private AdViewPager vpAdv = null;
	private ViewGroup vg = null;
	private ImageView[] imageViews = null;
	private List<View> advs = null;
	private int currentPage = 0;

	private static final int CHANGE_SIGN = 1;
	private static final int CHANGE_INFO = 2;

	private MyApplication myapp;

	private SlidingMenu slidingMenu = null;

	private DataBaseAdapter db = null;

	Bitmap bitmap;
	public static String Nick="";
	
	public boolean log_State;

	final Handler cwjHandler = new Handler();

	   final Runnable mUpdateResults = new Runnable() {
	        public void run() {
	            updateUI();
	        }

			private void updateUI() {
				// TODO Auto-generated method stub
				photo.setImageBitmap(bitmap);
				nick.setText(Nick);
			}
	    };
	    protected void NetworkOperation() {

	        Thread t = new Thread() {
	            public void run() {
	            	
					try {
						JSONObject j = new JSONObject(Util.obj);
						
						bitmap = getHttpBitmap(j.getString("figureurl_qq_2"));
						Nick=j.getString("nickname");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	                cwjHandler.post(mUpdateResults); //高速UI线程可以更新结果了
	            }
	        };
	        t.start();
	    }
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CheckData();
		db = new DataBaseAdapter(this);
		myapp = (MyApplication) getApplication();
		log_State = myapp.isLog();
		Toast.makeText(
				this,
				"log_state:" + log_State + ",myapp.log:" + myapp.isLog()
						+ ",UserName:" + myapp.getName(), Toast.LENGTH_LONG)
				.show();
		// 设置抽屉菜单
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单
		slidingMenu.setMenu(R.layout.slidingmenu_left);
		if (log_State) {
			slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right_havelogin);
		} else {
			slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
		}
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		// 将抽屉菜单与主页面关联起来
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		findViewById();
	}

	boolean CheckData() {
		try {
			DatabaseHelper.checkFile(DatabaseHelper.DB_PATH);
			DatabaseHelper.getDataBase(this);
			log_database = true;
		} catch (Exception ex) {
			log_database = false;
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("系统提示")
					// 设置对话框标题

					.setMessage("数据库不存在！")
					// 设置显示的内容

					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {// 添加确定按钮

								@Override
								public void onClick(DialogInterface dialog,
										int which) {// 确定按钮的响应事件
									finish();
									System.exit(0);
								}

							}).show();

		}
		return log_database;

	}

	void UserInfo(String Name) {
		info = (TextView) findViewById(R.id.info);
		sign = (TextView) findViewById(R.id.sign);
		photo = (ImageView) findViewById(R.id.user_photo);
		sex = (ImageView) findViewById(R.id.sex);
		nick = (TextView) findViewById(R.id.nick);
		findViewById(R.id.change).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.setting)).setOnClickListener(this);
		if (Name.equals("qq_User")) {
//				Log.v("main", Util.obj);
//				JSONObject j = new JSONObject(Util.obj);
//				nick.setText(j.getString("nickname"));
//				Bitmap bitmap = getHttpBitmap(j.getString("figureurl_qq_2"));
//				photo.setImageBitmap(bitmap);
				NetworkOperation();
				
		} else {
			User user = db.getUser(Name);
			if (user != null) {
				nick.setText(user.getNick());
				sign.setText(user.getSign());
				byte[] p = user.getPhoto();
				if (null != p && p.length > 0) {
					Bitmap photo = ImageTools.byteToBitmap(p);
					(this.photo).setImageBitmap(photo);
				}
				if (user.isSex())
					sex.setImageResource(R.drawable.sex_fmale);
				else
					sex.setImageResource(R.drawable.sex_male);
				info.setText(user.getInfo());
			}
		}
	}
	
	void ChangeImg(String url){
		Bitmap bitmap = getHttpBitmap(url);
		photo.setImageBitmap(bitmap);
	}
	
	/**
     * 获取网落图片资源 
     * @param url
     * @return
     */
	public static Bitmap getHttpBitmap(String url) {
	     URL myFileUrl = null;
	     Bitmap bitmap = null;
	     try {
	          myFileUrl = new URL(url);
	     } catch (MalformedURLException e) {
	          e.printStackTrace();
	     }
	     try {
	          HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	          conn.setConnectTimeout(0);
	          conn.setDoInput(true);
	          conn.connect();
	          InputStream is = conn.getInputStream();
	          bitmap = BitmapFactory.decodeStream(is);
	          is.close();
	     } catch (IOException e) {
	          e.printStackTrace();
	     }
	     return bitmap;
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
		views = new ArrayList<View>();
		views.add(LayoutInflater.from(this).inflate(R.layout.layout1, null));
		// views.add(LayoutInflater.from(this).inflate(R.layout.layout3, null));
		// views.add(LayoutInflater.from(this).inflate(R.layout.layout2, null));

		vpViewPager = (ViewPager) findViewById(R.id.vpViewPager1);
		vpViewPager.setAdapter(new MyPagerAdapter(views));
		vpViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		// initCursor(views.size());

		MyPagerAdapter myPagerAdapter = (MyPagerAdapter) vpViewPager
				.getAdapter();
		View v1 = myPagerAdapter.getItemAtPosition(0);
		// View v2 = myPagerAdapter.getItemAtPosition(1);
		// View v3 = myPagerAdapter.getItemAtPosition(2);
		ptrlvHeadLineNews = (PullToRefreshListView) v1
				.findViewById(R.id.ptrlvHeadLineNews);
		ptrlvHeadLineNews.setOnItemClickListener(new MyOnItemClick());
		// ptrlvEntertainmentNews = (PullToRefreshListView) v2
		// .findViewById(R.id.ptrlvEntertainmentNews);
		// ptrlvFinanceNews = (PullToRefreshListView) v3
		// .findViewById(R.id.ptrlvFinanceNews);
		newAdapter = new NewListAdapter(this, getSimulationNews(1));
		initPullToRefreshListView(ptrlvHeadLineNews, newAdapter);
		// initPullToRefreshListView(ptrlvEntertainmentNews, newAdapter);
		// initPullToRefreshListView(ptrlvFinanceNews, newAdapter);
		MyApplication myapp = (MyApplication) getApplication();
		if (log_State) {
			if (myapp.getName().equals("qq_User")) {
				//QQShareDemo qq = new QQShareDemo(MainActivity.this, this);
				//Log.v("main", "find1:"+Util.obj);
				//qq.getInfo();
				//qq.getInfo();
				UserInfo("qq_User");
				
				
			} else {
				UserInfo(myapp.getName());
			}
		} else
			((LinearLayout) findViewById(R.id.loginNow))
					.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == Constants.REQUEST_API) {
		// if (resultCode == Constants.RESULT_LOGIN) {
		// Tencent mTencent = Tencent.createInstance(QQShareDemo.APP_ID, this);
		// mTencent.handleLoginData(data, new BaseUiListener(this));
		// }
		// if(resultCode==Constants.REQUEST_API)
		// {
		//
		// }
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				init();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
		//Log.v("main", "ResultCode" + resultCode);

	}

	private void init() {
		log_State = myapp.isLog();
		//Log.v("main", Util.obj);
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
		Intent intent;
		switch (v.getId()) {
		case R.id.bNew:
			slidingMenu.showMenu();
			break;
		case R.id.loginNow:
			intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			this.startActivityForResult(intent, 100);
			// finish();
			// System.exit(0);
			break;
		// case R.id.tvTag1:
		// vpViewPager.setCurrentItem(0);
		// break;
		// case R.id.tvTag2:
		// vpViewPager.setCurrentItem(1);
		// break;
		// case R.id.tvTag3:
		// vpViewPager.setCurrentItem(2);
		case R.id.left_menu1:
			Log.d("main", "Menu1_Clicked");
			if (!log_State) {
				Toast.makeText(this, "请先登录~", Toast.LENGTH_LONG).show();
				intent = new Intent(this, LoginActivity.class);
				this.startActivity(intent);
				finish();
				System.exit(0);
			} else {
				intent = new Intent(this, SendDialogActivity.class);
				this.startActivity(intent);
				finish();
				System.exit(0);
			}

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
		case R.id.setting:
			break;
		case R.id.change:
			intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	/**
	 * 获取N条模拟的新闻数据 打包成ArrayList返回
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getSimulationNews(int n) {
		ArrayList<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
		ArrayList<Dialog> d = db.getDialog("", n);
		HashMap<String, Object> hm;
		for (Dialog dd : d) {
			hm = new HashMap<String, Object>();
			hm.put("uri",
					"http://images.china.cn/attachement/jpg/site1000/20131029/001fd04cfc4813d9af0118.jpg");
			Bitmap bit = ImageTools.byteToBitmap(dd.getImage());
			hm.put("img", bit);
			hm.put("title", dd.getTitle());
			hm.put("content", dd.getContent());
			hm.put("review", dd.getID());
			ret.add(hm);
		}
		// for (int i = 0; i < n; i++) {
		// hm = new HashMap<String, Object>();
		// if (i % 2 == 0) {
		// hm.put("uri",
		// "http://images.china.cn/attachement/jpg/site1000/20131029/001fd04cfc4813d9af0118.jpg");
		// } else {
		// hm.put("uri",
		// "http://photocdn.sohu.com/20131101/Img389373139.jpg");
		// }
		// hm.put("title", "国内成品油价两连跌几成定局");
		// hm.put("content", "国内成品油今日迎调价窗口，机构预计每升降价0.1元。");
		// hm.put("review", i + "跟帖");
		// // Bitmap b=null;
		// // hm.put("img", b);
		// ret.add(hm);
		// }
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

	class MyOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long arg3) {
			// System.out.println("你点击了" + (position + 1));
			ListView listView = (ListView) parent;
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) listView
					.getItemAtPosition(position);
			String ID = map.get("review");
			Dialog d = db.getDialogByID(ID);
			Intent it = new Intent(getBaseContext(), DialogActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", ID);
			// Intent it = new Intent(this, GanbuInfos.class);
			// Bundle bd = new Bundle();
			// bd.putString("PersonID", userid);
			it.putExtras(bundle);
			startActivity(it);

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
