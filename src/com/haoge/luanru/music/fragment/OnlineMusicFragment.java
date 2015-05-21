package com.haoge.luanru.music.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.music.activity.MusicMainActivity;
import com.haoge.luanru.music.activity.PlayMusicActivity;
import com.haoge.luanru.music.adapter.MusicAdapter4;
import com.haoge.luanru.music.dao.BmobMusicDao;
import com.haoge.luanru.music.dao.MusicDao;
import com.haoge.luanru.music.dao.MusicDaoImp;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.service.MusicDownloadService;
import com.haoge.luanru.music.service.PictureDownloadService;
import com.haoge.luanru.music.util.BroadcastActions;
import com.haoge.luanru.music.util.MusicGlobalConsts;

public class OnlineMusicFragment extends Fragment implements BroadcastActions,MusicFragment {
	private ListView lvMusics;
	private MusicAdapter4 adapter;
	private List<Music> musics;
	public static boolean PICFALG = false;
	public static boolean MUSICFALG = false;
	private MusicDao musicDao;
	private boolean isFirst =true;
	private BmobMusicDao mBmobMusicDao;
	private static int currentMusicPosition;
	private Music m;
	private LuanruApplication app;
	private MusicMainActivity mainActivity;
	private Intent mIntent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_music_online, null);
		lvMusics = (ListView) v.findViewById(R.id.lv_music_online);
		musicDao = new MusicDaoImp(getActivity());
		mainActivity=(MusicMainActivity) getActivity();
		//初始化Bmob
		Bmob.initialize(getActivity(), MusicGlobalConsts.BMOB_APPID);
		refresh();
		initReceiver();
		app=(LuanruApplication) getActivity().getApplication();
		// 发送请求 执行异步任务
		//new MusicBiz(this).execute();
		lvMusics.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("iv_play");
				mainActivity.getCurrentMusicPosition(position);
				mIntent = new Intent(ACTVITY_ITEM);
				mIntent.putExtra("OnLineMusic", position);
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
				b.setItems(new String[] { "下载", "播放" }, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 执行下载操作 启动service
						if (which == 0) {
						String musicAlbumpic = m.getAlbumpic().getFileUrl(getContext());
						String albumpicName=m.getAlbumpic().getFilename();
						String musicpath = m.getMusicpath().getFileUrl(getContext());	
						String fileName=m.getMusicpath().getFilename();
						Intent i = new Intent(getActivity(),
									MusicDownloadService.class);
						Intent i2 = new Intent(getActivity(),
									PictureDownloadService.class);
							i.putExtra("fileName", fileName);
							i.putExtra("path", musicpath);
							i2.putExtra("path",musicAlbumpic);
							i2.putExtra("fileName",albumpicName);
							getActivity().startService(i);
							getActivity().startService(i2);
						} else if (which == 1) {
							Intent i = new Intent(getActivity(),
									PlayMusicActivity.class);
							i.putExtra("OnLineMusic", position);
							currentMusicPosition=position;
							startActivity(i);
						}
					}
				});
				b.create().show();
				return false;
			}

		});
		return v;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void refresh() {
		mBmobMusicDao= new BmobMusicDao(OnlineMusicFragment.this);
		mBmobMusicDao.findAllMusic();
	}

	// 更新ListView 设置Adapter
	public void updateListView(List<Music> result) {
		this.musics = result;
		setApp();
		System.out.println("updateListView____________DaoBiz");
		System.out.println(result.size()+"updateListView");
		//Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
		adapter = new MusicAdapter4(getActivity(), result, lvMusics);
		//可以设置每个item的触发事件
//		adapter.setOnInViewClickListener(R.id.ivAlbum, 		new MusicAdapter3.onInternalClickListener() {
//			@Override
//			public void OnClickListener(View parentV, View v,
//					Integer position, Object values) {
//				Toast.makeText(getActivity(), "这个是图片", Toast.LENGTH_LONG).show();
//			}
//		});
//		adapter.setOnInViewClickListener(R.id.tvName, 		new MusicAdapter3.onInternalClickListener() {
//			@Override
//			public void OnClickListener(View parentV, View v,
//					Integer position, Object values) {
//				Toast.makeText(getActivity(), "这个是歌曲名称", Toast.LENGTH_LONG).show();
//			}
//		});
		lvMusics.setAdapter(adapter);
		if(isFirst){
			getActivity().sendBroadcast(new Intent().setAction(ACTVITY_SET_LOCAL_APP));	
			System.out.println("getActivity().sendBroadcast(new Intent().setAction(ACTVITY_SET_LOCAL_APP));	");
			isFirst =false;
		}
	}
	private void setApp() {
		System.out.println("	app.setmMusicFragment(OnlineMusicFragment.this);_______");
		MusicMainActivity mma=(MusicMainActivity) getActivity();
		mma.getMusics(musics);
		app.setmMusicFragment(OnlineMusicFragment.this);
	}

	/**
	 * 注册广播接收者
	 */
	private void initReceiver() {
		InnerReceiver receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SERVICE_DOWN_MUSIC);
		filter.addAction(SERVICE_DOWN_PIC);
		filter.addAction(REFRESH_ONLINE_MUSICS);
		filter.addAction(ACTVITY_SET_ONLINE_APP);
		getActivity().registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (SERVICE_DOWN_PIC.equals(intent.getAction())) {
				PICFALG = true;
				if (MUSICFALG == true) {
					this.inserDB();
				}else{
					return;
				}
			}else if (SERVICE_DOWN_MUSIC.equals(intent.getAction())) {

				MUSICFALG = true;
				if (PICFALG == true) {
					this.inserDB();
				}else{
					return;
				}
			}else if (REFRESH_ONLINE_MUSICS.equals(intent.getAction())){
				refresh();
			}else if(ACTVITY_SET_ONLINE_APP.equals(intent.getAction())){
				System.out.println(ACTVITY_SET_ONLINE_APP+"_______");
				setApp();
			}
		}

		/**
		 * 插入数据库
		 */
		private void inserDB() {
			long i = musicDao.addMusic(m);
			if (i != -1) {
				Toast.makeText(getActivity(), "插入成功", Toast.LENGTH_SHORT)
						.show();
				// TODO Auto-generated method stub
				getActivity().sendBroadcast(
						new Intent().setAction(REFRESH_LOCAL_MUSICS));

			} else {
				Toast.makeText(getActivity(), "插入失败", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return getActivity();
	}

	@Override
	public List<Music> getMusics() {
		// TODO Auto-generated method stub
	 return musics;
	}
//	@Override
//	public int getCurrentMusicPosition() {
//		// TODO Auto-generated method stub
//		return currentMusicPosition;
//	}

}
