# PopularMoviesStage1

In order for the application to work you will need to get an API Key for the TMDB.org api.
Go to https://developers.themoviedb.org/3/getting-started to get started in getting an API Key for this API if you do not already have one.

Once you have your API Key, open the project and edit project:gradle.properties file and add a new reference for TMDB_API_KEY = "Your_Key" aka:

#TMDB API Key Entered here
TMDB_API_KEY = "blahblahYOURKEYblahBlah"

Note the application has been tested on a Google Pixel, Samsung S7, S7 edge, Samsung Tab 2 and Kindle Fire HD. It also works in the Emulator for Android Studio and GenyMotion, The Android Studio Emulator seems to have an issue with the network on occaision that I have not been able to solve. This is not just for the app but also affects the browser.

I recommend testing on a live device.
