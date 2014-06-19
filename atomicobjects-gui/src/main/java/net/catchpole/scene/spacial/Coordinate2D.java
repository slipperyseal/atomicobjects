package net.catchpole.scene.spacial;

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

public class Coordinate2D {
    private float x;
    private float y;

    public Coordinate2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate2D(Coordinate2D coordinate2D) {
        this.x = coordinate2D.x;
        this.y = coordinate2D.y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void add(Coordinate2D coordinate2D) {
        this.x += coordinate2D.x;
        this.y += coordinate2D.y;
    }

    public void subtract(Coordinate2D coordinate2D) {
        this.x -= coordinate2D.x;
        this.y -= coordinate2D.y;
    }
}
