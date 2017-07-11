package florent37.github.com.rxgraphql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.florent37.rxgraphql.RxGraphQl;
import com.github.florent37.rxgraphql.converter.GsonConverter;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import florent37.github.com.rxgraphql.model.WeatherForecastResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RxGraphQl rxGraphQl;

    @Bind(R.id.text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.rxGraphQl = new RxGraphQl.Builder()
                .okClient(((MainApplication) getApplication()).getOkHttpClient())
                .baseUrl("http://192.168.1.16:8888/graphql")
                .converter(new GsonConverter(new Gson()))
                .build();

        query();
        //mutation();
    }

    private void enqueue() {
        rxGraphQl.query(
                "weatherForecast(city:@city) {" +
                        "city {" +
                        "      id," +
                        "      population," +
                        "      name," +
                        "      country," +
                        "      coord{" +
                        "        lon," +
                        "        lat" +
                        "      }" +
                        "   }" +
                        "}"
        )
                .cast(WeatherForecastResponse.class)
                .field("city", "Seattle")
                .enqueue(data -> {
                    Log.d(TAG, data.toString());
                    textView.setText(data.toString());
                }, error -> {

                });
    }

    private void query() {
        rxGraphQl
                .query(
                        "weatherForecast(city:@city) {" +
                                "city {" +
                                "      id," +
                                "      population," +
                                "      name," +
                                "      country," +
                                "      coord{" +
                                "        lon," +
                                "        lat" +
                                "      }" +
                                "   }" +
                                "}"
                )

                .cast(WeatherForecastResponse.class)
                .field("city", "Seattle")
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(data -> Log.d(TAG, data.toString()))
                .subscribe(
                        data -> textView.setText(data.toString()),
                        throwable -> textView.setText(throwable.getLocalizedMessage())
                );
    }

    private void jedi() {
        rxGraphQl

                .query(
                        "Hero($episode: Episode, $withFriends: Boolean!) {" +
                                "  hero(episode: $episode) {" +
                                "    name" +
                                "    friends @include(if: $withFriends) {" +
                                "      name" +
                                "    }" +
                                "  }"
                )


                .variable("episode", "JEDI")
                .variable("withFriends", false)

                .cast(WeatherForecastResponse.class)

                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> textView.setText(data.toString()),
                        throwable -> textView.setText(throwable.getLocalizedMessage())
                );
    }
}
