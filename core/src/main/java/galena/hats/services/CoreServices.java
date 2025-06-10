package galena.hats.services;

import java.util.ServiceLoader;

public class CoreServices {

    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, CLASS_LOADER)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

}