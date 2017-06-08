# Themoviedb Android Client

This app get information about movies from [TheMoviedb](https://www.themoviedb.org/) 

This project uses the [Themovie Android Wrapper](https://github.com/rafaelcrz/themovie_android_wrapper).

## Features
- Get the Now Playing
- Get the Popular Movies
- Get the Top Rated Movies
- Show the movie detail
- Get the trailers/clips from a movie

## Build this project

* Create a file called ``` secret.properties ``` on root project
* In the ``` secret.properties ``` create a reference following this format

``` API_KEY=your_themoviedb_apikey ```

Get the TheMoviedb API KEY here

In the ``` build.gradle ``` there's the getAPIKey function, its reads the ``` secret.properties ``` file and return the client API_KEY.

```
def getAPIKey() {
   	def Properties props = new Properties()
    props.load(new FileInputStream(new File('secret.properties')))
   	return props['API_KEY']
}
```
* In the ``` app/build.gradle ``` create a __buildConfigField__
```
buildConfigField ("String", "THEMOVIEDB_API_KEY", "\""+getAPIKey+"\"") 
---
buildConfigField ("<type>", "<reference_name>", "\""+<value>+"\"") 
```

Now you can create a String variable getting the API_KEY value from ``` BuildConfig.java ``` constant (it is automatically generated after the build of Gradle).
```
public static final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;
```
__This apprach is recomended because prevents the client API KEY value be add to yours commit__


### In |app| build.gradle

buildConfigField("String", "THEMOVIEDB_API_KEY", "${THEMOVIEDB_API_KEY}")

-> __Use your API_KEY.__ Get it [here](https://developers.themoviedb.org/3/getting-started).

## Screen

<img src="/screen.png">
