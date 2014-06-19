package net.catchpole.compiler.writer;

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

public interface CodeWriter {
    public void print(boolean b);

    public void print(char c);

    public void print(int i);

    public void print(long l);

    public void print(float v);

    public void print(double v);

    public void print(char[] chars);

    public void print(java.lang.String s);

    public void print(java.lang.Object o);

    public void println();

    public void println(boolean b);

    public void println(char c);

    public void println(int i);

    public void println(long l);

    public void println(float v);

    public void println(double v);

    public void println(char[] chars);

    public void println(java.lang.String s);

    public void println(java.lang.Object o);

    public void indent();

    public void outdent();
}
