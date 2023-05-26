package javabridge.lang;

import com.jesse205.module.gallerydrag.ClassManager;

public class ObjectBridge {
    protected final Object thisObj;
    protected final ClassManager classManager;

    public ObjectBridge(Object object, ClassManager classManager) {
        thisObj = object;
        this.classManager = classManager;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public Object getThisObj() {
        return thisObj;
    }
}
