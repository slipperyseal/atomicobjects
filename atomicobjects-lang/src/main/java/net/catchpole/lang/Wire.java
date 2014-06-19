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

public class Wire {
    private final boolean resettable;
    private boolean tripped = false;

    public Wire() {
        this.resettable = true;
    }

    public Wire(boolean resettable) {
        this.resettable = resettable;
    }

    public Wire(boolean resettable, boolean tripped) {
        this.resettable = resettable;
        this.tripped = tripped;
    }

    public boolean trip() {
        try {
            return tripped;
        } finally {
            tripped = true;
        }
    }

    public boolean reset() {
        if (!resettable) {
            throw new IllegalStateException("Wire not resettable");
        }
        try {
            return tripped;
        } finally {
            tripped = false;
        }
    }

    public boolean isTripped() {
        return tripped;
    }
}
