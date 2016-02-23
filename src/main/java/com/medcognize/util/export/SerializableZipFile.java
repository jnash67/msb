package com.medcognize.util.export;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.Serializable;

/* this isn't really fully Serializable for the purposes of persisting
so convert into byte array before persisting */
public class SerializableZipFile extends ZipFile implements Serializable {

    public SerializableZipFile(File zipFile) throws ZipException {
        super(zipFile);
    }
}