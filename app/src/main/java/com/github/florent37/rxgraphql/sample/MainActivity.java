package com.github.florent37.rxgraphql.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.florent37.okgraphql.OkGraphql;
import com.github.florent37.okgraphql.converter.GsonConverter;
import com.github.florent37.rxgraphql.sample.model.StarWarsResponse;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.github.florent37.okgraphql.Field.newField;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;

    private OkGraphql okGraphql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.okGraphql = new OkGraphql.Builder()
                .okClient(((MainApplication) getApplication()).getOkHttpClient())
                .baseUrl("http://10.0.0.4:8080/graphql")
                .converter(new GsonConverter(new Gson()))
                .build();

        //query_hero();
        //query_hero_enqueue();
        query_variables_directives();
//
        //query_hero_built();
        //query_argument_built();
        //fragment(text3);
        //mutation();
    }

    private void query_hero_enqueue() {
        okGraphql

                .query("{" +
                        "  hero {" +
                        "    name" +
                        "  }" +
                        "}"
                )

                .enqueue(responseString -> {
                    text1.setText(responseString);
                }, error -> {
                    text1.setText(error.getLocalizedMessage());
                });
    }

    private void fragment() {
        okGraphql

                .body("{" +
                        "  leftComparison: hero(episode: EMPIRE) {" +
                        "    ...comparisonFields" +
                        "  }" +
                        "  rightComparison: hero(episode: JEDI) {" +
                        "    ...comparisonFields" +
                        "  }" +
                        "}"
                )

                .fragment("comparisonFields on Character {" +
                        "  name" +
                        "  appearsIn" +
                        "  friends {" +
                        "    name" +
                        "  }" +
                        "}")

                .enqueue(responseString -> {
                    text1.setText(responseString);
                }, error -> {
                    text1.setText(error.getLocalizedMessage());
                });
    }

    private void query_hero_built() {
        okGraphql

                .query(newField()
                        .field(newField("hero")
                                .field("name")
                                .field(newField("friend")
                                        .field("name")
                                )
                        )
                )

                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dataString -> text1.setText(dataString),
                        throwable -> text1.setText(throwable.getLocalizedMessage())
                );
    }

    private void query_argument_built() {
        okGraphql

                .query(newField()
                        .field(newField("human").argument("id: \"1000\"")
                                .field("name")
                                .field("height")
                        )
                )

                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dataString -> text1.setText(dataString),
                        throwable -> text1.setText(throwable.getLocalizedMessage())
                );
    }

    private void query_hero() {
        okGraphql

                .query("{" +
                        "  hero {" +
                        "    name" +
                        "  }" +
                        "}"
                )

                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dataString -> text1.setText(dataString),
                        throwable -> text1.setText(throwable.getLocalizedMessage())
                );
    }

    private void query_variables_directives() {
        okGraphql

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

                .cast(StarWarsResponse.class)

                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> text1.setText(data.toString()),
                        throwable -> text1.setText(throwable.getLocalizedMessage())
                );
    }
}
