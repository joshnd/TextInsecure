package org.thoughtcrime.securesms;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

import java.lang.reflect.Field;


public abstract class BaseActionBarActivity extends AppCompatActivity {
  private static final String TAG = BaseActionBarActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BaseActivity.isMenuWorkaroundRequired()) {
      forceOverflowMenu();
    }
    super.onCreate(savedInstanceState);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return (keyCode == KeyEvent.KEYCODE_MENU && BaseActivity.isMenuWorkaroundRequired()) || super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU && BaseActivity.isMenuWorkaroundRequired()) {
      openOptionsMenu();
      return true;
    }
    return super.onKeyUp(keyCode, event);
  }

  /**
   * Modified from: http://stackoverflow.com/a/13098824
   */
  private void forceOverflowMenu() {
    try {
      ViewConfiguration config       = ViewConfiguration.get(this);
      Field             menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
      if(menuKeyField != null) {
        menuKeyField.setAccessible(true);
        menuKeyField.setBoolean(config, false);
      }
    } catch (IllegalAccessException e) {
      Log.w(TAG, "Failed to force overflow menu.");
    } catch (NoSuchFieldException e) {
      Log.w(TAG, "Failed to force overflow menu.");
    }
  }

  protected void startActivitySceneTransition(Intent intent, View sharedView, String transitionName) {
    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, transitionName)
                                         .toBundle();
    ActivityCompat.startActivity(this, intent, bundle);
  }
}
