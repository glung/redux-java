# redux-java
The java version of Redux : a predictable state container for java apps. 

Redux-java has been designed with the Android case in mind but it is not constrained to it. It does not have any dependency on Android - except for the optional android dev-tools module. 

Like the initial version of [Redux (for JavaScript apps)] (https://github.com/rackt/redux), it helps you to write applications that behave consistently and are easy to test. It also provides a time traveling debugger.

![devtools-android-ui demo gif](docs/devtools-ui-android.gif)

# Principles 
When using Redux-java your application logic is at the center but isolated from the context of exceution, such as the platform specifics. In the case of Android, none of your application logic will depend on Android. 

To accomplish this, Redux takes actions as input and provides a gloval state as the output. The actions are reduced to states using pure functions. This is why your application states are predictables and easy to test. 

Redux has a unidirectional data flow* (from the action to the state) which make it fairly easy to understand. 

# Android 
Redux-java as been designed with the Android case in mind (it depends on Java7, does not have big dependencies). 

It is pure Java but as been designed to work with Kotlin (which I think is a better choice to write your reducers). Therefore, the tests are written in Kotlin, but this is not a runtime dependency

# Project 
The project is in early developent and is not yet released.

# Examples 
- [app-redux-android-todolist](examples/app-redux-android-todolist)
- [app-redux-android-todolist-async](examples/app-redux-android-todolist-async)
- [app-redux-android-drawing](examples/app-redux-android-drawing)


# Notes*
- If you are not familiar with unidirectional data flow, I recommand readling the [Facebook flux documentation](https://facebook.github.io/flux/). 
- Redux is a simplification of flux created by [Dan Abramov](https://twitter.com/dan_abramov), I also recommend watching this [30' video](https://www.youtube.com/watch?v=xsSnOQynTHs) to understand the principle and motivations of redux.



http://rackt.github.io/redux/docs/introduction/Motivation.html

utilizing a unidirectional data flow. It's more of a pattern rather than a formal framework, and you can start using Flux immediately without a lot of new code.
