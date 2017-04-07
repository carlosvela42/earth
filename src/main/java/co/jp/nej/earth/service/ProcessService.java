package co.jp.nej.earth.service;

public interface ProcessService {
    public String openProcess(String token, String workspaceId, String processId, String workItemId);
    public String closeProcess(String token, String workspaceId, String processId);
    public Process getProcess(String token, String workspaceId, String processId);
    public String updateProcess(String token, String workspaceId, Process process);
}
