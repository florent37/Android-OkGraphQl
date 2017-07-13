package com.github.florent37.okgraphql;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

/**
 * Find the application context, no need to use .init(this) from the extended Application
 */
public class AppContextFinder {
    private static Application APPLICATION;

    public static Context getContext(){
        if (APPLICATION == null) {
            findApplication();
        }
        return APPLICATION;
    }

    private static void findApplication(){
        try {
            final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method = activityThreadClass.getMethod("currentApplication");
            APPLICATION = (Application) method.invoke(null, (Object[]) null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
