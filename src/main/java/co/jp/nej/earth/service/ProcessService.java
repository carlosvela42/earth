package co.jp.nej.earth.service;

public interface ProcessService {
    String openProcess(String token, String workspaceId, String processId, String workItemId);
    String closeProcess(String token, String workspaceId, String processId);
    Process getProcess(String token, String workspaceId, String processId);
    String updateProcess(String token, String workspaceId, Process process);
}
