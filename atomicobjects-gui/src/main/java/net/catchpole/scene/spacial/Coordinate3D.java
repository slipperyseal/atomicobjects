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

public class Coordinate3D extends Coordinate2D {
    private float z;

    public Coordinate3D(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    public Coordinate3D(Coordinate2D coordinate2D, float z) {
        super(coordinate2D);
        this.z = z;
    }

    public Coordinate3D(Coordinate3D coordinate3D) {
        super(coordinate3D);
        this.z = coordinate3D.z;
    }

    public void add(Coordinate3D coordinate3D) {
        super.setX(super.getX() + coordinate3D.getX());
        super.setY(super.getY() + coordinate3D.getY());
        this.z += coordinate3D.z;
    }

    public void subtract(Coordinate3D coordinate3D) {
        super.setX(super.getX() - coordinate3D.getX());
        super.setY(super.getY() - coordinate3D.getY());
        this.z -= coordinate3D.z;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
