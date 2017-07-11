package florent37.github.com.rxgraphql.model;

/**
 * Created by florentchampigny on 10/07/2017.
 */

public class WeatherForecastResponse {
    WeatherForecast weatherForecast;

    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    @Override
    public String toString() {
        return "WeatherForecastResponse{" +
                "weatherForecast=" + weatherForecast +
                '}';
    }
}
