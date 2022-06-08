package ch.heigvd.statique.utils;

import com.jcraft.jsch.Channel;
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

    public static ChannelSftp connectSftp(String host, int port, String password) throws SftpException, JSchException {
        JSch jsch = new JSch();
        String[] hostSplit = host.split("@", 2);

        if (hostSplit.length == 1) {
            hostSplit = new String[] { "root", hostSplit[0] };
        }

        if (password == null) {
            jsch.addIdentity("~/.ssh/id_rsa");
        }
        jsch.setConfig("StrictHostKeyChecking", "no");

        Session jschSession = jsch.getSession(hostSplit[0], hostSplit[1], port);

        if (password != null) {
            jschSession.setPassword(password);
        }

        jschSession.connect();
        Channel channelSftp = jschSession.openChannel("sftp");
        channelSftp.connect();

        return (ChannelSftp) channelSftp;
    }

    public static void disconnect(ChannelSftp connection) {
        connection.disconnect();
    }
}