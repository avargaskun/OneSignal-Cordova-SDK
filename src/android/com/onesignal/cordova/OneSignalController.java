package com.onesignal.cordova;

import com.onesignal.OneSignal;
import com.onesignal.Continue;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class OneSignalController {

  /**
   * Misc
   */
  public static void setLogLevel(JSONArray data) {
    try {
      int logLevel = data.getInt(0);
      int visualLevel = data.getInt(1);
      OneSignal.setLogLevel(logLevel, visualLevel);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  
  public static boolean setLanguage(JSONArray data) {
    try {
      OneSignal.getUser().setLanguage(data.getString(0));
      return true;
    }
    catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean login(JSONArray data) {
    try {
      String externalId = data.getString(0);
      OneSignal.login(externalId);
      return true;
    }
    catch (JSONException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean logout() {
    OneSignal.logout();
    return true;
  }

  public static boolean optIn() {
    OneSignal.getUser().getPushSubscription().optIn();
    return true;
  }

  public static boolean optOut() {
    OneSignal.getUser().getPushSubscription().optOut();
    return true;
  }

  public static boolean getId(CallbackContext callbackContext) {
    String pushId = OneSignal.getUser().getPushSubscription().getId();
    try {
      JSONObject subscriptionProperty = new JSONObject ();
      subscriptionProperty.put("value", pushId);

      CallbackHelper.callbackSuccess(callbackContext, subscriptionProperty);
    } catch (JSONException e){
      e.printStackTrace();
    }
    return true;
  }

  public static boolean getToken(CallbackContext callbackContext) {
    String token = OneSignal.getUser().getPushSubscription().getToken();
    try {
      JSONObject subscriptionProperty = new JSONObject ();
      subscriptionProperty.put("value", token);

      CallbackHelper.callbackSuccess(callbackContext, subscriptionProperty);
    } catch (JSONException e){
      e.printStackTrace();
    }
    return true;
  }
  
  public static boolean getOptedIn(CallbackContext callbackContext) {
    boolean optedIn = OneSignal.getUser().getPushSubscription().getOptedIn();
    try {
    JSONObject subscriptionProperty = new JSONObject ();
    subscriptionProperty.put("value", optedIn);

    CallbackHelper.callbackSuccess(callbackContext, subscriptionProperty);
  } catch (JSONException e){
    e.printStackTrace();
  }
  return true;
    
  }

  /** 
  * Aliases
  */

  public static boolean addAliases(JSONArray data) {
    try{
      JSONObject aliasObject = data.getJSONObject(0);
      Map<String, String> aliasesToAdd = new HashMap<>();
      Iterator<String> labels = aliasObject.keys();

      while (labels.hasNext()) {
          String label = labels.next();
          aliasesToAdd.put(label, aliasObject.getString(label));
      }
      
      OneSignal.getUser().addAliases(aliasesToAdd);
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean removeAliases(JSONArray data) {
    try {
      Collection<String> aliasesToRemove = new ArrayList<String>();
      
      for (int i = 0; i < data.length(); i++)
        aliasesToRemove.add(data.get(i).toString());
      
      OneSignal.getUser().removeAliases(aliasesToRemove);
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  /**
   * Tags
   */

  public static boolean addTags(JSONArray data) {
    try {
      JSONObject tagsObject = data.getJSONObject(0);
      Map<String, String> tagsToAdd = new HashMap<>();
      Iterator<String> keys = tagsObject.keys();

      while (keys.hasNext()) {
          String key = keys.next();
          tagsToAdd.put(key, tagsObject.get(key).toString());
      }
      
      OneSignal.getUser().addTags(tagsToAdd);
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean removeTags(JSONArray data) {
    try {
      Collection<String> list = new ArrayList<String>();
      for (int i = 0; i < data.length(); i++)
        list.add(data.get(i).toString());
      OneSignal.getUser().removeTags(list);
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  /**
   * Notifications
   */

  public static boolean clearAllNotifications() {
    try {
      OneSignal.getNotifications().clearAllNotifications();
      return true;
    }
    catch(Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean removeNotification(JSONArray data) {
    try {
      OneSignal.getNotifications().removeNotification(data.getInt(0));
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean removeGroupedNotifications(JSONArray data) {
    try {
      OneSignal.getNotifications().removeGroupedNotifications(data.getString(0));
      return true;
    } catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
  }

  public static boolean registerForProvisionalAuthorization() {
    // doesn't apply to Android
    return true;
  }

  public static boolean requestPermission(CallbackContext callbackContext, JSONArray data) {
    final CallbackContext jsPromptForPushNotificationsCallback = callbackContext;
    boolean fallbackToSettings = false;
    try {
      fallbackToSettings = data.getBoolean(0);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    OneSignal.getNotifications().requestPermission(fallbackToSettings, Continue.with(r -> {
      if (r.isSuccess()) {
        if (r.getData()) {
          Boolean didPermit = r.getData();
          CallbackHelper.callbackSuccessBoolean(callbackContext, didPermit);
        }
      }
    }));
    return true;
  }

  public static boolean getPermission(CallbackContext callbackContext) {
    boolean granted = OneSignal.getNotifications().getPermission();
    try {
      JSONObject permissionObj = new JSONObject ();
      permissionObj.put("value", granted);

      CallbackHelper.callbackSuccess(callbackContext, permissionObj);
    } catch (JSONException e){
      e.printStackTrace();
    }
    return true;
  }

  public static boolean canRequestPermission() {
      // doesn't apply to Android 5.0.0-beta1?
      return true;
  }

  public static boolean setLaunchURLsInApp() {
    // doesn't apply to Android
    return true;
  }

  /**
   * Privacy consent
   */
  public static boolean userProvidedConsent(CallbackContext callbackContext) {
    boolean providedConsent = OneSignal.userProvidedPrivacyConsent();
    CallbackHelper.callbackSuccessBoolean(callbackContext, providedConsent);
    return true;
  }

  public static boolean getRequiresPrivacyConsent(CallbackContext callbackContext) {
    boolean requiresUserConsent = OneSignal.getRequiresPrivacyConsent();
    CallbackHelper.callbackSuccessBoolean(callbackContext, requiresUserConsent);
    return true;
  }

   public static boolean getPrivacyConsent(CallbackContext callbackContext) {
    boolean getPrivacyConsent = OneSignal.getPrivacyConsent();
    CallbackHelper.callbackSuccessBoolean(callbackContext, getPrivacyConsent);
    return true;
  }

  public static boolean setRequiresPrivacyConsent(JSONArray data) {
    try {
      OneSignal.setRequiresPrivacyConsent(data.getBoolean(0));
      return true;
    } catch (JSONException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean setPrivacyConsent(JSONArray data) {
    try {
      OneSignal.setPrivacyConsent(data.getBoolean(0));
      return true;
    } catch (JSONException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Location
   */
  
  public static boolean requestLocationPermission(CallbackContext callbackContext) {
    OneSignal.getLocation().requestPermission(Continue.with(r -> {
      if (r.isSuccess()) {
        if (r.getData()) {
          Boolean didPermit = r.getData();
          CallbackHelper.callbackSuccessBoolean(callbackContext, didPermit);
        }
      }
    }));
    return true;
  }

  public static void setLocationShared(JSONArray data) {
    try {
      OneSignal.getLocation().setShared(data.getBoolean(0));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static boolean isLocationShared(CallbackContext callbackContext) {
    boolean isShared = OneSignal.getLocation().isShared();
    CallbackHelper.callbackSuccessBoolean(callbackContext, isShared);
    return true;
  }

  public static boolean enterLiveActivity() {
    // doesn't apply to Android
    return true;
  }

  public static boolean exitLiveActivity() {
    // doesn't apply to Android
    return true;
  }
}
