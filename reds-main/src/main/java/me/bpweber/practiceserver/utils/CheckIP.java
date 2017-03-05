package me.bpweber.practiceserver.utils;

/**
 * Created by jaxon on 2/24/2017.
 */


import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckIP {
    public static boolean checkone;
    public static boolean checktwo;
    public static boolean checkonefail, checktwofail;
    public static boolean lowprob;
    public static JSONObject geo;
    public static JSONObject weather;
    public static JSONObject cnnnews;


    public static String getTemp() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return "Unavailable";
        }
    }

    public static String getTempText() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("text");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return "Unavailable";
        }
    }

    public static String getTitle() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getString("title");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return "Unavailable";
        }
    }

    public static double getHumidity() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getDouble("humidity");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return (Double) null;
        }
    }

    public static double getWindChill() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getDouble("chill");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return (Double) null;
        }
    }

    public static double getWindSpeed() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getDouble("speed");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return (Double) null;
        }
    }

    public static String getTempUnit() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("units").getString("temperature");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return "F";
        }
    }

    public static String getSpeedUnit() {
        try {
            return weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("units").getString("speed");
        } catch (Exception e) {
            Bukkit.broadcastMessage(e.toString() + e.getCause());
            return "mph";
        }
    }

    public static String getRegionCode() {
        try {
            return geo.getJSONObject("geolocation_data").getString("region_code");
        } catch (Exception e) {

            return "Unavailable";
        }
    }

    public static String getCountryCode() {
        try {
            return geo.getJSONObject("geolocation_data").getString("country_code_iso3166alpha2");
        } catch (Exception e) {

            return "Unavailable";
        }
    }

    public static String getCity() {
        try {
            return geo.getJSONObject("geolocation_data").getString("city");
        } catch (Exception e) {

            return "Unavailable";
        }
    }

    public static String getCountry() {
        try {
            return geo.getJSONObject("geolocation_data").getString("country_name");
        } catch (Exception e) {

            return "Unavailable";
        }
    }

    public static String getContinent() {
        try {
            return geo.getJSONObject("geolocation_data").getString("continent_name");
        } catch (Exception e) {

            return "Unavailable";
        }
    }

    public static String getRegion() {
        try {
            return geo.getJSONObject("geolocation_data").getString("region_name");
        } catch (Exception e) {

            return "Unavailable";
        }
    }


    public static boolean calculate(String ip) {
        boolean three = checkthree(ip);
        if (three) {
            return true;
        }


        double one = checkone(ip);
        double two = checkone(ip);


        if (one == 1 && three) {
            return true;
        } else if (two == 1 && three) {
            return true;
        } else if (one == 0.5 && three) {
            return true;
        } else if (one == 0 && two == 1 && three) {
            return true;
        } else if (two == 0 && one == 0.5 && three) {
            return true;
        } else if (one == 1 && two == 1) {
            return true;
        } else if (one == 0.5 && two == 1) {
            return true;
        } else if (one == 0 && two == 0) {
            return false;
        } else if (one == 1 && two == 0) {
            return false;
        } else if (one == 0 && two == 1) {
            return false;
        } else if (one == 0.101 && two == 0.101) {
            return false;
        } else if (one == 0.101 && two == 1) {
            return true;
        } else if (one == 1 && two == 0.101) {
            return true;
        } else if (one == 0.5 && two == 0.101) {
            return false;
        } else if (one == 0.5 && two == 0) {
            return false;
        } else {
            return false;
        }

    }

    public static boolean getGeoObject(String ip) {

        try {
            geo = getJSONObjectFromURL("http://api.eurekapi.com/iplocation/v1.8/locateip?key=SAK6XG68KB889597Z49Z&ip=" + ip + "&format=JSON");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                geo = getJSONObjectFromURL("http://api.eurekapi.com/iplocation/v1.8/locateip?key=SAK6XG68KB889597Z49Z&ip=" + ip + "&format=JSON");
                return true;
            } catch (Exception e1) {
                return false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                geo = getJSONObjectFromURL("http://api.eurekapi.com/iplocation/v1.8/locateip?key=SAK6XG68KB889597Z49Z&ip=" + ip + "&format=JSON");
                return true;
            } catch (Exception e1) {
                return false;
            }
        }


    }

    public static boolean getCNNObject() {

        try {
            cnnnews = getJSONObjectFromURL("https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=524bded2060d4e0bb974b0d8a53ba912");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                cnnnews = getJSONObjectFromURL("https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=524bded2060d4e0bb974b0d8a53ba912");
                return true;
            } catch (Exception e1) {
                return false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                cnnnews = getJSONObjectFromURL("https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=524bded2060d4e0bb974b0d8a53ba912");
                return true;
            } catch (Exception e1) {
                return false;
            }
        }


    }


    public static String getCnnTitle(int i) {
        try {
            JSONObject obj = cnnnews.getJSONArray("articles").getJSONObject(i);
            return obj.getString("title");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return "Unavailable";
        }


    }

    public static String getCnnDesc(int i) {
        try {
            JSONObject obj = cnnnews.getJSONArray("articles").getJSONObject(i);
            return obj.getString("description");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return "Unavailable";
        }


    }

    public static String getCnnLink(int i) {
        try {
            JSONObject obj = cnnnews.getJSONArray("articles").getJSONObject(i);
            return obj.getString("url");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return "Unavailable";
        }


    }

    public static boolean getWeatherObject(String ip) {

        try {
            weather = getJSONObjectFromURL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + getCity() + "%2C%20" + getRegionCode() + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                weather = getJSONObjectFromURL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + getCity() + "%2C%20" + getRegionCode() + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                return true;
            } catch (Exception e1) {
                return false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                weather = getJSONObjectFromURL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + getCity() + "%2C%20" + getRegionCode() + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                return true;
            } catch (Exception e1) {
                return false;
            }
        }


    }

    public static double checkone(String ip) {
        JSONObject jo = null;
        lowprob = false;
        try {
            jo = getJSONObjectFromURL("http://check.getipintel.net/check.php?ip=" + ip
                    + "&contact=jexcoon@gmail.com&format=json&flags=f");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                jo = getJSONObjectFromURL("http://check.getipintel.net/check.php?ip=" + ip
                        + "&contact=jexcoon@gmail.com&format=json&flags=f");
            } catch (Exception e1) {
                return 0.101;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                jo = getJSONObjectFromURL("http://check.getipintel.net/check.php?ip=" + ip
                        + "&contact=jexcoon@gmail.com&format=json&flags=f");
            } catch (Exception e1) {
                return 0.101;
            }
        }
        if (!(jo == null)) {
            try {
                long resultlong = jo.getLong("result");

                if (resultlong > 0.89) {
                    return 1;
                } else if (resultlong > 0.50) {
                    return 0.5;
                } else {
                    return 0;
                }

            } catch (JSONException e) {
                return 0.101;
            }

        } else {
            return 0.101;
        }

    }

    public static double checktwo(String ip) {
        JSONObject jo2 = null;
        try {
            jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return 0.101;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return 0.101;
            }
        }

        if (!(jo2 == null)) {
            try {
                long result2long = jo2.getLong("proxy");
                Bukkit.broadcastMessage("ipintel" + result2long);
                if (result2long > 0.9) {
                    return 1;
                } else {
                    return 0;
                }

            } catch (Exception e) {
                return 0.101;
            }

        } else {
            return 0.101;
        }

    }

    public static String getHostName(String ip) {
        JSONObject jo2 = null;
        try {
            jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return "null";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return "null";
            }
        }

        if (!(jo2 == null)) {
            try {
                String hostname = jo2.getString("hostname");
                String asn = jo2.getString("asn");

                return hostname;

            } catch (Exception e) {
                return "null";
            }

        } else {
            return "null";
        }
    }

    public static boolean checkthree(String ip) {
        JSONObject jo2 = null;
        try {
            jo2 = getJSONObjectFromURL("https://cymon.io/api/nexus/v1/ip/" + ip.trim() + "/events/?format=json");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("https://cymon.io/api/nexus/v1/ip/" + ip.trim() + "/events/?format=json");
            } catch (Exception e1) {
                return false;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("https://cymon.io/api/nexus/v1/ip/" + ip.trim() + "/events/?format=json");
            } catch (Exception e1) {
                return false;
            }
        }

        if (!(jo2 == null)) {
            try {
                int result2long = jo2.getInt("count");


                return result2long >= 1;

            } catch (Exception e) {
                return false;
            }

        } else {
            return false;
        }
    }

    public static String getASN(String ip) {
        JSONObject jo2 = null;
        try {
            jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return "null";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            try {
                jo2 = getJSONObjectFromURL("http://legacy.iphub.info/api.php?ip=" + ip + "&showtype=4");
            } catch (Exception e1) {
                return "null";
            }
        }

        if (!(jo2 == null)) {
            try {
                String hostname = jo2.getString("hostname");
                String asn = jo2.getString("asn");

                return asn;

            } catch (Exception e) {
                return "null";
            }

        } else {
            return "null";
        }
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        jsonString = sb.toString();

        return new JSONObject(jsonString);
    }


}