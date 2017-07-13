package com.github.florent37.rxgraphql.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.florent37.okgraphql.OkGraphql;
import com.github.florent37.okgraphql.converter.GsonConverter;
import com.github.florent37.rxgraphql.sample.model.StarWarsResponse;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.github.florent37.okgraphql.Field.newField;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.text1)
    TextView query_hero;
    @Bind(R.id.text2)
    TextView query_hero_enqueue;
    @Bind(R.id.text3)
    TextView query_variables_directives;
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
        //query_variables_directives();
//
        //query_hero_built();
        //query_argument_built();
        fragment();
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
                    query_hero_enqueue.setText(responseString);
                }, error -> {
                    query_hero_enqueue.setText(error.getLocalizedMessage());
                });
    }

    private void fragment() {
        okGraphql

                .query("{" +
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
                    query_hero_enqueue.setText(responseString);
                }, error -> {
                    query_hero_enqueue.setText(error.getLocalizedMessage());
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
                        dataString -> query_hero.setText(dataString),
                        throwable -> query_hero.setText(throwable.getLocalizedMessage())
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
                        dataString -> query_hero.setText(dataString),
                        throwable -> query_hero.setText(throwable.getLocalizedMessage())
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
                        dataString -> query_hero.setText(dataString),
                        throwable -> query_hero.setText(throwable.getLocalizedMessage())
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
                        data -> query_variables_directives.setText(data.toString()),
                        throwable -> query_variables_directives.setText(throwable.getLocalizedMessage())
                );
    }
}
