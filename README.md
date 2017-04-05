# Prevent-Screen-Off

[![Build Status](https://travis-ci.org/kevalpatel2106/Prevent-Screen-Off.svg?branch=master)](https://travis-ci.org/kevalpatel2106/Prevent-Screen-Off) [![Download](https://api.bintray.com/packages/kevalpatel2106/maven/Prevent-Screen-Off/images/download.svg) ](https://bintray.com/kevalpatel2106/maven/Prevent-Screen-Off/_latestVersion) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/kevalpatel2106/UserAwareVideoView) [![API](https://img.shields.io/badge/API-15%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=15)

![GitHub Main](/assets/Prevent_screen_off.gif)

## Featured in:
- [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Prevent--Screen--Off-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4598)
- [Medium](https://medium.com/@kevalpatel2106/keep-your-device-screen-on-smartly-7081b692c09e#.vedo7hdae)

## What is this library for?
- Ideally, when you user is looking at the screen, your application should not turn the screen off. This is huge deal for the blogging, messaging applications because those applications displays textual content to the user. Reading those textual content takes more time to the user. While reading that content (let say anu article) if the screen turns off, because of the screen timeout that is frustrating to the user.
- This library provides smart handling of the screen on-off. This library prevents screen from turning off if your user is looking at the screen might reading some textual content on the screen. As soon as the user stop looking at the screen it will allow phone screen to turn off.

## How this library works?
- This library users Google Play Services Mobile Vision API to track users eye using the front camera of the device. 
- Library will start tracking user eyes as soon as activity comes into the foreground. If the user is looking at the screen this will prevent screen from turning off.
- When library detects that the user is not looking at the screen, this will turn off the screen after some time and stop eye tracking the preserve the battery. 

## How to use this library???
### Gradle dependency:
Add these lines to your `build.gradle` file to start integration. 

```
dependency{
    compile 'com.kevalpatel2106:prevent-screen-off:1.1'
}
```
- This library automatically adds `android.permission.CAMERA` and `android.permission.WAKE_LOCK` permission in your applications `AndroidManifest.xml` file.

### Initialize in your activity:
- First you need to inherit `AnalyserActivity` in the activity which you want to controll screen on/off automatically. The library will synchronise with your activity lifecycle and start and stop eye tracking based on your activity state
- Implement `ScreenListener` to receive the callbacks from the library.
```
public class MainActivity extends AnalyserActivity      //Inherit AnalyseActivity to automatically manage activity callback.
        implements ScreenListner {                      //Implement the listener to get the callbacks
```

- Handle the callbacks and errors received from `ScreenListener`.
```
public void onScreenMonitoringStart() {
    //This callback will receive when eye tracking algorithm is intilized.
}

@Override
public void onScreenMonitoringStop() {
      //This callback will receive when eye tracking algorithm is stopped.
}

@Override
public void onErrorOccurred(int errorCode) {
    switch (errorCode) {
        case Errors.UNDEFINED:  
            //Error is not defined. 
            //Library won't control the screen on/off anymore.
            break;
        case Errors.CAMERA_PERMISSION_NOT_AVAILABLE:    
            //Camera permission is not available ask for the runtime camera permission
            break;
        case Errors.FRONT_CAMERA_NOT_AVAILABLE:     
            //Device does not have the front camera. 
            //So, this library won't control the screen on/off.
            break;
        case Errors.LOW_LIGHT:       
            //Low light in the surrounding environment so that eye tracking cannot work. 
            //Library won't control the screen on/off anymore. 
            break;
        case Errors.PLAY_SERVICE_NOT_AVAILABLE:     
            //This device doesn't have the play services installed.
            // The SDK will display the error dialog it self. This will stop the eye tracker and will not
            // prevent screen off automatically.
            break;
    }
}
```

#### That's it. You are ready to test.

## Where to use this library:
- Tracking user's eye using device camera consumes more battery. So, it is advisable that you don't integrate automatic screen controll in every screen of you application.
- You can integrate this features in your application activity, which has more textual content to read. e.g. chat conversation activity in messaging app or activity that displays full article in your blogging application._(This list can be extend for may other users. Let me know if you have more ideas.)_

## Demo
- You can download the sample apk from [here](/apk/sample.apk).

## Contribute:
#### Simple 3 step to contribute into this repo:

1. Fork the project. 
2. Make required changes and commit. 
3. Generate pull request. Mention all the required description regarding changes you made.

## Questions? 
Hit me on [![Twitter](https://img.shields.io/badge/Twitter-@kevalpatel2106-blue.svg?style=flat)](https://twitter.com/kevalpatel2106)

## License
Copyright 2017 Keval Patel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

