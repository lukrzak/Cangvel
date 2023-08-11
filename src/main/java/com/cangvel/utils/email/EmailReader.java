package com.cangvel.utils.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Component
public class EmailReader {

    @Value("${application.mail.password}")
    private String password;

    @Value("${application.mail.username}")
    private String username;

    /**
     * Connects to inbox and returns all attachments in list. Closes connection with inbox after. Requires proper
     * configuration by passing environment variables. Throws Runtime exception when failing to connect to inbox.
     *
     * @return list of all attachments as File type.
     */
    public List<File> getAttachments() {
        List<File> files;
        Properties props = getEmailProperties();
        Session session = Session.getDefaultInstance(props);
        try (Store store = session.getStore("imap")) {
            store.connect(username, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            files = mapMultipartToFile(messages);
            inbox.close();
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Cannot read emails");
        }

        return files;
    }

    private Properties getEmailProperties() {
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.host", "imap.gmail.com");
        props.setProperty("mail.imap.port", "993");

        return props;
    }

    private List<File> mapMultipartToFile(Message[] messages) throws MessagingException, IOException {
        List<File> files = new LinkedList<>();
        for (Message message : messages) {
            if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();
                files.add(fetchFileFromMultipart(multipart));
            }
        }

        return files.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    private File fetchFileFromMultipart(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bp = multipart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                String fileName = bp.getFileName();
                return convertMessageAttachmentToFileType(bp.getInputStream(), fileName);
            }
        }
        return null;
    }

    private File convertMessageAttachmentToFileType(InputStream is, String fileName) throws IOException {
        File tempFile = File.createTempFile(fileName, ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1)
                fos.write(buffer, 0, bytesRead);
        }
        return tempFile;
    }
}
