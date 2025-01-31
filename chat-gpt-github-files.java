import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;
import java.util.Map;

public interface GitHubFeignClient {
    
    @RequestLine("GET /repos/{owner}/{repo}/pulls/{pull_number}/files")
    @Headers("Authorization: token {token}")
    List<Map<String, Object>> getPullRequestFiles(@Param("owner") String owner, 
                                                  @Param("repo") String repo, 
                                                  @Param("pull_number") int pullNumber, 
                                                  @Param("token") String token);
    
    @RequestLine("GET /repos/{owner}/{repo}/commits/{commit_sha}")
    @Headers("Authorization: token {token}")
    Map<String, Object> getCommitDetails(@Param("owner") String owner, 
                                         @Param("repo") String repo, 
                                         @Param("commit_sha

