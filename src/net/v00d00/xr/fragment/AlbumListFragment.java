package net.v00d00.xr.fragment;

import java.util.ArrayList;
import java.util.List;

import net.v00d00.xr.R;
import net.v00d00.xr.view.CoverView;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.AudioLibrary;
import org.xbmc.android.jsonrpc.api.model.AudioModel;
import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.api.model.ListModel;
import org.xbmc.android.jsonrpc.api.model.ListModel.Sort;
import org.xbmc.android.jsonrpc.api.model.ListModel.Sort.Method;
import org.xbmc.android.jsonrpc.api.model.ListModel.Sort.Order;
import org.xbmc.android.jsonrpc.io.ApiCallback;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class AlbumListFragment extends Fragment implements LoadableFragment {

	private GridView gridView;
	private ConnectionManager cm;
	private AlbumListAdapter adapter;
	private Provider provider;

	public interface Provider {
		public void showAlbumListing(AlbumDetail album);
	}

	public AlbumListFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		adapter = new AlbumListAdapter(getActivity(), new ArrayList<AlbumDetail>());

		View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
		gridView = (GridView) rootView.findViewById(R.id.album_grid);

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				AlbumDetail detail = adapter.getItem(pos);

				Context context = getActivity().getApplicationContext();
				String text = "Album Id: " + Integer.toString(detail.albumid);
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				provider.showAlbumListing(detail);
			}
		});
		load();
		return rootView;
	}

	@Override
	public void setConnectionManager(ConnectionManager cm) {
		this.cm = cm;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public void load() {
		ListModel.Sort sort = new Sort(true, Method.ALBUM, Order.ASCENDING);

		// create api call object
		final AudioLibrary.GetAlbums call = new AudioLibrary.GetAlbums(null, sort, (ListModel.AlbumFilter)null,
		        AudioModel.AlbumFields.TITLE,
		        AudioModel.AlbumFields.DISPLAYARTIST, // For the single album view
		        AudioModel.AlbumFields.YEAR,
		        AudioModel.AlbumFields.THUMBNAIL,
		        AudioModel.AlbumFields.ALBUMLABEL);

		// execute
		cm.call(call, new ApiCallback<AlbumDetail>() {
		    public void onResponse(final AbstractCall<AlbumDetail> call) {
		    	getActivity().runOnUiThread(new Runnable() {
		    		public void run() {
				    	adapter.clear();
				        adapter.addAll(call.getResults());
				        adapter.notifyDataSetChanged();
		    		}
		    	});
		    }
		    public void onError(int code, String message, String hint) {
		        Log.d("TEST", "Error " + code + ": " + message);
		    }
		});
	}

	private static class AlbumListAdapter extends ArrayAdapter<AlbumDetail> {
		public AlbumListAdapter(Context context, List<AlbumDetail> items) {
			super(context, 0, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CoverView view;
			if (convertView == null) {
				view = new CoverView(getContext());
			} else {
				view = (CoverView) convertView;
			}

			final AlbumDetail album = getItem(position);;
			view.setPosition(position);
			view.setTitle(album.title);
			view.setThumbnailPath(album.thumbnail);

			return view;
		}
	}



}