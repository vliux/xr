package net.v00d00.xr.fragment;

import java.util.ArrayList;

import net.v00d00.xr.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TitlePageIndicator;


public class MusicFragment extends Fragment {

	XRPagerAdapter adapter = null;
	ViewPager pager;
	TitlePageIndicator indicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pager, container, false);

		pager = (ViewPager) view.findViewById(R.id.pager);
		indicator = (TitlePageIndicator) view.findViewById(R.id.titles);

		if (adapter == null) {
			adapter = new XRPagerAdapter(getChildFragmentManager());
			ArrayList<AbstractXRFragment> fl = new ArrayList<AbstractXRFragment>();

			AlbumListFragment albumListFragment = new AlbumListFragment();
			fl.add(albumListFragment);

			ArtistListFragment artistListFragment = new ArtistListFragment();
			fl.add(artistListFragment);

			SongListFragment songListFragment = new SongListFragment();
			fl.add(songListFragment);

			adapter.setFragmentList(fl);
		}
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		return view;
	}
}
