package code.namanbir.lab.app;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.adobe.air.InstallOfferPingUtils;
import com.adobe.air.ResourceIdMap;
import com.savegame.SavesRestoringPortable;
import dalvik.system.DexClassLoader;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Date;

public class AppEntry extends Activity {
//  private static String AIR_PING_URL;
  ""

  private static final String LOG_TAG = "AppEntry";
  
  private static final String RESOURCE_BUTTON_EXIT = "string.button_exit";
  
  private static final String RESOURCE_BUTTON_INSTALL = "string.button_install";
  
  private static final String RESOURCE_CLASS = "code.namanbir.lab.app.R";
  
  private static final String RESOURCE_TEXT_RUNTIME_REQUIRED = "string.text_runtime_required";
  
  private static final String RESOURCE_TITLE_ADOBE_AIR = "string.title_adobe_air";
  
  private static String RUNTIME_PACKAGE_ID;
  
  private static Object sAndroidActivityWrapper;
  
  private static Class<?> sAndroidActivityWrapperClass;
  
  private static DexClassLoader sDloader;
  
  private static boolean sRuntimeClassesLoaded = false;
  
  private static AppEntry sThis;
  
  static {
    sAndroidActivityWrapper = null;
    sThis = null;
    RUNTIME_PACKAGE_ID = "com.adobe.air";
    AIR_PING_URL = "https://airdownload2.adobe.com/air?";
  }
  
  private void BroadcastIntent(String paramString1, String paramString2) {
    try {
      startActivity(Intent.parseUri(paramString2, 0).setAction(paramString1).addFlags(268435456));
      return;
    } catch (URISyntaxException uRISyntaxException) {
      return;
    } catch (ActivityNotFoundException activityNotFoundException) {
      return;
    } 
  }
  
  private Object InvokeMethod(Method paramMethod, Object... paramVarArgs) {
    if (!sRuntimeClassesLoaded)
      return null; 
    if (paramVarArgs != null)
      try {
        return paramMethod.invoke(sAndroidActivityWrapper, paramVarArgs);
      } catch (Exception exception) {
        return null;
      }  
    return exception.invoke(sAndroidActivityWrapper, new Object[0]);
  }
  
