package vn.edu.tdtu.anhminh.myapplication.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NetworkUtils {
    private NetworkUtils(){

    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return false;
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiConnected(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null){
            return false;
        }
        NetworkInfo networkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isMobileConnected(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null){
            return false;
        }
        NetworkInfo networkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getYouTubeId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        if (url.contains("youtu.be/")) {
            Pattern pattern = Pattern.compile("youtu\\.be/([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        if (url.contains("youtube.com/watch")) {
            Pattern pattern = Pattern.compile("[?&]v=([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        if (url.contains("youtube.com/embed/")) {
            Pattern pattern = Pattern.compile("embed/([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

}
