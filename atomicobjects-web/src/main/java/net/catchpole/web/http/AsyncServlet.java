package net.catchpole.web.http;

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

import net.catchpole.io.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A simple comet servlet. have a nice day.
 */
public class AsyncServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        HttpSession session = httpServletRequest.getSession(true);
        BlockingQueue<InputStream> blockingQueue = getQueue(session);

        InputStream inputStream = null;
        try {
            inputStream = (InputStream) blockingQueue.poll(25000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
        }

        if (inputStream != null) {
            Arrays.spool(inputStream, httpServletResponse.getOutputStream());
        }
    }

    public void feed(HttpSession httpSession, InputStream inputStream) {
        getQueue(httpSession).add(inputStream);
    }

    private BlockingQueue<InputStream> getQueue(HttpSession session) {
        BlockingQueue<InputStream> blockingQueue = (BlockingQueue<InputStream>) session.getAttribute(this.getClass().getName());
        if (blockingQueue == null) {
            blockingQueue = new ArrayBlockingQueue<InputStream>(100);
            session.setAttribute(this.getClass().getName(), blockingQueue);
        }
        return blockingQueue;
    }
}
