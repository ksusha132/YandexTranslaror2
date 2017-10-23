
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.*;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        System.out.println("Input the language you want to translate from:");
        BufferedReader formLang = new BufferedReader(new InputStreamReader(System.in));
        String from = formLang.readLine();

        System.out.println("Input the language you want to translate to:");
        BufferedReader toLang = new BufferedReader(new InputStreamReader(System.in));
        String to = toLang.readLine();

        URLConnection urlConnection = urlCreater(from, to);

        System.out.println("Input the word you want to translate:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String text = bufferedReader.readLine();

        String stringToReverse = URLEncoder.encode(text, "UTF-8");
        System.out.println(translatedTextWriter(urlConnection, stringToReverse));
    }


    public static String createURL(String langFrom, String langTo, String key) throws URISyntaxException, UnsupportedEncodingException {
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost("translate.yandex.net")
                .setPath("/api/v1.5/tr.json/translate")
                .addParameter("lang", langFrom + "-" + langTo)
                .addParameter("key", key);
        URI uri = builder.build();
        return uri.toString();
    }

    public static String jsonParser(String str) {
        JsonParser parser = new JsonParser();
        JsonObject mainObject = (JsonObject) parser.parse(str);
        JsonArray result = mainObject.getAsJsonArray("text");
        String res = result.toString().substring(2, result.toString().length() - 2);
        return res;
    }

    public static URLConnection urlCreater(String from, String to) throws IOException, URISyntaxException {
        URL url = new URL(createURL(Lang.languageGetter(from), Lang.languageGetter(to), Const.KEY));
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true); // flag to true if you intend to use the URL connection for output
        return urlConnection;
    }

    public static String translatedTextWriter(URLConnection urlConnection, String stringToReverse) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        String text = null;
        if (stringToReverse.isEmpty()) {
            out.close();
            return "Nothing to send";
        }

        out.write("text=" + stringToReverse);
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String decodedString;

        while ((decodedString = in.readLine()) != null) {
            text = jsonParser(decodedString);
        }
        in.close();
        return text;
    }
}
