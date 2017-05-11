package com.xz.activeplan.ui.news.fragment;

import java.util.List;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.news.adapter.MessageAdapter;
import com.xz.activeplan.utils.DensityUtil;
import com.xz.activeplan.utils.UIUtil;
import com.xz.activeplan.views.swipemenulistview.SwipeMenu;
import com.xz.activeplan.views.swipemenulistview.SwipeMenuCreator;
import com.xz.activeplan.views.swipemenulistview.SwipeMenuItem;
import com.xz.activeplan.views.swipemenulistview.SwipeMenuListView;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 消息fragment界面
 * 
 * @author johnny
 *
 */
public class NewsFragment extends BaseFragment {
	private View mView;// 处理整个页面的view
	private SwipeMenuListView mSwipeMenuListView;

	private List<ApplicationInfo> mAppList;
	private MessageAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_message, container, false);
			initViews();
//		} else {
//			ViewGroup vg = (ViewGroup) mView.getParent();
//			if (vg != null) {
//				vg.removeView(mView);
//			}
//		}
		return mView;
	}

	private void initViews() {
		mSwipeMenuListView = (SwipeMenuListView) mView.findViewById(R.id.id_XListview);
		
		
		mAppList = getActivity().getPackageManager().getInstalledApplications(0);

		mAdapter = new MessageAdapter(getActivity(),mAppList);
        mSwipeMenuListView.setAdapter(mAdapter);
        
        

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
            	createMenu(menu);
//                switch (menu.getViewType()) {
//                    case 0:
//                        createMenu1(menu);
//                        break;
//                    case 1:
//                        
//                        break;
//                    case 2:
//                        createMenu3(menu);
//                        break;
//                }
            }

           

            private void createMenu(SwipeMenu menu) {
                
                SwipeMenuItem item = new SwipeMenuItem(
                		getActivity().getApplicationContext());
                item.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item.setWidth(DensityUtil.dip2px(getActivity(),90));
                item.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(item);
            }
        };
        // set creator
        mSwipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();

                return false;
            }
        });
        
        mSwipeMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
				UIUtil.showToast(getActivity(), "" + position);
			}
		});

	}



	

	@Override
	public void onBackPressed() {

	}

}
