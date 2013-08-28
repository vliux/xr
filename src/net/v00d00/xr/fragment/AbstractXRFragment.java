/*

XR - Modern Android XBMC remote
Copyright (C) 2013 Ian Whyman

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package net.v00d00.xr.fragment;

import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class AbstractXRFragment extends Fragment {

	private ConnectionManager cm;

	public interface ConnectionManagerProvider {
		ConnectionManager getConnectionManager();
	}

	public ConnectionManager getConnectionManager() {
		return getConnectionManager(null);
	}

	protected ConnectionManager getConnectionManager(Activity activity) {
		if (cm == null) {
			if (activity == null)
				activity = getActivity();
			if (activity != null) {
				if (activity instanceof ConnectionManagerProvider)
					cm = ((ConnectionManagerProvider) activity).getConnectionManager();
				else
					throw new ClassCastException(activity.toString() + " does not implement ConnectionManagerProvider");
			}
		}
		return cm;
	}

	protected abstract void load();
	public abstract CharSequence getTitle();

	@Override
	public void onStart() {
		super.onStart();
		load();
	}

}
