package net.catchpole.airline;

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

import java.util.HashMap;
import java.util.Map;

public class Airline {
    private Map<String, Aircraft> aircraft = new HashMap<String, Aircraft>();
    private String code;
    private String name;

    public Airline() {
    }

    public void addAircraft(Aircraft employee) {
        aircraft.put(employee.getName(), employee);
    }

    public Aircraft getAircraft(String name) {
        return aircraft.get(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + this.code + ' ' + this.name + ' ' + this.aircraft;
    }
}
