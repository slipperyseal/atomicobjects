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

/**
 * Allows operations (such as graphics painting) to be speed limited.
 * Calling the timeout() method will block if required, to ensure the
 * timeout period (or possibly more) has elapsed since the object was constructed.
 * The timer is reset after each timeout call so that subsequent calls to timeout will
 * again wait the elapsed time.
 */
public class SpeedLimiter {
    private final long duration;
    private long until;

    public SpeedLimiter(long duration) {
        this.duration = duration;
        reset();
    }

    /**
     * @return true if the limiter had to wait.
     */
    public boolean timeout() throws InterruptedException {
        long now = System.currentTimeMillis();
        long delay = this.until - now;
        this.until = this.duration + now;

        if (delay > 0) {
            Thread.sleep(delay);
            return true;
        }
        return false;
    }

    public void reset() {
        this.until = this.duration + System.currentTimeMillis();
    }
}
