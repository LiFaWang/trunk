package net.huansi.hsgmtapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Tony on 2017/9/4.
 * 13:34
 */

public class Base64BitmapUtils {
    /*
*bitmap转base64
*/
    public static Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }catch (Exception e){
            return null;
        }

    }
}
