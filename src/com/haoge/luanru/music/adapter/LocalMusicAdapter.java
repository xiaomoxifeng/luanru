package com.haoge.luanru.music.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.haoge.luanru.R;
import com.haoge.luanru.music.adapter.MusicAdapter.ImageLoadTask;
import com.haoge.luanru.music.adapter.MusicAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.BitmapUtil;
import com.haoge.luanru.music.util.HttpUtil;
import com.haoge.luanru.music.util.MusicGlobalConsts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LocalMusicAdapter extends BaseAdapter {
	public static final int HANDLER_IMAGE_LOAD_SUCCESS_2 = 0;
	private Context context;
	private List<Music> musics;
	private LayoutInflater inflater;
	private ListView lv;
	// 声明任务队列
	private List<ImageLoadTask> tasks = new ArrayList<ImageLoadTask>();
	// 声明一个工作线程 用于批量加载图片
	private Thread workThread;
	private boolean isLoop = true;
	// 声明缓存区
	private Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
	// 声明Handler 设置Bitmap
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_IMAGE_LOAD_SUCCESS_2:
				// 获取ImageLoadTask
				ImageLoadTask task = (ImageLoadTask) msg.obj;
				// 给相应的ImageView设置Bitmap
				ImageView iv = (ImageView) lv.findViewWithTag(task.path);
				if (iv != null) {
					iv.setImageBitmap(task.bitmap);
				}
				break;
			}
		}
	};

	class ImageLoadTask {
		String path;
		Bitmap bitmap;
	}

	public LocalMusicAdapter(Context context, List<Music> musics, ListView lv) {
		this.context = context;
		this.musics = musics;
		this.lv = lv;
		this.inflater = LayoutInflater.from(context);
		workThread = new Thread() {
			public void run() {
				// 轮循任务队列
				while (isLoop) {
					if (!tasks.isEmpty()) {
						// 取出任务对象 执行图片下载任务
						ImageLoadTask task = tasks.remove(0);
						Bitmap bitmap = loadBitmap(task);
						// 设置到相应的ImageView中
						task.bitmap = bitmap;
						Message msg = new Message();
						msg.what = HANDLER_IMAGE_LOAD_SUCCESS_2;
						msg.obj = task;
						handler.sendMessage(msg);
					} else {
						// 工作线程等待
						synchronized (workThread) {
							try {
								workThread.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
		workThread.start();
	}

	public Bitmap loadBitmap(ImageLoadTask task) {
		// 先从缓存中获取 看有没有下载过
		SoftReference<Bitmap> ref = cache.get(task.path);
		if (ref != null && ref.get() != null) {
			Log.i("info", "从缓存中读取的图片" + task.path);
			return ref.get();
		}
		// 获取一张本地Bitmap
		try {
			// 调用工具类 获取一个50*50的Bitmap对象
			Bitmap bitmap = BitmapUtil.loadBitmap(task.path, 50, 50);
			// 把bitmap存入缓存
			cache.put(task.path, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_lv_music, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.ivAlbum);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvSinger = (TextView) convertView
					.findViewById(R.id.tvSinger);
			holder.tvAuthor = (TextView) convertView
					.findViewById(R.id.tvAuthor);
			holder.tvDuration = (TextView) convertView
					.findViewById(R.id.tvDuration);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		// 获取当前item 加载的音乐信息
		Music m = musics.get(position);
		holder.tvName.setText(m.getName());
		holder.tvSinger.setText(m.getSinger());
		holder.tvAuthor.setText(m.getAuthor());
		holder.tvDuration.setText(m.getDurationtime());
		// 设置imageView的tag 用于获取了bitmap后调用imageView.setImageBitmap()
		holder.ivPic.setTag(m.getL_albumpic());
		// 向任务队列中添加一个任务
		ImageLoadTask task = new ImageLoadTask();
		task.path = m.getL_albumpic();
		tasks.add(task);
		notifyWorkThread();
		return convertView;
	}

	class ViewHolder {
		ImageView ivPic;
		TextView tvName;
		TextView tvSinger;
		TextView tvAuthor;
		TextView tvDuration;
	}

	// 唤醒工作线程继续干活
	public void notifyWorkThread() {
		synchronized (workThread) {
			workThread.notify();
		}
	}

	// 停止工作线程 isLoop改为false
	public void stopWorkThread() {
		this.isLoop = false;
	}
}
