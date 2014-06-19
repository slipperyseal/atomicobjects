package net.catchpole.model.annotation;

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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Maps method arguments to class wide property values.
 *
 * @Argument("width, height, Style.getColor() = color, ?")
 * @Result("Image.getWidth() = width; Image.getHeight() = height")
 * <p/>
 * public Image renderImage(int x, int y, Style style, Source source) {
 * <p/>
 * This indicates that there is a relationship of width and height between the input
 * arguments and the getter methods on the image returned.  it also indicates that there
 * is no known or relevant relationship for the source parameter as indicated by the question
 * mark.
 * <p/>
 * The name space for properties is class wide, therefor other methods or constructors may
 * also indicate their relationships to the same property names.
 * <p/>
 * As method parameter names are not retained in the class file, they can not be referenced
 * in the Argument and Result definitions.  eg. The naming of arguments x and y are
 * irrelevant to the mapping of width and height.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String value();
}
