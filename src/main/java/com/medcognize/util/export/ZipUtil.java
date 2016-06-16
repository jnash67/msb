package com.medcognize.util.export;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.medcognize.domain.User;
import com.vaadin.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

@Slf4j
public class ZipUtil implements Serializable {

    public static ZipParameters getZipParameters(final char[] password, String filename) {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword(password);
        parameters.setFileNameInZip(filename);
        parameters.setSourceExternalStream(true);
        return parameters;
    }

    // NOTE: zip4j is supposed to be able to delete an entry from a zip
    // couldn't get removing an existing entry to work, so when something changes we have to re-create
    // the entire zip file
    @SuppressWarnings("UnusedDeclaration")
    public static void writeCsvEntryToZip(SerializableZipFile szf, char[] password, String fileName, String csv) {
        //		try {
        //			log.warn("Headers BEFORE writing (" + fileName + ")--> " + szf.getFileHeaders().size());
        //		} catch (ZipException e) {
        //			e.printStackTrace();
        //		}
        ListAllFilesInZipFile(szf);
        try {
            szf.removeFile(fileName);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        File f = new File(fileName);
        try {
            Files.write(csv.getBytes(), f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipParameters zp = getZipParameters(password, fileName);
        FileHeader fh = null;
        try {
            boolean b = szf.isEncrypted();
            ZipUtil.log.warn("Is encrypted --> " + b);
            szf.setPassword(password);
            fh = szf.getFileHeader(fileName);
        } catch (
        //			if (null != fh) {
        //				szf.removeFile(fh);
        //			}
        ZipException e) {
            e.printStackTrace();
        }
        if (null == fh) {
            ZipUtil.log.warn("entry should already exist in zip file but does not -- " + fileName);
        }
        try {
            szf.addFile(f, zp);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        //		try (ByteArrayInputStream bais = new ByteArrayInputStream(csv.getBytes())) {
        //			szf.addStream(bais, zp);
        //		} catch (ZipException | IOException e) {
        //			e.printStackTrace();
        //		}
        //		try {
        //			log.warn("Headers AFTER writing (" + fileName + ")--> " + szf.getFileHeaders().size());
        //		} catch (ZipException e) {
        //			e.printStackTrace();
        //		}
    }

    public static String getCsvEntryFromZip(SerializableZipFile szf, String entry) {
        FileHeader fh = getZipEntryFileHeader(szf, entry);
        if (null == fh) {
            return "";
        }
        return getCsvEntryFromZip(szf, fh);
    }

    public static FileHeader getZipEntryFileHeader(SerializableZipFile szf, String entry) {
        try {
            return szf.getFileHeader(entry);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCsvEntryFromZip(SerializableZipFile szf, FileHeader fileHeader) {
        String csv = "";
        ZipInputStream is;
        ByteArrayOutputStream os;
        try {
            if (fileHeader != null) {
                //Get the InputStream from the ZipFile
                is = szf.getInputStream(fileHeader);
                //Initialize the output stream
                os = new ByteArrayOutputStream();
                ByteStreams.copy(is, os);
                csv = os.toString();
                //Closing inputstream also checks for CRC of the the just extracted file.
                //If CRC check has to be skipped (for ex: to cancel the unzip operation, etc)
                //use method is.close(boolean skipCRCCheck) and set the flag,
                //skipCRCCheck to false
                //NOTE: It is recommended to close outputStream first because Zip4j throws
                //an exception if CRC check fails
                is.close();
                //Close output stream
                os.close();
            } else 
            //log.warn("Done extracting: " + fileHeader.getFileName());
            {
                ZipUtil.log.error("FileHeader does not exist");
            }
        } catch (ZipException | IOException e) {
            e.printStackTrace();
        }
        return csv;
    }

    public static byte[] getEmptyZipEntries(final char[] password) {
        byte[] bytesToWrite = "".getBytes();
        InMemoryOutputStream inMemoryOutputStream = new InMemoryOutputStream();
        ZipOutputStream zos = new ZipOutputStream(inMemoryOutputStream);
        for (String entry : MedcognizeExportFileFormat.entryFileNames) {
            writeZipEntry(zos, entry, bytesToWrite, password);
        }
        try {
            zos.finish();
            zos.close();
            return inMemoryOutputStream.getZipContent();
        } catch (IOException | ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeZipEntry(ZipOutputStream zos, String entry, byte[] bytesToWrite, final char[] password) {
        ZipParameters parameters = getZipParameters(password, entry);
        try {
            zos.putNextEntry(null, parameters);
            zos.write(bytesToWrite);
            zos.closeEntry();
        } catch (ZipException | IOException e1) {
            e1.printStackTrace();
        }
    }

    public static SerializableZipFile createEmptyInMemoryZipFile(final String name, final char[] password) {
        byte[] emptyContent = getEmptyZipEntries(password);
        return createInMemoryZipFile(name, emptyContent);
    }

    public static SerializableZipFile createInMemoryZipFile(final String name, final byte[] content) {
        FileOutputStream os;
        try {
            File inMemoryCreatedFile = new File(name);
            os = new FileOutputStream(inMemoryCreatedFile);
            os.write(content);
            return new SerializableZipFile(inMemoryCreatedFile);
        } catch (ZipException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StreamResource createDownloadZipFileStreamResource(final User u) {
        String filename = "medcognize.zip";
        String mimetype = "application/zip";
        StreamResource.StreamSource ss = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                FileInputStream fis;
                try {
                    SerializableZipFile f = MedcognizeExportFileFormat.createFileFromUser(u);
                    fis = new FileInputStream(f.getFile());
                    ZipUtil.ListAllFilesInZipFile(f);
                    return fis;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        StreamResource sr = new StreamResource(ss, filename);
        sr.setMIMEType(mimetype);
        return sr;
    }

    public static void ListAllFilesInZipFile(ZipFile zipFile) {
        try {
            // Get the list of file headers from the zip file
            List fileHeaderList = zipFile.getFileHeaders();
            // Loop through the file headers
            for (Object aFileHeaderList : fileHeaderList) {
                FileHeader fileHeader = (FileHeader) aFileHeaderList;
                // FileHeader contains all the properties of the file
                ZipUtil.log.warn("****File Details for: " + fileHeader.getFileName() + "*****");
                ZipUtil.log.warn("Name: " + fileHeader.getFileName());
                ZipUtil.log.warn("Compressed Size: " + fileHeader.getCompressedSize());
                ZipUtil.log.warn("Uncompressed Size: " + fileHeader.getUncompressedSize());
                ZipUtil.log.warn("CRC: " + fileHeader.getCrc32());
                ZipUtil.log.warn("************************************************************");
                // Various other properties are available in FileHeader. Please have a look at FileHeader
                // class to see all the properties
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
