### Challengefy

An Android experiment using MVVM and DataBinding.

#### Explaining Implementation

##### Language
I used `Kotlin` instead of `Java`, which it's pretty much self explanatory.

##### Architecture
The architecture that I'm using it's a simple **MVVM** with **DataBinding** without the `Architecture Components`.<br>
I wanted to use **DataBinding** pointing directly to the **ViewModel** fields, which is only possible using `MutableLiveData` after the 3.1.0-alpha Gradle release.
<br><br>
I'm not very confident with the alpha version to use it in this experiment, that's why I used `ObservableField` instead.

##### Dependency Injection
The dependency graph is built based on `dagger.android`.
I created three types of scope to include all dependencies.
- **Singleton** for app level dependencies
- **ActivityScope** for every dependency used inside an Activity lifecycle
- **FragmentScope** dependencies living inside a Fragment attachment lifecycle

All the communication between Activity <-> Fragments are done via DI using scopes.

##### Place Awareness
I used the APIs from Google Play Services, like the **Current Place Discovery** and **Predictions**.
For location updates I used the new implementation for the `FusedLocationProviderClient`.
<br><br>
I created a simple logic for the place discovery, if the Google Places returns an address more than 30 meters away from the current user position, I fetch the local Geocoder for closer current address.

##### Fragments
Yeah I used mostly **Fragments** to do the View layer, but please don't be scared.
<br><br>
**The reasons that I chose to use Fragments were:** <br>
- I wanted to create smooth transitions between **Views**
- Load the Map just once (performance stuff)
- With Fragments it was much more simpler to create Subcomponent scopes using DaggerAndroid.

##### Future Improvements
- Update Gradle to 3.1.0-alphaX to use `MutableLiveData` instead of `ObservableField` for DataBinding


##### Other stuff
I won't discuss everything in this file, but if you have any question or want to kill me for anything that I did, please contact me I'll be glad to discuss it with you.
