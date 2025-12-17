package id.komorebi.util;

import java.awt.Window;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WindowManager {
    private static final Set<Window> windows = Collections.synchronizedSet(new HashSet<>());

    public static void register(Window w) {
        if (w != null) windows.add(w);
    }

    public static void unregister(Window w) {
        if (w != null) windows.remove(w);
    }

    public static void closeAllExcept(Window keep) {
        synchronized (windows) {
            for (Window w : windows.toArray(new Window[0])) {
                if (w != null && w != keep) {
                    try {
                        w.dispose();
                    } catch (Exception ignored) {}
                    windows.remove(w);
                }
            }
        }
    }
}
