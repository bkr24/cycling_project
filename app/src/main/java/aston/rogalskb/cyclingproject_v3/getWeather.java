package aston.rogalskb.cyclingproject_v3;

import android.os.AsyncTask;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class getWeather extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... args) {
        LatLng location = ViewWeather.getInstance().getLocation();
        String apiKEY = ViewWeather.getInstance().openWeatherApi;
        String lng1 = Double.toString(location.longitude);
        String lan1 = Double.toString(location.latitude);
        // String url = String.format("https://samples.openweathermap.org/data/2.5/weather?lat=%1$s&lon=%2$s&appid=%3$s", location.latitude, location.longitude, apiKEY);
        String url = HttpRequest.excuteGet("https://samples.openweathermap.org/data/2.5/weather?lat="+ lan1 + "&lon=" + lng1 + "&appid=" + apiKEY);
        String response = HttpRequest.excuteGet(url);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {


        try {

            TextView location = ViewWeather.getInstance().t1_location;
            TextView temperatueView = ViewWeather.getInstance().t3_temp;
            TextView weatherCond = ViewWeather.getInstance().t2_weather_description;
            TextView date = ViewWeather.getInstance().t4_date;


            JSONObject jsonObj = new JSONObject(result);
            JSONObject main = jsonObj.getJSONObject("main");
            JSONObject sys = jsonObj.getJSONObject("sys");
            // JSONObject wind = jsonObj.getJSONObject("wind");
            JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

            //Long updatedAt = jsonObj.getLong("dt");
            //String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
            String temp = main.getString("temp") + "°C";
            String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
            String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
            String pressure = main.getString("pressure");
            String humidity = main.getString("humidity");

            // Long sunrise = sys.getLong("sunrise");
            // Long sunset = sys.getLong("sunset");
            // String windSpeed = wind.getString("speed");
            String weatherDescription = weather.getString("description");

            String address = jsonObj.getString("name") + ", " ;//+ sys.getString("country");


            location.setText(address);
            temperatueView.setText(temp);
            weatherCond.setText(weatherDescription);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
