package net.catchpole.web.paths;

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

import net.catchpole.web.http.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PathResolver {
    private Map<String, PathHandler> hashMap = new HashMap<String, PathHandler>();

    public PathResolver() {
    }

    public void addDefaultHandler(PathHandler pathHandler) {
        addHandler("", pathHandler);
    }

    public void addHandler(String category, PathHandler pathHandler) {
        hashMap.put(category, pathHandler);
    }

    public boolean handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String[] path = HttpUtils.pathToArray(req);
        findPathHandler(path).handle(path, req, res);
        return true;
    }

    public long getLastModified(HttpServletRequest req) throws IOException {
        String[] path = HttpUtils.pathToArray(req);
        return findPathHandler(path).getLastModified(path, req);
    }

    private PathHandler findPathHandler(String[] path) throws IOException {
        String category = (path != null && path.length > 0 ? path[0] : "");
        PathHandler pathHandler = hashMap.get(category);
        if (pathHandler == null) {
            pathHandler = hashMap.get("");
        }
        return pathHandler;
    }
}
