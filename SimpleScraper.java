import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleScraper extends JFrame {

    JButton scrapeButton;

    public SimpleScraper() {

        setTitle("Product Scraper");
        setSize(350, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        scrapeButton = new JButton("Start Scraping");

        add(scrapeButton);

        scrapeButton.addActionListener(e -> scrapeData());

        setVisible(true);
    }

    public void scrapeData() {

        try {

            URL url = new URL("https://books.toscrape.com/");

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );

            String line;
            StringBuilder html = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                html.append(line);
            }

            reader.close();

            String webpage = html.toString();

            Pattern pattern = Pattern.compile(
                    "title=\"(.*?)\".*?price_color\">£(.*?)</p>.*?star-rating (.*?)\"",
                    Pattern.DOTALL
            );

            Matcher matcher = pattern.matcher(webpage);

            FileWriter writer = new FileWriter("products.csv");

            writer.write("Product Name,Price,Rating\n");

            while (matcher.find()) {

                String name = matcher.group(1);
                String price = matcher.group(2);
                String rating = matcher.group(3);

                writer.write(name + "," + price + "," + rating + "\n");
            }

            writer.close();

            JOptionPane.showMessageDialog(this,
                    "Data saved into products.csv");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error occurred");
        }
    }

    public static void main(String[] args) {

        new SimpleScraper();
    }
}