package galena.hats;

public class ConfigStorage {

    // TODO actually save to a config on disk
    private static HatType _type = HatType.BINOME;
    private static boolean _enabled = true;

    public static void setHatType(HatType value) {
        _type = value;
    }

    public static HatType getHatType() {
        return _type;
    }

    public static void setEnabled(boolean value) {
        _enabled = value;
    }

    public static boolean isEnabled() {
        return _enabled;
    }

}
