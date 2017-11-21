package com.farebid.userservice.controller;

import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@RestController
@RequestMapping(value = "/file")
public class FileController {

  @RequestMapping(value = "/upload", method = RequestMethod.GET)
  public void fileUpload(String fileName, String fileContent){
    byte[] file = Base64.getDecoder().decode(fileContent);
    System.err.println(file);
    Storage storage = StorageOptions.getDefaultInstance().getService();
    BlobId blobId = BlobId.of("test-farebid", fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
    System.err.println("before");
    Blob blob = storage.create(blobInfo, file);
    System.err.println("after");
  }
}
