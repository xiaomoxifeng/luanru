package com.haoge.luanru.music.adapter;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.baseAdapter.BaseListAdapter2;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.ImageDownLoader;
import com.haoge.luanru.music.util.ImageDownLoader.onImageLoaderListener;

public class MusicAdapter5 extends BaseListAdapter2 {
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LayoutInflater mInflater;
	private AbsListView mAbsView;
	private Context mContext;
	private List<Music> mls;
	private LuanruApplication app;
	private OnlineMusicFragment mOnlineMusicFragment;
	/**
	 * 第一张可见图片的下标
	 */
	private int mFirstVisibleItem;
	/**
	 * 一屏有多少张图片可见
	 */
	private int mVisibleItemCount;
	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 */
	private boolean isFirstEnter = true;
	/**
	 * Image 下载器
	 */
	private ImageDownLoader mImageDownLoader;

	public MusicAdapter5(Context context, List<Music> list,AbsListView lbs) {
		super(context, list);
		//app=(LuanruApplication) context.getActivity().getApplication();
		//context.getA
		//(Activity)context.;
//		if(context instanceof OnlineMusicFragment){
//			//mMusicFragment= (MusicFragment) context;
//		System.out.println("context_____________________________ instanceof MusicFragment");
//		}
		//if(context instanceof OnlineMusicFragment){
		//mOnlineMusicFragment=(mOnlineMusicFragment)context;
		//}
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mImageDownLoader = new ImageDownLoader(context);
		this.mls = list;
		this.mAbsView=lbs;
		this.mAbsView.setOnScrollListener(new ScrollListener());
	}

	// 监听下滑事件
	public final class ScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 仅当ListView静止时才去下载图片，ListView滑动时取消所有正在下载的任务
			if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
				showImage(mFirstVisibleItem, mVisibleItemCount);
			}else{
				cancelTask();
			}
		}
		/**
		 * 滚动的时候调用的方法，刚开始显示也会调用此方法
		 */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;
			// 因此在这里为首次进入程序开启下载任务。
			if (isFirstEnter && visibleItemCount > 0) {
				showImage(mFirstVisibleItem, mVisibleItemCount);
				isFirstEnter = false;
			}
		}

		/**
		 * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
		 * 
		 * @param mFirstVisibleItem
		 * @param mVisibleItemCount
		 */
		private void showImage(int firstVisibleItem, int visibleItemCount) {
			// TODO Auto-generated method stub
			Bitmap bitmap = null;
			for (int i = firstVisibleItem; i < firstVisibleItem+visibleItemCount; i++) {
				String url = mls.get(i).getAlbumpic().getFileUrl(mContext);
				final ImageView mImageView = (ImageView) mAbsView
						.findViewWithTag(url);
				bitmap = mImageDownLoader.downloadImage(url,
						new onImageLoaderListener() {
							@Override
							public void onImageLoader(Bitmap bitmap, String url) {
								// TODO Auto-generated method stub
								if (mImageView != null && bitmap != null) {
									mImageView.setImageBitmap(bitmap);
								}
							}
						});
				if (bitmap != null) {
					mImageView.setImageBitmap(bitmap);
				} else {
					mImageView.setImageResource(R.drawable.ic_launcher);
				}
			}
		}
	};


	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_lv_music, null);
		}
		ImageView ivAlbum = ViewHolder.get(convertView, R.id.ivAlbum);
		TextView tvName = ViewHolder.get(convertView, R.id.tvName);
		// tvName.setText(text);
		TextView tvSinger = ViewHolder.get(convertView, R.id.tvSinger);
		TextView tvAuthor = ViewHolder.get(convertView, R.id.tvAuthor);
		TextView tvDuration = ViewHolder.get(convertView, R.id.tvDuration);
		Music m = (Music) mlist.get(position);
		tvName.setText(m.getName());
		tvAuthor.setText(m.getAuthor());
		tvSinger.setText(m.getSinger());
		tvDuration.setText(m.getDurationtime());
		// 设置imageView的tag 用于获取了bitmap后调用imageView.setImageBitmap()
		ivAlbum.setTag(m.getAlbumpic().getFileUrl(mContext));
		// setImageView(m.getAlbumpic(), ivAlbum);
		/******************************* 去掉下面这几行试试是什么效果 ****************************/
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(m.getAlbumpic()
				.getFileUrl(mContext).replaceAll("[^\\w]", ""));
		if (bitmap != null) {
			ivAlbum.setImageBitmap(bitmap);
		} else {
			ivAlbum.setImageResource(R.drawable.ic_launcher);
		}
		/**********************************************************************************/
		return convertView;
	}
	/**
	 * 取消下载任务
	 */
	public void cancelTask(){
		mImageDownLoader.cancelTask();
	}

}
