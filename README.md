# Fast Launcher
### An Android app by Alex Lutz

This is my final profect CIS-1051.
I'm really bad at organizing my phone homescreen, so it usually takes me a good 10-30 seconds to open an app I'm looking for (which is unacceptable).
I wanted to create an app that not only forced me to organize my phone, but also allowed me to launch apps faster than ever before.

I created this app using Java and XML in Android Studio -- which took care of generating all of the necessary config files.
This project was created almost entirely from included Android Libraries, with the exception being the [blurkit](https://github.com/CameraKit/blurkit-android/blob/master/blurkit/src/main/java/io/alterac/blurkit/BlurKit.java) library by Alterac. I used blurkit to apply the blur effect behind the homescreen tiles.

I followed [this](https://www.androidauthority.com/make-a-custom-android-launcher-837342-837342/) tutorial by Android Authority for some of the basic functionality, such as setting up the app drawer. But all of the logic for launching and editing favorited apps is entirely my own. (A lot of Android content producers on the internet use Kotlin for Android developement now, so it was pretty tricky finding tutorials outside of the documentation that didn't require a lot of translating)

The majority of the Java code I wrote is in [this](/app/src/main/java/com/example/launchertest1) directory, and all of the XML is [here](/app/src/main/res/layout).

This was my first time seriously coding in the Android Environment, so this was a tough undertaking, but I learned a ton from it. And I'm really glad to have an organized homescreen now.
