import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

class TestAlignment {

    private static String readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while((line = reader.readLine()) != null) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

    static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void main(String[] args) throws IOException {
        //String bartok_json = readFile("examples/Bartok.json");
        //String bartok_original_text = readFile("examples/bartok.txt");
        String bartok_json = readFile("examples/bartok1.json");
        String bartok_original_text = readFile("examples/bartok1.txt");

        String url = "http://stanfordneralign.appspot.com/api";
        String charset = "UTF-8";
        String query = String.format("json=%s&text=%s",
            URLEncoder.encode(bartok_json, charset),
            URLEncoder.encode(bartok_original_text, charset));

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        OutputStream output = connection.getOutputStream();
        try {
            output.write(query.getBytes(charset));
        } finally {
            try { output.close(); } catch (IOException logOrIgnore) {}
        }
        InputStream response = connection.getInputStream();
        def json = convertStreamToString(response);
        //println json
        
        println new Container(json).toPrettyJson()
    }
}
