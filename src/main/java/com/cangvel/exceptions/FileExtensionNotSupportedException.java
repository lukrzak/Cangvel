package com.cangvel.exceptions;

import java.io.IOException;

public class FileExtensionNotSupportedException extends IOException {
    public FileExtensionNotSupportedException() {
        super("File with given extension is not supported by file analyser.");
    }
}
