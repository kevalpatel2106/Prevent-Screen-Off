/*
 * Copyright 2016 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel.preventscreenoff;

/**
 * Created by Keval on 28-Oct-16.
 * This class contains different error codes, that can occur during eye tracking.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class Errors {

    private Errors() {
        throw new RuntimeException("Cannot create new instance of this class.");
    }

    /**
     * This error code indicates that error is undefined. This will stop the eye tracker and will not
     * prevent screen off automatically.
     * <p>
     * Developer should make sure if the user is connected to the internet for the first run. Play Services
     * Vision Api needs internet connection while running for the first time to download required files.
     */
    public static final int UNDEFINED = 0;

    /**
     * This indicates that camera permission is not available. This will stop the eye tracker and will not
     * prevent screen off automatically.
     * <p>
     * In this case developer should ask for the camera permission at runtime and if the camera permission
     * is granted than SDK will reinitialize the eye tracker.
     */
    public static final int CAMERA_PERMISSION_NOT_AVAILABLE = 1;

    /**
     * This error code indicates that device dose not have the front camera. This will stop the eye
     * tracker and will not prevent screen off automatically.
     */
    public static final int FRONT_CAMERA_NOT_AVAILABLE = 2;

    /**
     * This error code indicates that there is low light environment around. So eye detector cannot
     * detect user face/eyes. This will stop the eye tracker and will not
     * prevent screen off automatically.
     */
    public static final int LOW_LIGHT = 3;

    /**
     * This error code indicates that the device does not have the Google Play Services available or
     * updated. The SDK will display the error dialog it self. This will stop the eye tracker and will not
     * prevent screen off automatically.
     */
    public static final int PLAY_SERVICE_NOT_AVAILABLE = 4;
}
