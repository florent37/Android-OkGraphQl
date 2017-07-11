# RxGraphQl

Reactive GraphQl client for Android Edit

# Download

In your module [![Download](https://api.bintray.com/packages/florent37/maven/RxGraphQl/images/download.svg)](https://bintray.com/florent37/maven/RxGraphQl/_latestVersion)
```groovy
compile 'com.github.florent37:rxgraphql:0.0.1'
```


# Create your RxGraphQl

```
RxGraphQl rxGraphQl = new RxGraphQl.Builder()
                .okClient(okHttpClient)
                .baseUrl("http://192.168.1.16:8888/graphql")
                .converter(new GsonConverter(new Gson()))
                .build();
```

# Query

```
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
        
        .cast(WeatherForecastResponse.class) //auto convert response to WeatherForecastResponse
        .field("city", "Seattle")
        
        .toSingle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                data -> /* play with the fetched WeatherForecastResponse */,
                throwable -> /* handle error */
        );
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
