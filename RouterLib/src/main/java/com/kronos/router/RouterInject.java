package com.kronos.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by zhangyang on 16/7/18.
 */
public class RouterInject {
    public static void Inject(FragmentActivity activity, Intent intent) {
        Bundle bundle = intent.getExtras();
        String target = bundle != null ? bundle.getString("target") : null;
        IActivityInject inject = getActivityInject(activity);
        if (inject == null) {
            return;
        }
        inject.Inject(bundle);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment == null) {
                continue;
            }
            IFragmentInject fragmentInject = Inject(fragment, target);
            if (fragmentInject != null) {
                fragmentInject.Inject(bundle);
                break;
            }
        }
    }

    public static void Inject(Fragment fragment, Bundle bundle) {
        String target = bundle != null ? bundle.getString("target") : null;
        IFragmentInject inject = deal(fragment, target);
        if (inject != null) {
            inject.Inject(bundle);
        }
    }

    private static IFragmentInject Inject(Fragment fragment, String target) {
        return deal(fragment, target);
    }

    private static IFragmentInject deal(Object object, String target) {
        if (TextUtils.equals(object.getClass().getName(), target)) {
            return getFragmentInject(object);
        }
        return null;
    }

    private static IActivityInject getActivityInject(Object object) {
        if (object instanceof IActivityInject) {
            return (IActivityInject) object;
        } else {
            return null;
        }
    }

    private static IFragmentInject getFragmentInject(Object object) {
        if (object instanceof IFragmentInject) {
            return (IFragmentInject) object;
        } else {
            return null;
        }
    }

}