  private void InvokeWrapperOnCreate() {
    try {
      Method method = sAndroidActivityWrapperClass.getMethod("onCreate", new Class[] { Activity.class, String[].class });
      Boolean bool1 = new Boolean(false);
      SavesRestoringPortable.DoSmth((Context)this);
      Boolean bool2 = new Boolean(false);
      InvokeMethod(method, new Object[] { this, { "", "", "-nodebug", bool1.toString(), bool2.toString() } });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  private static void KillSelf() {
    Process.killProcess(Process.myPid());
  }
  
  private void createActivityWrapper(boolean paramBoolean) {
    if (paramBoolean)
      try {
        sAndroidActivityWrapper = sAndroidActivityWrapperClass.getMethod("CreateAndroidActivityWrapper", new Class[] { Activity.class, Boolean.class }).invoke(null, new Object[] { this, Boolean.valueOf(paramBoolean) });
        return;
      } catch (Exception exception) {
        return;
      }  
    sAndroidActivityWrapper = sAndroidActivityWrapperClass.getMethod("CreateAndroidActivityWrapper", new Class[] { Activity.class }).invoke(null, new Object[] { this });
  }
  
  private boolean isRuntimeInstalled() {
    PackageManager packageManager = getPackageManager();
    try {
      packageManager.getPackageInfo(RUNTIME_PACKAGE_ID, 256);
      return true;
    } catch (PackageManager.NameNotFoundException nameNotFoundException) {
      return false;
    } 
  }
  
  private boolean isRuntimeOnExternalStorage() {
    PackageManager packageManager = getPackageManager();
    try {
      int i = (packageManager.getApplicationInfo(RUNTIME_PACKAGE_ID, 8192)).flags;
      if ((i & 0x40000) == 262144)
        return true;
    } catch (PackageManager.NameNotFoundException nameNotFoundException) {}
    return false;
  }
  
  private void launchAIRService() {
    try {
      Intent intent = new Intent("com.adobe.air.AIRServiceAction");
      intent.setClassName(RUNTIME_PACKAGE_ID, "com.adobe.air.AIRService");
      bindService(intent, new ServiceConnection() {
            public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
              AppEntry.this.unbindService(this);
              AppEntry.this.loadSharedRuntimeDex();
              AppEntry.this.createActivityWrapper(false);
              if (AppEntry.sRuntimeClassesLoaded) {
                AppEntry.this.InvokeWrapperOnCreate();
                return;
              } 
              AppEntry.KillSelf();
            }
            
            public void onServiceDisconnected(ComponentName param1ComponentName) {}
          },  1);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  private void launchMarketPlaceForAIR() {
    String str1;
    String str2 = null;
    try {
      Bundle bundle = (getPackageManager().getActivityInfo(getComponentName(), 128)).metaData;
      str1 = str2;
      if (bundle != null)
        str1 = (String)bundle.get("airDownloadURL"); 
    } catch (PackageManager.NameNotFoundException nameNotFoundException) {
      str1 = str2;
    } 
    str2 = str1;
    if (str1 == null)
      str2 = "market://details?id=" + RUNTIME_PACKAGE_ID; 
    try {
      BroadcastIntent("android.intent.action.VIEW", str2);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  private boolean loadCaptiveRuntimeClasses() {
    boolean bool = false;
    try {
      sAndroidActivityWrapperClass = Class.forName("com.adobe.air.AndroidActivityWrapper");
      boolean bool1 = true;
      bool = bool1;
      if (sAndroidActivityWrapperClass != null) {
        bool = bool1;
        sRuntimeClassesLoaded = true;
      } 
      return true;
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  private void loadSharedRuntimeDex() {
    try {
      if (!sRuntimeClassesLoaded) {
        Context context = createPackageContext(RUNTIME_PACKAGE_ID, 3);
        sDloader = new DexClassLoader(RUNTIME_PACKAGE_ID, getFilesDir().getAbsolutePath(), null, context.getClassLoader());
        sAndroidActivityWrapperClass = sDloader.loadClass("com.adobe.air.AndroidActivityWrapper");
        if (sAndroidActivityWrapperClass != null)
          sRuntimeClassesLoaded = true; 
      } 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  private void registerForNotifications() {
    Intent intent = new Intent("com.adobe.air.AndroidGcmRegistrationService");
    intent.setClassName(RUNTIME_PACKAGE_ID, "com.adobe.air.AndroidGcmRegistrationService");
    startService(intent);
  }
  
  private void showDialog(int paramInt1, String paramString, int paramInt2, int paramInt3) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setTitle(paramInt1);
    builder.setMessage(paramString);
    builder.setPositiveButton(paramInt2, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            AppEntry.this.launchMarketPlaceForAIR();
            InstallOfferPingUtils.PingAndExit(AppEntry.sThis, AppEntry.AIR_PING_URL, true, false, true);
          }
        });
    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
          public void onCancel(DialogInterface param1DialogInterface) {
            InstallOfferPingUtils.PingAndExit(AppEntry.sThis, AppEntry.AIR_PING_URL, false, false, true);
          }
        });
    builder.show();
  }
  
  private void showRuntimeNotInstalledDialog() {
    ResourceIdMap resourceIdMap = new ResourceIdMap("code.namanbir.lab.app.R");
    String str = getString(resourceIdMap.getId("string.text_runtime_required")) + getString(resourceIdMap.getId("string.text_install_runtime"));
    showDialog(resourceIdMap.getId("string.title_adobe_air"), str, resourceIdMap.getId("string.button_install"), resourceIdMap.getId("string.button_exit"));
  }
  
  private void showRuntimeOnExternalStorageDialog() {
    ResourceIdMap resourceIdMap = new ResourceIdMap("code.namanbir.lab.app.R");
    String str = getString(resourceIdMap.getId("string.text_runtime_required")) + getString(resourceIdMap.getId("string.text_runtime_on_external_storage"));
    showDialog(resourceIdMap.getId("string.title_adobe_air"), str, resourceIdMap.getId("string.button_install"), resourceIdMap.getId("string.button_exit"));
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent) {
    boolean bool = false;
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("dispatchGenericMotionEvent", new Class[] { MotionEvent.class, boolean.class }), new Object[] { paramMotionEvent, Boolean.valueOf(false) });
    } catch (Exception exception) {}
    if (false || super.dispatchGenericMotionEvent(paramMotionEvent))
      bool = true; 
    return bool;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool = false;
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("dispatchKeyEvent", new Class[] { KeyEvent.class, boolean.class }), new Object[] { paramKeyEvent, Boolean.valueOf(false) });
    } catch (Exception exception) {}
    if (false || super.dispatchKeyEvent(paramKeyEvent))
      bool = true; 
    return bool;
  }
  
