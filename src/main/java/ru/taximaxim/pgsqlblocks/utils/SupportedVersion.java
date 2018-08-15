package ru.taximaxim.pgsqlblocks.utils;


import java.util.*;

public enum SupportedVersion {
    VERSION_9_2 (90200, "9.2", "9.2"),
    VERSION_9_3 (90300, "9.3", "9.3"),
    VERSION_9_4 (90400, "9.4", "9.4"),
    VERSION_9_5 (90500, "9.5", "9.5"),
    VERSION_9_6 (90600, "9.6", "9.6"),
    VERSION_10 (100000, "10.0", "10.0"),
    VERSION_DEFAULT(100000, "10.0", "");

    private static final Map<String, SupportedVersion> lookup = new HashMap<>();
    private final String version;
    private final int versionNumber;
    private final String versionText;

    static {
        for (SupportedVersion s : SupportedVersion.values()) {
            if (!s.equals(VERSION_DEFAULT)) {
                lookup.put(s.getVersion(), s);
            }
        }
    }

    SupportedVersion(int versionNumber, String version, String versionText) {
        this.versionNumber = versionNumber;
        this.version = version;
        this.versionText = versionText;
    }

    public static List<SupportedVersion> getValuesAdd() {
        List<SupportedVersion> list = new ArrayList<>(Arrays.asList(SupportedVersion.values()));
        list.remove(SupportedVersion.VERSION_DEFAULT);
        return list;
    }

    public String getVersion() {
        return version;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getVersionText() {
        return versionText;
    }

    //а если в енаме значения нет???? todo
    public static SupportedVersion get(String text){
        return lookup.get(text);
    }

    public static SupportedVersion get(int number){
        List<SupportedVersion> set = SupportedVersion.getValuesAdd();
        for (int i = set.size() - 1; i >= 0; i--) {
            SupportedVersion verEnum = set.get(i);
            if (verEnum.checkVersion(number)) {
                return verEnum;
            }
        }
        return SupportedVersion.VERSION_DEFAULT;
    }

    private boolean checkVersion(int number) {
        return number >= this.versionNumber;
    }
}
