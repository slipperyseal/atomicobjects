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

public class Position extends Coordinate3D {
    private float rotationX;
    private float rotationY;

    public Position(float x, float y, float z, float rotationX, float rotationY) {
        super(x, y, z);
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }

    public Position(Coordinate3D coordinate3D, float rotationX, float rotationY) {
        super(coordinate3D);
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }

    public float getRotationX() {
        return this.rotationX;
    }

    public float getRotationY() {
        return this.rotationY;
    }
}
