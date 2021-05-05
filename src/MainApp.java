

import javafx.application.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp {


    public static JSONObject GetJSON(String urlF) {
        try {

            JSONParser parser = new JSONParser();
            URL oracle = new URL(urlF);
            HttpURLConnection con = (HttpURLConnection) oracle.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(60000);
            con.setReadTimeout(100000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.142");
            //URLConnection connection = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            String s = response.toString();

            JSONObject jsonObject = (JSONObject) parser.parse(s);
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void MatchDay(String url) {
        try {

            JSONObject jsonObject = GetJSON(url);

            System.out.println(jsonObject);

            JSONArray leagues = (JSONArray) jsonObject.get("leagues");
            JSONObject teams = (JSONObject) jsonObject.get("teams");

            for (int i = 0; i<2; i++ ) {
                JSONObject jsonObject1 = (JSONObject) leagues.get(i);
                JSONArray matchUps = (JSONArray) jsonObject1.get("matchUps");
                for (int r = 0; r<8; r++ ) {
                    JSONObject match = (JSONObject) matchUps.get(r);
                    Scores(match, teams);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String Scores(JSONObject scores, JSONObject teams) {
        try {

            String awayId = scores.get("awayId").toString();
            JSONObject awayT = (JSONObject) teams.get(awayId);
            String awayTeam = (String) awayT.get("name");

            String homeId = scores.get("homeId").toString();
            JSONObject homeT = (JSONObject) teams.get(homeId);
            String homeTeam = (String) homeT.get("name");

            String score = homeTeam + " - " + awayTeam + "  --> (" +scores.get("homeHT").toString() + " : "+ scores.get("awayHT").toString() + ")  - " +scores.get("homeScore").toString() + " : "+ scores.get("awayScore").toString();

            System.out.println(score);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void paw() {

        try {

            String id1 = "2647";
            String id2 = "/100246";

            String urlF = "https://www.betpawa.ug/virtuals/standing/" + id1;

            JSONObject jsonObject = GetJSON(urlF);

//            JSONObject jsonObject1 = (JSONObject) jsonObject.get("items");
//            JSONArray jsonArray = (JSONArray) jsonObject1.get("collection");

            JSONArray jsonArray = (JSONArray) jsonObject.get("standings");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            JSONArray jsonArray1 = (JSONArray) jsonObject1.get("gameRounds");

            for (int i = 0; i < 38; i++) {
                JSONObject matchDay = (JSONObject) jsonArray1.get(i);
                String matchDayId = matchDay.get("gameRoundId").toString();

                String url = "https://www.betpawa.ug/virtuals/standing/matchUps/" + id1 + "/" + matchDayId;
                MatchDay(url);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void DataBase() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            String sql = "CREATE TABLE MATCHES " +
                    "(MATCH TEXT PRIMARY KEY     NOT NULL," +
                    " SCORE           TEXT    NOT NULL, " +
                    " TOTAL            INT     NOT NULL, " +
                    " M1            INT     NOT NULL, " +
                    " Mx            INT     NOT NULL, " +
                    " M2            INT     NOT NULL, " +
                    " MO1            INT     NOT NULL, " +
                    " MO2            INT     NOT NULL, " +
                    " MO3            INT     NOT NULL, " +
                    " MU1            INT     NOT NULL, " +
                    " MU2            INT     NOT NULL, " +
                    " MU3            INT     NOT NULL, " +
                    " MYES            INT     NOT NULL, " +
                    " MNO            INT     NOT NULL, " +
                    " MII            INT     NOT NULL, " +
                    " MIX            INT     NOT NULL, " +
                    " MIT            INT     NOT NULL, " +
                    " MXI           INT     NOT NULL, " +
                    " MXX            INT     NOT NULL, " +
                    " MXT            INT     NOT NULL, " +
                    " MTI            INT     NOT NULL, " +
                    " MTX            INT     NOT NULL, " +
                    " MTT            INT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Table created successfully");
    }

    public static void PutData() {
        //Connection connection = DbConnect.getInstance().getConnection();

        String url = "https://www.betpawa.ug/events/ws/virtuals/2652/100742";

        JSONObject jsonObject = GetJSON(url);


        System.out.println(jsonObject);

//        try {
//            Statement statement = connection.createStatement();
//            int status = statement.executeUpdate("insert into MATCH (MATCH,SCORE,TOTAL,M1,MX,M2,MO1,MO2,MO3,MU1,MU2,MU3,MYES,MNO,MII,MIX,MTI,MXI,MXX,MXT,MTI,MTX,MTT)" +
//                    " values('" + file + "','" + file.toUri() + "','" + file.toUri().toURL() + "','" + file.getFileName() + "')");
//
//            if (status > 0) {
//                System.out.println("file " + file.getFileName() + " registered");
//            }
//        } catch (Exception throwables) {
//            throwables.printStackTrace();
//        }


    }

    public static void main(String[] args) {
        PutData();
    }
}
