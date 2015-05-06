package ind.demo.icondydemo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public final class IcondyUtil {
	public static final String ICONDY_PACKAGE = "ind.fem.black.rayyan.blendicons";
	public static final String ICONDY_ACTIVITY = "ind.fem.black.rayyan.blendicons.api.IcondyActivity";
	public static final String ACTION_ICONPACK_LIST = ICONDY_PACKAGE + ".ACTION_ICONPACK_LIST";
	public static final String ACTION_ICONPACK = ICONDY_PACKAGE + ".ACTION_ICONPACK";
	
	public static final String EXTRA_FOR_ICONPACK_LIST = "iconPackList";
	public static final String EXTRA_FOR_ICONPACK = "iconPack";
	public static final String EXTRA_FOR_FILEPATH = "filePath";
	
	public static final int REQUEST_CODE_FOR_ICONPACK_LIST = 100;
	public static final int REQUEST_CODE_FOR_ICONPACK = 200;
	
	
	/**
	 * Filter the calendar icons based on the component name.
	 * 
	 * Eg component name =
	 * "com.google.android.calendar/com.android.calendar.AllInOneActivity"
	 * 
	 * @param List<String> original list contains all the calendar icons names like
	 *         com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_1, com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_2 .... up to
	 *         com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_31
	 *         am.sunrise.android.calendar/am.sunrise.android.calendar.ui.HomeActivity|calendar_sunrise_1, am.sunrise.android.calendar/am.sunrise.android.calendar.ui.HomeActivity|calendar_sunrise_2 up to
	 *         am.sunrise.android.calendar/am.sunrise.android.calendar.ui.HomeActivity|calendar_sunrise_31
	 * @param componentName
	 *            component name should be one of the calendar application.
	 * @return List<String> return list contains calendar icons based on the
	 *         component name like 
	 *         com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_1, com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_2 .... up to
	 *         com.google.android.calendar/com.android.calendar.AllInOneActivity|calendar_31
	 */
	
	public static List<String> filterCalenderIcons(
			List<String> originalList, final String componentName) {
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.addAll(originalList);
		Collections.sort(resultList);

		filter(resultList, new Predicate<String>() {
			@Override
			public boolean evaluate(String input) {
				return input.startsWith(componentName);
			}
		});
		return resultList;
	}

	/**
     * Read image from the given url and send it back to the caller 
     *
     * @param url  stored image url
     * @param Resources 
     * @return Drawable if the image is available same will be returned.
     */
	public static Drawable getImageBitmap(String url, Resources resources) {
		Bitmap bm = null;
		try {
			URL aURL = new URL("file://" + url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Drawable draw = new BitmapDrawable(resources, bm);
		return draw;
	}
	
	public static List<ResolveInfo> getCalendars(PackageManager packageManager) {
        Intent calendarIntent = new Intent(Intent.ACTION_MAIN);
        calendarIntent.addCategory("android.intent.category.LAUNCHER");
        calendarIntent.addCategory("android.intent.category.APP_CALENDAR");
        return packageManager.queryIntentActivities(calendarIntent, PackageManager.GET_META_DATA);
    }
	
	
	/**
     * Filter the collection by applying a Predicate to each element. If the
     * predicate returns false, remove the element.
     * <p>
     * If the input collection or predicate is null, there is no change made.
     *
     * @param <T>  the type of object the {@link Iterable} contains
     * @param collection  the collection to get the input from, may be null
     * @param predicate  the predicate to use as a filter, may be null
     * @return true if the collection is modified by this call, false otherwise.
     */
    public static <T> boolean filter(final Iterable<T> collection, final Predicate<? super T> predicate) {
        boolean result = false;
        if (collection != null && predicate != null) {
            for (final Iterator<T> it = collection.iterator(); it.hasNext();) {
                if (!predicate.evaluate(it.next())) {
                    it.remove();
                    result = true;
                }
            }
        }
        return result;
    }
    
    /**
     * Defines a functor interface implemented by classes that perform a predicate
     * test on an object.
     * <p>
     * A <code>Predicate</code> is the object equivalent of an <code>if</code> statement.
     * It uses the input object to return a true or false value, and is often used in
     * validation or filtering.
     * <p>
     * Standard implementations of common predicates are provided by
     * {@link PredicateUtils}. These include true, false, instanceof, equals, and,
     * or, not, method invokation and null testing.
     *
     * @param <T> the type that the predicate queries
     *
     * @since 1.0
     * @version $Id: Predicate.java 1543262 2013-11-19 00:47:45Z ggregory $
     */
    public interface Predicate<T> {

        /**
         * Use the specified parameter to perform a test that returns true or false.
         *
         * @param object  the object to evaluate, should not be changed
         * @return true or false
         * @throws ClassCastException (runtime) if the input is the wrong class
         * @throws IllegalArgumentException (runtime) if the input is invalid
         * @throws FunctorException (runtime) if the predicate encounters a problem
         */
        boolean evaluate(T object);

    }
}
