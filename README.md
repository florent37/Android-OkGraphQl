GraphQL is a query language for your API, and a server-side runtime for executing queries by using a type system you define for your data. GraphQL isn't tied to any specific database or storage engine and is instead backed by your existing code and data.
A GraphQL service is created by defining types and fields on those types, then providing functions for each field on each type.

Learn more at : http://graphql.org/learn/

OkGraphQl is an Android client implementation, thinked to be easy to use, fluent, and completely modular

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

In your module 

[![Download](https://api.bintray.com/packages/florent37/maven/OkGraphQl/images/download.svg)](https://bintray.com/florent37/maven/OkGraphQl/_latestVersion)
```groovy
compile 'com.github.florent37:okgraphql:0.0.0'

//dependencies
compile 'com.squareup.okhttp3:okhttp:3.8.1'
compile 'io.reactivex.rxjava2:rxjava:2.1.0'
compile 'com.google.code.gson:gson:2.8.0'
compile 'com.github.florent37:android-nosql:1.0.0'
```

# Creation

First, initialize you OkGraphQl client with
- The GraphQl sever url
- An OkHttpClient (optional)
- A converter (optional)

```java
OkGraphql okGraphql = new OkGraphql.Builder()
                .okClient(okHttpClient)
                .baseUrl("http://192.168.1.16:8888/graphql")
                .converter(new GsonConverter(new Gson()))
                .build();
```

# Usage

**Please note that following examples have been tested on the StarWars GraphQl server : https://github.com/apollographql/starwars-server**

Create your GraphQl query with `query(string)`, 
then execute with `enqueue(SuccessCallback, ErrorCallback)`

By default the success response is the Json (as String)

```java
okGraphql
        .query("{" +
                "  hero {" +
                "    name" +
                "  }" +
                "}"
        )
        .enqueue(responseString -> {
            //play with your responseString
        }, error -> {
            //display the error
        });
```

You can also inflate a POJO with the returned Json using `.cast(CLASS)`
This will use the `converter` assigned to your OkGraphQl

```java
class Character {
     String name;
}
   
class StarWarsResponse {
   Character hero;  
}

okGraphql
        .query("{" +
                "  hero {" +
                "    name" +
                "  }" +
                "}"
        )
        
        .cast(StarWarsResponse.class) //will automatically cast the data json to

        .enqueue(response -> {
            //play with your StarWarsResponse
        }, error -> {
            //display the error
        });
```

# RxJava !

You can also use RxJava methods to your query using `.toSingle()`

```java
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

         .cast(StarWarsResponse.class) //will automatically cast the data json to
         
         .toSingle()
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(
                 data -> textView.setText(data.toString()),
                 throwable -> textView.setText(throwable.getLocalizedMessage())
         );
```

# Query Builder

Use Fields Builders instead of Strings to create dynamically your queries

```java
okGraphql

      .query(newField()
              .field(newField("human").argument("id: \"1000\"")
                      .field("name")
                      .field("height")
              )
      )
      
      ...
```

This example will generate the query :

```
{
  human(id: "1000") {
    name
    height
  }
}
```

# Mutations

Mutations are easy to execute with OkGraphQl

```java
 okGraphql
          .mutation("CreateReviewForEpisode($ep: Episode!, $review: ReviewInput!) {" +
                  "  createReview(episode: $ep, review: $review) {" +
                  "    stars" +
                  "    commentary" +
                  "  }" +
                  "}")
          .variable("ep", "JEDI")
          .variable("review", "{ \"stars\": 5, \"commentary\": \"This is a great movie!\"")
          .enqueue(responseString -> {
              text1.setText(responseString);
          }, error -> {
              text1.setText(error.getLocalizedMessage());
          });
```

# Fragments 

Use `.fragment(code)` to append a fragment on your request

```java
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
                    query_hero_enqueue.setText(responseString);
                }, error -> {
                    query_hero_enqueue.setText(error.getLocalizedMessage());
                });
```

# Cache

//TODO will use https://github.com/florent37/Android-NoSql

# Credits

Author: Florent Champigny [http://www.florentchampigny.com/](http://www.florentchampigny.com/)

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>
<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


License
--------

    Copyright 2016 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
