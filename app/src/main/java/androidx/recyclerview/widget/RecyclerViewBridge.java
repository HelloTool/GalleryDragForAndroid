package androidx.recyclerview.widget;

import android.view.View;

import com.jesse205.module.gallerydrag.ClassManager;

import java.io.File;
import java.lang.reflect.Field;

import javabridge.lang.ObjectBridge;

public class RecyclerViewBridge {


    public static class ViewHolderBridge extends ObjectBridge {
        private final Class clazz;
        private Field itemViewField;

        public ViewHolderBridge(Object viewHolder, ClassManager classManager) throws ClassNotFoundException {
            super(viewHolder, classManager);
            clazz = classManager.getClass("androidx.recyclerview.widget.RecyclerView$ViewHolder");
        }

        public View getItemView() throws Throwable {
            if (itemViewField == null) {
                itemViewField = clazz.getField("itemView");
            }
            return (View) itemViewField.get(thisObj);
        }

    }
}
