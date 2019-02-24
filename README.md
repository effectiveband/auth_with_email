# Auth via different services

## VK

https://vk.com/dev/android_sdk

Notes:

1. Add a dependence to module gradle file like this instead of using a jar or copying the wole lib:
   
   ```
   implementation ('com.vk:androidsdk:2.0.0') {
        exclude group: 'com.android.support'
    }
    ```

    Its is necessary to avoid error with different android support version. VKsdk uses 27.0.2

2. Use `AuthVkInteractor` interactor with a callback in the constructor

3. Ask to reauthorize when access to email was forbidden.

4. To get a developer SHA hash key start gradle task android -> sign report. You need a SHA1 from a output.

5. Implementation of an email getter in AuthVkInteractor.

## Facebook

https://developers.facebook.com/docs/facebook-login/android

Notes:

1. You need a convert SHA from point #4 for VK to base64.

2. Add a dependence as follow:

    ```
    implementation ('com.facebook.android:facebook-login:4.40.0')  {
        exclude group: 'com.android.support'
    }
    implementation 'com.android.support:cardview-v7:28.0.0'
    implementation 'com.android.support:customtabs:28.0.0'
    ```

    Its is necessary to avoid error with different android support version. FB uses 27.0.2

3. Do not use a LoginButton from the instruction, use a `AuthFbInteractor`