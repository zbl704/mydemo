package com.example.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;

public class ActivityAddFriends extends Activity implements SectionIndexer {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortGroupMemberAdapter adapter;
	private ClearEditText mClearEditText;

	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private List<GroupMemberBean> myPhotoBeans = new ArrayList<GroupMemberBean>(); ;
	
	private List<GroupMemberBean> groupMemberBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends);
		initViews();
		(new GetLetterList(this )).execute();
	}

	private void initViews() {
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		title = (TextView) this.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) this .findViewById(R.id.title_layout_no_friends);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

//		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						getApplication(),
						((GroupMemberBean) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

//		myPhotoBeans = filledData(getResources().getStringArray(R.array.date));
//		myPhotoBeans = filledData(myPhotoBeans);
		 myPhotoBeans = new ArrayList<GroupMemberBean>();
		 pinyinComparator = new PinyinComparator();
		 
//		 groupMemberBeans= myPhotoBeans;
		 //TODO
		 
		// 根据a-z进行排序源数据
		Collections.sort(myPhotoBeans, pinyinComparator);
		adapter = new SortGroupMemberAdapter(this, myPhotoBeans);
		sortListView.setAdapter(adapter);
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				try {
				
				int section = getSectionForPosition(firstVisibleItem);
				int nextSection = getSectionForPosition(firstVisibleItem + 1);
				int nextSecPosition = getPositionForSection(+nextSection);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout .getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(myPhotoBeans.get( getPositionForSection(section)).getSortLetters());
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout .getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
				
				} catch (Exception e) {
				}
			}
		});
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 这个时候不需要挤压效果 就把他隐藏掉
				titleLayout.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	//TODO
	private List<GroupMemberBean> filledData(List<GroupMemberBean> groupMemberBeans) {
		List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

		for (int i = 0; i < groupMemberBeans.size(); i++) {
			GroupMemberBean sortModel = new GroupMemberBean();
			sortModel.setName((groupMemberBeans.get(i).nickname));
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling((groupMemberBeans.get(i).nickname));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = myPhotoBeans;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (GroupMemberBean sortModel : myPhotoBeans) {
				try {
			
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
				
				} catch (Exception e) {
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return myPhotoBeans.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < myPhotoBeans.size(); i++) {
			String sortStr = myPhotoBeans.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	
	
	/** 异步任务 获取列表信息 */
	class GetLetterList extends AsyncTask<Void, Integer, Integer> {
		private Context context;
		private String flag;

		private List<GroupMemberBean> pullBeanUnXies;

		GetLetterList(Context context ) {
			this.context = context;
			pullBeanUnXies = new ArrayList<GroupMemberBean>();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Integer doInBackground(Void... params) {
			JSONObject jsonObject = JsonUtils.ReadHttpGet("http://192.168.1.114/letter/list");
			Integer result = 0;
			
			if (null != jsonObject) {
				try {
					flag = jsonObject.getString("flag").toString().trim();
					if (flag.equals("1")) {
						result = 1;
						JSONArray jsonArray = new JSONArray( jsonObject.getString("data"));
						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject temp = (JSONObject) jsonArray.get(i);
							
							GroupMemberBean bean = new GroupMemberBean();

							bean.setUid(temp.getString("uid")) ;
							bean.setNickname(temp.getString("nickname")) ;
							bean.setHeadpic(temp.getString("headpic"));
							bean.setProvince(temp.getString("province")) ;
							
							// 汉字转换成拼音
							String pinyin = characterParser.getSelling(bean.getNickname());
							String sortString = pinyin.substring(0, 1).toUpperCase();
							
							// 正则表达式，判断首字母是否是英文字母
							if (sortString.matches("[A-Z]")) {
								bean.setSortLetters(sortString.toUpperCase());
							} else {
								bean.setSortLetters("#");
							}
							pullBeanUnXies.add(bean);
							
						}
					} else {
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					result = 0;
				}
			}
			return result ;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// over do somethhing
			if (1 == result) {
				for (GroupMemberBean beanUnXy : pullBeanUnXies) {
					myPhotoBeans.add(beanUnXy);
				}
			}else {
				Toast.makeText(context, "初始化失败！", Toast.LENGTH_SHORT).show();
			}
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}
}
