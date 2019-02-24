# Auth via different services

## VK

https://vk.com/dev/android_sdk

Differences

1. Add a dependence to module gradle file like this instead of using a jar or copying the wole lib:
   
   `implementation ('com.vk:androidsdk:2.0.0') {
        exclude group: 'com.android.support'
    }`

2. Use AuthVkInteractor interactor with a callback in the constructor

3. Ask to reauthorize when access to email was forbidden.