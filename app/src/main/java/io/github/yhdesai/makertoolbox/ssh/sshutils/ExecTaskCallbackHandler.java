package io.github.yhdesai.makertoolbox.ssh.sshutils;


public interface ExecTaskCallbackHandler {

    void onFail();

    void onComplete(String completeString);
}
