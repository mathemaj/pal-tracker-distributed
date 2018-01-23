package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private ConcurrentHashMap<Long, ProjectInfo> cacheMap;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
        this.cacheMap = new ConcurrentHashMap<>();
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo retVal = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        cacheMap.put(projectId,retVal);
        return retVal;
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        return cacheMap.get(projectId);
    }
}
