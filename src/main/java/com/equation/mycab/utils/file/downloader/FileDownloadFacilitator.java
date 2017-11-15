package com.equation.mycab.utils.file.downloader;

import java.io.File;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

/**
 *
 * @author Wellington
 */
public class FileDownloadFacilitator {
	/**
	 * 
	 * @param path
	 *            The File path
	 * @param download
	 *            The download button
	 */
	public static void downloadFile(String path, Button download) {
		Resource res = new FileResource(new File(path));
		FileDownloader fd = new FileDownloader(res);
		fd.extend(download);
	}

}
