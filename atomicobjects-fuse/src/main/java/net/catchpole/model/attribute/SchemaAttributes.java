package net.catchpole.model.attribute;

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

public interface SchemaAttributes {
    /**
     * Returns true if the schema for this GraphIterator allows child nodes, regardless
     * of how many actually exist.
     */
    public boolean hasAny();

    /**
     * Returns true if the schema for this GraphIterator allows more than one child nodes, regardless
     * of how many actually exist.
     */
    public boolean hasMany();

    /**
     * Returns true if the schema for this GraphIterator requires child node or nodes, regardless
     * of how many actually exist.
     */
    public boolean isRequired();
}
