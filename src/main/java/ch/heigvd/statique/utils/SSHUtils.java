package ch.heigvd.statique.utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHUtils {
    public static void cleanRemote(ChannelSftp connection, String remotePath) throws SftpException {
        connection.rmdir(remotePath);
        connection.mkdir(remotePath);
    }

    public static void copy(ChannelSftp connection, String localPath, String remotePath) throws SftpException {
        connection.put(localPath, remotePath);
    }

    public static ChannelSftp connectSftp(String host, String user, int port, String password) throws SftpException, JSchException {
        JSch jsch = new JSch();
        Session jschSession = jsch.getSession(user, host, port);
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }
}