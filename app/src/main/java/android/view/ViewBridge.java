package android.view;

import android.annotation.SuppressLint;
import android.os.Build;

import com.jesse205.module.gallerydrag.ClassManager;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javabridge.lang.ObjectBridge;

public class ViewBridge extends ObjectBridge {

    private final View thisObj;
    private Method getListenerInfo;
    private ListenerInfoBridge listenerInfoBridge;

    public ViewBridge(View view, ClassManager classManager) throws NoSuchMethodException {
        super(view, classManager);
        thisObj = view;
    }

    @SuppressLint("DiscouragedPrivateApi")
    public Object getListenerInfo() throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return HiddenApiBypass.invoke(View.class, thisObj, "getListenerInfo");
        } else {
            if (getListenerInfo == null) {
                getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
                getListenerInfo.setAccessible(true);
            }
            return getListenerInfo.invoke(thisObj);
        }
    }

    public ListenerInfoBridge getListenerInfoBridge() throws Throwable {
        if (listenerInfoBridge == null)
            listenerInfoBridge = new ListenerInfoBridge(getListenerInfo(), classManager);
        return listenerInfoBridge;
    }

    public static class ListenerInfoBridge {
        private final Object thisObj;
        private Field mOnLongClickListenerField;
        private final Class<?> clazz;

        public ListenerInfoBridge(Object listenerInfo, ClassManager classManager) throws ClassNotFoundException {
            thisObj = listenerInfo;
            clazz = classManager.getClass("android.view.View$ListenerInfo");
        }

        public View.OnLongClickListener getOnLongClickListener() throws Throwable {
            if (mOnLongClickListenerField == null) {
                mOnLongClickListenerField = clazz.getDeclaredField("mOnLongClickListener");
                mOnLongClickListenerField.setAccessible(true);
            }
            return (View.OnLongClickListener) mOnLongClickListenerField.get(thisObj);
        }
    }
}
