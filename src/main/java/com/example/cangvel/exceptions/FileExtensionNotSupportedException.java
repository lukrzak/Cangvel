package com.example.cangvel.exceptions;

public class FileExtensionNotSupportedException extends Exception{
    public FileExtensionNotSupportedException() {
        super("File with given extension is not supported by file analyser.");
    }
}
