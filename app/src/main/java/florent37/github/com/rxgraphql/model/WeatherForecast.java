package florent37.github.com.rxgraphql.model;

/**
 * Created by florentchampigny on 10/07/2017.
 */

public class WeatherForecast {
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "city=" + city +
                '}';
    }
}
