# Fast Launcher
### An Android app by Alex Lutz



This is my final project CIS-1051.
I'm really bad at organizing my phone homescreen, so it usually takes me a good 10-30 seconds to open an app I'm looking for (which is unacceptable).
I wanted to create an app that not only forced me to organize my phone, but also allowed me to launch apps faster than ever before.

I created this app using Java and XML in Android Studio -- which took care of generating all of the necessary config files.
This project was created almost entirely from included Android Libraries, with the exception being the [blurkit](https://github.com/CameraKit/blurkit-android/blob/master/blurkit/src/main/java/io/alterac/blurkit/BlurKit.java) library by Alterac. I used blurkit to apply the blur effect behind the homescreen tiles.

I followed [this](https://www.androidauthority.com/make-a-custom-android-launcher-837342-837342/) tutorial by Android Authority for some of the basic functionality, such as setting up the app drawer. But all of the logic for launching and editing favorited apps is entirely my own and the swipe-based tile concept was totally original as well.

The majority of the Java code I wrote is in [this](/app/src/main/java/com/example/launchertest1) directory, and all of the XML is [here](/app/src/main/res/layout).

This was my first time seriously coding in the Android Environment, so it was a big undertaking, but I learned a ton. Plus I'm really glad to have an organized homescreen for once.

## [CLICK HERE FOR VIDEO DEMO](https://youtu.be/mqH7dv4xRhc)

### Features

 - can be set as homescreen
 - dynamically creates a list of installed apps, and displays their name and icon.
 - the user has the ability to favorite apps and change their favorites at anytime.
 - favorited apps can be launched quickly from a gesture based interface.
 - favorited apps are saved to a config file, so preferences are not lost in between sessions
 - the app displays the system wallpaper.
 - transparent interface elements are blurred dynamically.

### Challenges

 - The Android API is incredibly daunting. At this point I'm accustomed to learning new python modules and I have some similar experience with java, however it felt like any time I wanted to add a new feature to the project, it required 3 new imports.
 - The perfered language to code in Android is Kotlin. I already have experience in Java, so using that made the most sense to me. Howerever, most Android content creaters use Kotlin so I was forced to either translate the tips they were giving to Java (which was time consuming) or search for the occastional java resource.
 - Getting the blur library to work was super difficult, the project hadn't been updated in 3 years, so I wasn't even sure it would work. I had to do a lot of fiddling with the config/gradle files which was absolutely out of my confort zone.
