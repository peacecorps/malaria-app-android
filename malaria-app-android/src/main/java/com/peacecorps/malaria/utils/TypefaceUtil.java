package com.peacecorps.malaria.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Anamika Tripathi on 7/8/18.
 */
public class TypefaceUtil {

    /**
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "serif"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
           ToastLogSnackBarUtil.showErrorLog("Can't set custom font instead!");
        }
    }
}