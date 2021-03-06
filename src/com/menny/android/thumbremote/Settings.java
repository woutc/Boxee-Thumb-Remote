/* The following code was written by Menny Even Danan
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.menny.android.thumbremote;


import java.net.InetAddress;

import com.menny.android.thumbremote.R;
import com.menny.android.thumbremote.boxee.BoxeeConnector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Settings {
	private static final String TAG = "Settings";
	
	public final String SERVER_NAME_KEY;
	private final String SERVER_VERSION_KEY;
	private final String SERVER_TYPE_KEY;
	private final String SERVER_HOST_KEY;
	private final String SERVER_PORT_KEY;
	private final String SERVER_PORT_DEFAULT_VALUE;
	private final String SERVER_CREDS_REQUIRED_KEY;
	private final String SERVER_USERNAME_KEY;
	private final String SERVER_PASSWORD_KEY;
	private final String SERVER_MANUALLY_SET_KEY;
	
	private final String VOLUME_STEP_SIZE_KEY;
	private final String VOLUME_STEP_SIZE_DEFAULT_VALUE;
	private final String SWIPE_SENSITIVITY_KEY;
	private final String SWIPE_SENSITIVITY_DEFAULT_VALUE;
	private final String HANDLE_HARD_BACK_KEY;
	private final boolean HANDLE_HARD_BACK_DEFAULT;
	private final String KEEP_SCREEN_ON_KEY;
	private final boolean KEEP_SCREEN_ON_DEFAULT;
	
	private final String NETWORK_TIMEOUT_KEY;
	private final String NETWORK_TIMEOUT_DEFAULT_VALUE;
	
	private SharedPreferences mPreferences;

	Settings(Context context) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		// Attempt to set default values if they have not yet been set
		PreferenceManager.setDefaultValues(context, R.layout.preferences, false);
		Resources res = context.getResources();
		
		VOLUME_STEP_SIZE_KEY = res.getString(R.string.settings_key_volume_step_size);
		VOLUME_STEP_SIZE_DEFAULT_VALUE = res.getString(R.string.settings_key_volume_step_size_default_value);
		
		SWIPE_SENSITIVITY_KEY = res.getString(R.string.settings_key_swipe_sensitivity_key);
		SWIPE_SENSITIVITY_DEFAULT_VALUE = res.getString(R.string.settings_key_swipe_sensitivity_default_value);
		
		HANDLE_HARD_BACK_KEY = res.getString(R.string.settings_key_handle_back_hard_key);
		HANDLE_HARD_BACK_DEFAULT = res.getBoolean(R.bool.settings_key_handle_back_hard_default);
		
		KEEP_SCREEN_ON_KEY = res.getString(R.string.settings_key_keep_screen_on_key);
		KEEP_SCREEN_ON_DEFAULT = res.getBoolean(R.bool.settings_key_keep_screen_on_default);
		
		SERVER_NAME_KEY = res.getString(R.string.settings_key_server_name_key);
		SERVER_TYPE_KEY = res.getString(R.string.settings_key_server_type_key);
		SERVER_VERSION_KEY = res.getString(R.string.settings_key_server_version_key);
		SERVER_HOST_KEY = res.getString(R.string.settings_key_server_host_key);
		SERVER_PORT_KEY = res.getString(R.string.settings_key_server_port_on_key);
		SERVER_PORT_DEFAULT_VALUE = res.getString(R.string.settings_key_server_port_on_default_value);
		SERVER_CREDS_REQUIRED_KEY = res.getString(R.string.settings_key_server_creds_required_key);
		SERVER_USERNAME_KEY = res.getString(R.string.settings_key_server_username_key);
		SERVER_PASSWORD_KEY = res.getString(R.string.settings_key_server_password_key);
		SERVER_MANUALLY_SET_KEY = res.getString(R.string.settings_key_server_manually_set_key);
		
		NETWORK_TIMEOUT_KEY = res.getString(R.string.settings_key_network_timeout_key);
		NETWORK_TIMEOUT_DEFAULT_VALUE = res.getString(R.string.settings_key_network_timeout_default_value);
	}

	public int getVolumeStep() {
		String volumeStep = mPreferences.getString(VOLUME_STEP_SIZE_KEY, VOLUME_STEP_SIZE_DEFAULT_VALUE);
		return Integer.parseInt(volumeStep);
	}
	
	public int getSwipeStepSize(Context appContext) {
		Resources res = appContext.getResources();
		String swipeStep = mPreferences.getString(SWIPE_SENSITIVITY_KEY, SWIPE_SENSITIVITY_DEFAULT_VALUE);
		if (swipeStep.equals("10dp"))
		{
			return res.getDimensionPixelSize(R.dimen.settings_key_swipe_sensitivity_value_10dp);
		}
		else if (swipeStep.equals("15dp"))
		{
			return res.getDimensionPixelSize(R.dimen.settings_key_swipe_sensitivity_value_15dp);
		}
		else if (swipeStep.equals("25dp"))
		{
			return res.getDimensionPixelSize(R.dimen.settings_key_swipe_sensitivity_value_25dp);
		}
		else if (swipeStep.equals("40dp"))
		{
			return res.getDimensionPixelSize(R.dimen.settings_key_swipe_sensitivity_value_40dp);
		}
		else 
		{
			return res.getDimensionPixelSize(R.dimen.settings_key_swipe_sensitivity_value_55dp);
		}
	}
	
	public boolean getHandleBack() {
		return mPreferences.getBoolean(HANDLE_HARD_BACK_KEY, HANDLE_HARD_BACK_DEFAULT);
	}
	
	public boolean getKeepScreenOn(){
		return mPreferences.getBoolean(KEEP_SCREEN_ON_KEY, KEEP_SCREEN_ON_DEFAULT);
	}
	
	public String getServerName() {
		return mPreferences.getString(SERVER_NAME_KEY, "");
	}
	
	public InetAddress getHost() {
		String address = mPreferences.getString(SERVER_HOST_KEY, "");
		try
		{
			if (TextUtils.isEmpty(address)) return null;
			return InetAddress.getByName(address);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public int getPort() {
		return Integer.parseInt(mPreferences.getString(SERVER_PORT_KEY, SERVER_PORT_DEFAULT_VALUE));
	}
	
	public String getUser() {
		return mPreferences.getString(SERVER_USERNAME_KEY, "");
	}
	
	public String getPassword() {
		return mPreferences.getString(SERVER_PASSWORD_KEY, "");
	}
	
	public boolean isManuallySetServer() {
		return mPreferences.getBoolean(SERVER_MANUALLY_SET_KEY, false);
	}
	
	public int getTimeout() {
		return Integer.parseInt(mPreferences.getString(NETWORK_TIMEOUT_KEY, NETWORK_TIMEOUT_DEFAULT_VALUE));
	}
	
	public boolean isAuthRequired() {
		return !TextUtils.isEmpty(getUser());
	}
	
	public ServerAddress constructServer() {
		return new ServerAddress(
				mPreferences.getString(SERVER_TYPE_KEY, BoxeeConnector.BOXEE_SERVER_TYPE),
				mPreferences.getString(SERVER_VERSION_KEY, BoxeeConnector.BOXEE_SERVER_VERSION_OLD), 
				getServerName(), isAuthRequired(), getHost(), getPort());
	}
	
	public void putServer(ServerAddress server, boolean isManual) {
		putServer(server.type(), server.version(), server.address().getHostAddress(), server.port(), server.name(), server.authRequired(), isManual);
	}
	
	public void putServer(String type, String version, String address, int port, String name, boolean auth, boolean isManual) {
		Log.i(TAG, "Storing server as: " + name + ", manual: " + isManual);
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(SERVER_HOST_KEY, address);
		editor.putString(SERVER_TYPE_KEY, type);
		editor.putString(SERVER_VERSION_KEY, version);
		editor.putInt(SERVER_PORT_KEY, port);
		editor.putString(SERVER_NAME_KEY, name);
		editor.putBoolean(SERVER_CREDS_REQUIRED_KEY, auth);
		editor.putBoolean(SERVER_MANUALLY_SET_KEY, isManual);
		editor.commit();	
	}
	
	public void listen(OnSharedPreferenceChangeListener listener) {
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unlisten(OnSharedPreferenceChangeListener listener) {
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
