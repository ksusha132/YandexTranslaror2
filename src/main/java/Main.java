
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.*;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String key = "trnsl.1.1.20171020T140902Z.10f6991ce44764b9.23e848d74e20f95c0d5fce8c6dcb20ebf947a303";

        System.out.println("Input the language you want to translate from:");
        BufferedReader formLang = new BufferedReader(new InputStreamReader(System.in));
        String from = formLang.readLine();


        System.out.println("Input the language you want to translate to:");
        BufferedReader toLang = new BufferedReader(new InputStreamReader(System.in));
        String to = toLang.readLine();


        URL url = new URL(createURL(Lang.languageGetter(from), Lang.languageGetter(to), key));
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);

        System.out.println("Input the word you want to translate:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String text = bufferedReader.readLine();

        String stringToReverse = URLEncoder.encode(text, "UTF-8");


        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        out.write("text=" + stringToReverse);
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String decodedString;
        JsonParser parser = new JsonParser();
        while ((decodedString = in.readLine()) != null) {
            JsonObject mainObject = (JsonObject) parser.parse(decodedString);
            JsonArray result = mainObject.getAsJsonArray("text");
            String res = result.toString().substring(2, result.toString().length() - 2);
            System.out.println(res);
        }
        in.close();
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
}
