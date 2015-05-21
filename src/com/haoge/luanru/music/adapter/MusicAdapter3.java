package com.haoge.luanru.music.adapter;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.haoge.luanru.R;
import com.haoge.luanru.baseAdapter.BaseListAdapter2;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.BitmapUtil;
import com.haoge.luanru.music.util.HttpUtil;
public class MusicAdapter3 extends BaseListAdapter2 {
	public static final int HANDLER_IMAGE_LOAD_SUCCESS = 0;
	private LayoutInflater mInflater;
	private ListView lv;
	private Context mContext;
	// 声明任务队列
	private List<ImageLoadTask> tasks = new ArrayList<ImageLoadTask>();
	// 声明一个工作线程 用于批量加载图片
	private Thread workThread;
	private boolean isLoop = true;
	//新优化
	private LruCache<String, Bitmap> mMemoryCache;  
	// 声明缓存区
	//private Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
	// 声明Handler 设置Bitmap
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_IMAGE_LOAD_SUCCESS:
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
	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
	public MusicAdapter3(Context context, List<Music> list,ListView lv) {
		super(context, list);
		mContext= context;
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// 使用最大可用内存值的1/8作为缓存的大小。
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}
		};
	//	    
		this.lv = lv;
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
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
						msg.what = HANDLER_IMAGE_LOAD_SUCCESS;
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
	//先从缓存中获取 看有没有下载过
	public Bitmap loadBitmap(ImageLoadTask task) {
		// 先从缓存中获取 看有没有下载过
		//SoftReference<Bitmap> ref = cache.get(task.path);
		Bitmap ref1= mMemoryCache.get(task.path);
		if (ref1 != null ) {
			Log.i("info", "从缓存中读取的图片" + task.path);
			return  getBitmapFromMemoryCache(task.path);
		}
		String path =  task.path;
		// 发送http请求 获取一张Bitmap
		HttpResponse resp;
		try {
			resp = HttpUtil.send(HttpUtil.METHOD_GET, path, null);
			HttpEntity entity = resp.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			// 调用工具类 获取一个50*50的Bitmap对象
			Bitmap bitmap = BitmapUtil.loadBitmap(bytes, 50, 50);
			// 把bitmap存入缓存
			//cache.put(task.path, new SoftReference<Bitmap>(bitmap));
			addBitmapToMemoryCache(task.path, bitmap);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_lv_music,null);
		}
		ImageView ivAlbum = ViewHolder.get(convertView,R.id.ivAlbum);
		TextView tvName =ViewHolder.get(convertView,R.id.tvName);
		//tvName.setText(text);
		TextView tvSinger =ViewHolder.get(convertView,R.id.tvSinger);
		TextView tvAuthor =ViewHolder.get(convertView,R.id.tvAuthor);
		TextView tvDuration =ViewHolder.get(convertView,R.id.tvDuration);
		Music m = (Music) mlist.get(position);
		tvName.setText(m.getName());
		tvAuthor.setText(m.getAuthor());
		tvSinger.setText(m.getSinger());
		tvDuration.setText(m.getDurationtime());
		// 设置imageView的tag 用于获取了bitmap后调用imageView.setImageBitmap()
		ivAlbum.setTag(m.getAlbumpic().getFileUrl(mContext));
		// 向任务队列中添加一个任务
		ImageLoadTask task = new ImageLoadTask();
		task.path = m.getAlbumpic().getFileUrl(mContext);
		tasks.add(task);
		notifyWorkThread();
		
		return convertView;
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
