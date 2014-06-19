package net.catchpole.io;

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

import java.io.IOException;
import java.io.OutputStream;

/**
 */
public class DispatchingOutputStream extends OutputStream {
    private byte[] data;
    private long blockNumber;

    public DispatchingOutputStream() {
    }

    public void processOutputStream(OutputStream os) throws IOException {
        long localBlockNumber = -1;

        Thread thread = Thread.currentThread();
        int oldPriority = thread.getPriority();
        thread.setPriority(2);
        try {
            byte[] buffer;
            synchronized (this) {
                if (data == null) {
                    this.wait();
                }
                buffer = new byte[data.length];
                System.arraycopy(data, 0, buffer, 0, data.length);
            }
            for (; ;) {
                os.write(buffer);
                os.flush();
                synchronized (this) {
                    // only synchronize on the wait.  we can't have blocked writes hold up other threads
                    this.wait();

//                    if (localBlockNumber == -1) {
//                        localBlockNumber = blockNumber;
//                    } else {
//                        if (++localBlockNumber != blockNumber) {
//                            throw new IOException("Dispatching streams out of sync");
//                        }
//                    }

                    System.arraycopy(data, 0, buffer, 0, data.length);
                }
            }
        } catch (InterruptedException ie) {
            throw new IOException(ie.getClass().getName() + ' ' + ie.getMessage());
        } finally {
            thread.setPriority(oldPriority);
        }
    }

    public void write(int b) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void write(byte b[], int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    public synchronized void write(byte data[]) throws IOException {
        // copy data be to thread safe with the processing threads
        if (this.data == null || this.data.length != data.length) {
            this.data = new byte[data.length];
        }
        System.arraycopy(data, 0, this.data, 0, data.length);
        blockNumber++;
        this.notifyAll();
    }
}
