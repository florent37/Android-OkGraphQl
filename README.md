# OkGraphQl

Reactive GraphQl client for Android

# Download

In your module [![Download](https://api.bintray.com/packages/florent37/maven/RxGraphQl/images/download.svg)](https://bintray.com/florent37/maven/RxGraphQl/_latestVersion)
```groovy
compile 'com.github.florent37:okgraphql:0.0.1'
```


# Create your RxGraphQl

```java
OkGraphQl okGraphql = new OkGraphQl.Builder()
                .okClient(okHttpClient)
                .baseUrl("http://192.168.1.16:8888/graphql")
                .converter(new GsonConverter(new Gson()))
                .build();
```

# Usage

Call the query then convert the json response to your model

```
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

you can also use RxJava instead of callbacks

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

Use Fields Builders instead of Strings to create your query dynamically

```
okGraphql

      .query(newField()
              .field(newField("human").argument("id: \"1000\"")
                      .field("name")
                      .field("height")
              )
      )
      
      ...
```

This example will generate :

```
{
  human(id: "1000") {
    name
    height
  }
}
```

# Credits

Author: Florent Champigny [http://www.florentchampigny.com/](http://www.florentchampigny.com/)

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
