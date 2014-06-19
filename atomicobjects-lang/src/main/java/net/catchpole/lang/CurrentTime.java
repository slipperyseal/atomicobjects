package net.catchpole.lang;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import java.util.Date;

/**
 * A container for a Date object which represents the current time when accessed,
 * not when the object was created.
 * <p/>
 * <p>Calling <code>toString()</code> on an instance of this
 * class will return the time the call was made.
 */

public class CurrentTime {
    private Date currentTime;

    public Date getCurrentTime() {
        return currentTime = new Date();
    }

    /**
     * Returns the Date object which was used the last time this object was accessed.
     *
     * @return the Date object which was used the last time this object was accessed.
     */
    public Date getLastTime() {
        return currentTime;
    }

    /**
     * Returns the toString of result of a Date object for the current time.
     *
     * @return the toString of result of a Date object for the current time.
     */
    public String toString() {
        return getCurrentTime().toString();
    }
}
