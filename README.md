# Prayer_App

Please ⭐️ this repo and share it with others.

# Description

Prayers times application is a simple application for showing prayers times. 
This application should allow the user to view the daily prayers times based on his 
location and selected calculation method. User should be able to view the today's 
prayers times and upcoming prayers times for a week, and display a countdown 
timer for the next prayer time. 
User should also be able to view the Qibla direction on map based on his location. 
As an additional point, the app can display the live direction of the Qibla based on 
the gyroscope sensor. 

# App Requirement Details: 

* In home screen, the APP must fetch and show today’s prayers times based on user location.
* Change the selected day by click back and forward arrows with limitation by today. 
* Store times locally to be shown if there was any problem with network. 
* Showing times in 12Hrs format. 
* Count down for the next prayer time in home screen. 
* Showing current location title in home screen. 
* Navigate to map screen to show Kaaba marker and current location marker. 
* Showing Qibla direction line on map screen. 
* Change the live direction based on gyroscope sensor result. (bonus points)

# Screenshots

<div>
  <img src="https://github.com/Abdallah-Mekky/News_App_Updated_Version/assets/80880411/afca9594-3b0f-4b79-b11c-74ea48da87e0"  width="250">
  <img src="https://github.com/Abdallah-Mekky/News_App_Updated_Version/assets/80880411/98a4b6ce-f3f6-42d6-86bf-97fcc0d17a58"  width="250">
</div>

# Video 
https://github.com/Abdallah-Mekky/News_App_Updated_Version/assets/80880411/153664a6-cff4-4bc2-b8ce-0471e700e9ba


# Tools
* [Kotlin](https://kotlinlang.org/) 
* [View Model](https://bit.ly/3e43P79) - The View Model class is designed to store and manage UI-related data in a lifecycle conscious way.
* [Live Data](https://bit.ly/3KuahQR) - LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services.
* [Data Binding](https://bit.ly/3PVsjNc) - The Data Binding Library is a support library that allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
* [Dependency Injection Using Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
* [Repository Pattern](https://medium.com/swlh/repository-pattern-in-android-c31d0268118c)   
* [Google gson](https://github.com/google/gson) - For parsing JSON data.

# Database
* [Room](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

# Network Calls
* [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.

# Background tasks
* [Kotlin Coroutines](https://bit.ly/3Kq3ec3) - A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.

# API
* [Al-Adhan](https://aladhan.com/)

# End Points 
* [PRAYERS TIMES API](https://aladhan.com/prayer-times-api)
* [QIBLA DIRECTION API:](https://aladhan.com/qibla-api)


