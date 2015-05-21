package com.haoge.luanru.music.view;

import com.haoge.luanru.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class LoadListView extends ListView {
	// 底部布局
	View footer;

	public LoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 添加底部布局
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		this.addFooterView(footer);
	}

}
