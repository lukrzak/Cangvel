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

    public List<File> getAttachments() {
        List<File> files = new LinkedList<>();
        Properties props = getEmailProperties();
        Session session = Session.getDefaultInstance(props);
        try (Store store = session.getStore("imap")) {
            store.connect(username, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();
                    files.add(fetchFileFromMultipart(multipart));
                }
            }
            inbox.close();
            return files.stream()
                    .filter(Objects::nonNull).toList();
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Cannot read emails");
        }
    }

    private Properties getEmailProperties() {
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.host", "imap.gmail.com");
        props.setProperty("mail.imap.port", "993");

        return props;
    }

    private File convertPdfToFileType(InputStream is, String fileName) {
        try {
            File tempFile = File.createTempFile(fileName, ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) fos.write(buffer, 0, bytesRead);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private File fetchFileFromMultipart(Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bp = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                    String fileName = bp.getFileName();
                    return convertPdfToFileType(bp.getInputStream(), fileName);
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