  public void finishActivityFromChild(Activity paramActivity, int paramInt) {
    super.finishActivityFromChild(paramActivity, paramInt);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("finishActivityFromChild", new Class[] { Activity.class, int.class }), new Object[] { paramActivity, Integer.valueOf(paramInt) });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void finishFromChild(Activity paramActivity) {
    super.finishFromChild(paramActivity);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("finishFromChild", new Class[] { Activity.class }), new Object[] { paramActivity });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    try {
      if (sRuntimeClassesLoaded)
        InvokeMethod(sAndroidActivityWrapperClass.getMethod("onActivityResult", new Class[] { int.class, int.class, Intent.class }), new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramIntent }); 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onApplyThemeResource(Resources.Theme paramTheme, int paramInt, boolean paramBoolean) {
    super.onApplyThemeResource(paramTheme, paramInt, paramBoolean);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onApplyThemeResource", new Class[] { Resources.Theme.class, int.class, boolean.class }), new Object[] { paramTheme, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onAttachedToWindow", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onBackPressed() {
    super.onBackPressed();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onBackPressed", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onChildTitleChanged(Activity paramActivity, CharSequence paramCharSequence) {
    super.onChildTitleChanged(paramActivity, paramCharSequence);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onChildTitleChanged", new Class[] { Activity.class, CharSequence.class }), new Object[] { paramActivity, paramCharSequence });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onConfigurationChanged", new Class[] { Configuration.class }), new Object[] { paramConfiguration });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onContentChanged() {
    super.onContentChanged();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onContentChanged", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onContextItemSelected(MenuItem paramMenuItem) {
    boolean bool = super.onContextItemSelected(paramMenuItem);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onContextItemSelected", new Class[] { MenuItem.class, boolean.class }), new Object[] { paramMenuItem, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onContextMenuClosed(Menu paramMenu) {
    super.onContextMenuClosed(paramMenu);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onContextMenuClosed", new Class[] { Menu.class }), new Object[] { paramMenu });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    sThis = this;
    long l = (new Date()).getTime();
    Log.i("StartupTime1", ":" + l);
    boolean bool = loadCaptiveRuntimeClasses();
    if (!bool) {
      if (!sRuntimeClassesLoaded && !isRuntimeInstalled()) {
        if (isRuntimeOnExternalStorage()) {
          showRuntimeOnExternalStorageDialog();
          return;
        } 
        showRuntimeNotInstalledDialog();
        return;
      } 
      loadSharedRuntimeDex();
    } 
    if (sRuntimeClassesLoaded) {
      createActivityWrapper(bool);
      InvokeWrapperOnCreate();
      return;
    } 
    if (bool) {
      KillSelf();
      return;
    } 
    launchAIRService();
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {
    super.onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateContextMenu", new Class[] { ContextMenu.class, View.class, ContextMenu.ContextMenuInfo.class }), new Object[] { paramContextMenu, paramView, paramContextMenuInfo });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public CharSequence onCreateDescription() {
    CharSequence charSequence = super.onCreateDescription();
    try {
      return (CharSequence)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateDescription", new Class[] { CharSequence.class }), new Object[] { charSequence });
    } catch (Exception exception) {
      return charSequence;
    } 
  }
  
  protected Dialog onCreateDialog(int paramInt) {
    Dialog dialog = super.onCreateDialog(paramInt);
    try {
      return (Dialog)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateDialog", new Class[] { int.class, Dialog.class }), new Object[] { Integer.valueOf(paramInt), dialog });
    } catch (Exception exception) {
      return dialog;
    } 
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    Dialog dialog = super.onCreateDialog(paramInt, paramBundle);
    try {
      return (Dialog)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateDialog", new Class[] { int.class, Bundle.class, Dialog.class }), new Object[] { Integer.valueOf(paramInt), paramBundle, dialog });
    } catch (Exception exception) {
      return dialog;
    } 
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    boolean bool = super.onCreateOptionsMenu(paramMenu);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateOptionsMenu", new Class[] { Menu.class, boolean.class }), new Object[] { paramMenu, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu) {
    boolean bool = super.onCreatePanelMenu(paramInt, paramMenu);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreatePanelMenu", new Class[] { int.class, Menu.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramMenu, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public View onCreatePanelView(int paramInt) {
    View view = super.onCreatePanelView(paramInt);
    try {
      return (View)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreatePanelView", new Class[] { int.class, View.class }), new Object[] { Integer.valueOf(paramInt), view });
    } catch (Exception exception) {
      return view;
    } 
  }
  
  public boolean onCreateThumbnail(Bitmap paramBitmap, Canvas paramCanvas) {
    boolean bool = super.onCreateThumbnail(paramBitmap, paramCanvas);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateThumbnail", new Class[] { Bitmap.class, Canvas.class, boolean.class }), new Object[] { paramBitmap, paramCanvas, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    View view = super.onCreateView(paramString, paramContext, paramAttributeSet);
    try {
      return (View)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onCreateView", new Class[] { String.class, Context.class, AttributeSet.class, View.class }), new Object[] { paramString, paramContext, paramAttributeSet, view });
    } catch (Exception exception) {
      return view;
    } 
  }
  
  public void onDestroy() {
    super.onDestroy();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onDestroy", new Class[0]), new Object[0]);
    } catch (Exception exception) {}
    sThis = null;
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onDetachedFromWindow", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = super.onKeyDown(paramInt, paramKeyEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onKeyDown", new Class[] { int.class, KeyEvent.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramKeyEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = super.onKeyLongPress(paramInt, paramKeyEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onKeyLongPress", new Class[] { int.class, KeyEvent.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramKeyEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent) {
    boolean bool = super.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onKeyMultiple", new Class[] { int.class, int.class, KeyEvent.class, boolean.class }), new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramKeyEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = super.onKeyUp(paramInt, paramKeyEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onKeyUp", new Class[] { int.class, KeyEvent.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramKeyEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onLowMemory() {
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onLowMemory", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem) {
    boolean bool = super.onMenuItemSelected(paramInt, paramMenuItem);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onMenuItemSelected", new Class[] { int.class, MenuItem.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramMenuItem, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onMenuOpened(int paramInt, Menu paramMenu) {
    boolean bool = super.onMenuOpened(paramInt, paramMenu);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onMenuOpened", new Class[] { int.class, Menu.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramMenu, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  protected void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onNewIntent", new Class[] { Intent.class }), new Object[] { paramIntent });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    boolean bool = super.onOptionsItemSelected(paramMenuItem);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onOptionsItemSelected", new Class[] { MenuItem.class, boolean.class }), new Object[] { paramMenuItem, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onOptionsMenuClosed(Menu paramMenu) {
    super.onOptionsMenuClosed(paramMenu);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onOptionsMenuClosed", new Class[] { Menu.class }), new Object[] { paramMenu });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu) {
    super.onPanelClosed(paramInt, paramMenu);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPanelClosed", new Class[] { int.class, Menu.class }), new Object[] { Integer.valueOf(paramInt), paramMenu });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onPause() {
    super.onPause();
    try {
      if (sRuntimeClassesLoaded)
        InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPause", new Class[0]), new Object[0]); 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onPostCreate(Bundle paramBundle) {
    super.onPostCreate(paramBundle);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPostCreate", new Class[] { Bundle.class }), new Object[] { paramBundle });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onPostResume() {
    super.onPostResume();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPostResume", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onPrepareDialog(int paramInt, Dialog paramDialog) {
    super.onPrepareDialog(paramInt, paramDialog);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPrepareDialog", new Class[] { R.id.class, Dialog.class }), new Object[] { Integer.valueOf(paramInt), paramDialog });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle) {
    super.onPrepareDialog(paramInt, paramDialog, paramBundle);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPrepareDialog", new Class[] { R.id.class, Dialog.class, Bundle.class }), new Object[] { Integer.valueOf(paramInt), paramDialog, paramBundle });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu) {
    boolean bool = super.onPrepareOptionsMenu(paramMenu);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPrepareOptionsMenu", new Class[] { Menu.class, boolean.class }), new Object[] { paramMenu, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu) {
    boolean bool = super.onPreparePanel(paramInt, paramView, paramMenu);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onPreparePanel", new Class[] { int.class, View.class, Menu.class, boolean.class }), new Object[] { Integer.valueOf(paramInt), paramView, paramMenu, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onRestart() {
    super.onRestart();
    try {
      if (sRuntimeClassesLoaded)
        InvokeMethod(sAndroidActivityWrapperClass.getMethod("onRestart", new Class[0]), new Object[0]); 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle) {
    super.onRestoreInstanceState(paramBundle);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onRestoreInstanceState", new Class[] { Bundle.class }), new Object[] { paramBundle });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onResume() {
    super.onResume();
    try {
      if (sRuntimeClassesLoaded)
        InvokeMethod(sAndroidActivityWrapperClass.getMethod("onResume", new Class[0]), new Object[0]); 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public Object onRetainNonConfigurationInstance() {
    Object object = super.onRetainNonConfigurationInstance();
    try {
      return InvokeMethod(sAndroidActivityWrapperClass.getMethod("onRetainNonConfigurationInstance", new Class[] { Object.class }), new Object[] { object });
    } catch (Exception exception) {
      return object;
    } 
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onSaveInstanceState", new Class[] { Bundle.class }), new Object[] { paramBundle });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onSearchRequested() {
    boolean bool = super.onSearchRequested();
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onSearchRequested", new Class[] { boolean.class }), new Object[] { Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onStart() {
    super.onStart();
  }
  
  public void onStop() {
    super.onStop();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onStop", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onTitleChanged(CharSequence paramCharSequence, int paramInt) {
    super.onTitleChanged(paramCharSequence, paramInt);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onTitleChanged", new Class[] { CharSequence.class, int.class }), new Object[] { paramCharSequence, Integer.valueOf(paramInt) });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onTouchEvent", new Class[] { MotionEvent.class, boolean.class }), new Object[] { paramMotionEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent) {
    boolean bool = super.onTrackballEvent(paramMotionEvent);
    try {
      return ((Boolean)InvokeMethod(sAndroidActivityWrapperClass.getMethod("onTrackballEvent", new Class[] { MotionEvent.class, boolean.class }), new Object[] { paramMotionEvent, Boolean.valueOf(bool) })).booleanValue();
    } catch (Exception exception) {
      return bool;
    } 
  }
  
  public void onUserInteraction() {
    super.onUserInteraction();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onUserInteraction", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  protected void onUserLeaveHint() {
    super.onUserLeaveHint();
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onUserLeaveHint", new Class[0]), new Object[0]);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams) {
    super.onWindowAttributesChanged(paramLayoutParams);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onWindowAttributesChanged", new Class[] { WindowManager.LayoutParams.class }), new Object[] { paramLayoutParams });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    super.onWindowFocusChanged(paramBoolean);
    try {
      InvokeMethod(sAndroidActivityWrapperClass.getMethod("onWindowFocusChanged", new Class[] { boolean.class }), new Object[] { Boolean.valueOf(paramBoolean) });
      return;
    } catch (Exception exception) {
      return;
    } 
  }
}


/* Location:              C:\Users\User\OneDrive\Desktop\Test\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\code\namanbir\lab\app\AppEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */