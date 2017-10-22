
public class Lang {
    public static String languageGetter(String lang) {
        switch (lang) {
            case "english":
                return "en";
            case "russian":
                return "ru";
            case "spanish":
                return "es";
            case "estonian":
                return "et";
            case "greek":
                return "el";
            case "german":
                return "de";
            default:
                return "ups";
        }
    }
}
