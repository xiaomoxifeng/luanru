package com.haoge.luanru.music.fragment;

import java.util.List;

import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.music.activity.MusicMainActivity;
import com.haoge.luanru.music.activity.PlayMusicActivity;
import com.haoge.luanru.music.adapter.LocalMusicAdapter;
import com.haoge.luanru.music.biz.QueryLocalBiz;
import com.haoge.luanru.music.dao.MusicDao;
import com.haoge.luanru.music.dao.MusicDaoImp;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.service.DeleteFileService;
import com.haoge.luanru.music.util.BroadcastActions;
import com.haoge.luanru.music.view.LoadListView.ILoadListener;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class LocalMusicFragment extends Fragment implements BroadcastActions,MusicFragment,ILoadListener {
	private ListView lvMusics;
	private  List<Music> musics;
	private LocalMusicAdapter adapter;
	private MusicDao musicDao;
	private ILoadMusicListenter mLoadMusicListener;
	public LocalMusicFragment LocalMusicFragment() {
		return LocalMusicFragment.this;
	}
	private Music m;
	private LuanruApplication app;
	private Intent mIntent;
	private static int currentMusicPosition;
	//private ILoadMusicListener mLoadMusicListener;

	public  List<Music> getMusics() {
		return musics;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_music_local, null);
		lvMusics = (ListView) v.findViewById(R.id.lv_music_local);
		musicDao = new MusicDaoImp(getActivity());
		//lvMusics = (SlideCutListView) findViewById(R.id.slideCutListView);

		DaoBiz();
		initReceiver();
		app=(LuanruApplication) getActivity().getApplication();
		lvMusics.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("iv_play");
				//mLoadMusicListener.onLoadMusic(position);
				mIntent = new Intent(ACTVITY_ITEM);
				mIntent.putExtra("Loaclmusic", position);
				getActivity().sendBroadcast(mIntent);
				getActivity().sendBroadcast(new Intent().setAction(SERVICE_REFRESH));
			}
		
		});
		lvMusics.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				m = musics.get(position);
				AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
				b.setItems(new String[] { "播放", "删除" }, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 执行下载操作 启动service
						if (which == 0) {
							Intent i = new Intent(getActivity(),
									PlayMusicActivity.class);
							Bundle mBundle = new Bundle();
							//mBundle.putSerializable("Loaclmusic", m);
							i.putExtra("Loaclmusic", position);
							
							i.putExtras(mBundle);
							startActivity(i);
						} else if (which == 1) {
							Intent i = new Intent(getActivity(),
									DeleteFileService.class);
							Bundle mBundle = new Bundle();
							mBundle.putSerializable("m", m);
							i.putExtras(mBundle);
							getActivity().startService(i);
						}
					}
				});
				b.create().show();
				return false;
			}

		});
		return v;
	}

	/**
	 * 注册广播接收者
	 */
	private void initReceiver() {
		InnerReceiver receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(REFRESH_LOCAL_MUSICS);
		filter.addAction(SERVICE_DELETE_FILE);
		filter.addAction(ACTVITY_SET_LOCAL_APP);
		getActivity().registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (REFRESH_LOCAL_MUSICS.equals(intent.getAction())) {
				// 异步刷新
				DaoBiz();
			} else if (SERVICE_DELETE_FILE.equals(intent.getAction())) {
				int i = musicDao.deleteMusic(m);
				if (i != -1) {
					Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT)
							.show();
					// 异步刷新
					DaoBiz();
				}
			}else if(ACTVITY_SET_LOCAL_APP.equals(intent.getAction())){
				System.out.println("ACTVITY_SET_LOCAL_APP");
				setApp();
			}
		}
	}

	/**
	 * 异步刷新
	 */
	private void DaoBiz() {
		new QueryLocalBiz(this).execute();
	}

	// 更新ListView 设置Adapter
	public void updateListView(List<Music> result) {
		// musics = daobiz.getMusicList();
		this.musics = result;
		System.out.println("updateListView____________DaoBiz");
		
		setApp();
		adapter = new LocalMusicAdapter(getActivity(), result, lvMusics);
		lvMusics.setAdapter(adapter);
	}

	private void setApp() {
		System.out.println("app.setmMusicFragment(LocalMusicFragment.this);______");
		//mLoadMusicListener.onLoadMusics(musics);
		app.setmMusicFragment(LocalMusicFragment.this);
	}
	

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return getActivity();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	public Fragment setMusicInterface(ILoadMusicListenter iLoadMusicListener){
		this.mLoadMusicListener = iLoadMusicListener;
		return this;
	}
//	//回调接口
//	public interface ILoadMusicListener{
//		public void onLoadMusic(int position);
//		public void onLoadMusics(List<Music> musics);
//	}
//
//	@Override
//	public void onLoad() {
//		// TODO Auto-generated method stub
//		
//	}

	
}
