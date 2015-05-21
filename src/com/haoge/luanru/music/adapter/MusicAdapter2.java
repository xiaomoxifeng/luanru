package com.haoge.luanru.music.adapter;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.haoge.luanru.R;
import com.haoge.luanru.baseAdapter.BaseListAdapter;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.BitmapUtil;
import com.haoge.luanru.music.util.HttpUtil;
import com.haoge.luanru.music.util.MusicGlobalConsts;
public class MusicAdapter2<E> extends BaseListAdapter<Music> {
	private Context mContext;
	public MusicAdapter2(Context context, List<Music> list, AbsListView lv) {
		super(context, list, lv);
	}
	@Override
	public Bitmap bindbitmap(ImageLoadTask task) {
		// TODO Auto-generated method stub
		String path = MusicGlobalConsts.BASEURL + task.path;
		// 发送http请求 获取一张Bitmap
		HttpResponse resp;
		try {
			resp = HttpUtil.send(HttpUtil.METHOD_GET, path, null);
			HttpEntity entity = resp.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			// 调用工具类 获取一个50*50的Bitmap对象
			Bitmap bitmap = BitmapUtil.loadBitmap(bytes, 50, 50);
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
		Music m = mlist.get(position);
		tvName.setText(m.getName());
		tvAuthor.setText(m.getAuthor());
		tvSinger.setText(m.getSinger());
		tvDuration.setText(m.getDurationtime());
		// 设置imageView的tag 用于获取了bitmap后调用imageView.setImageBitmap()
		ivAlbum.setTag(m.getAlbumpic());
		// 向任务队列中添加一个任务
		ImageLoadTask task = new ImageLoadTask();
		task.path = m.getAlbumpic().getFileUrl(mContext);
		tasks.add(task);
		notifyWorkThread();
		return null;
	}
}
