package poc.raja.dropbox.microservice.core.restservice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poc.raja.dropbox.microservice.core.integration.DropboxIntegration;

@RestController
public class RestApiController {
	
	@Autowired
	private DropboxIntegration integrationService;
	
	@RequestMapping(value = "/ping", method = RequestMethod.POST)
	public String getHello() {
		return new String("{\"timestamp\":\"" + new Date() + "\",\"content\":\"Ping from CaseCompositeServiceAPi\"}");
	}
	
	/**
	 * @param payLoad
	 * @return files and folders for the given path
	 */
	@CrossOrigin
	@RequestMapping(value = "/getFiles", method = RequestMethod.POST)
	public ResponseEntity<List<?>> getDropBoxFiles(@RequestBody HashMap<String, String> payLoad) {
		return integrationService.getFilesAndFolders(payLoad.get("auth"), payLoad.get("path"));
	}

	
	/**
	 * @param payLoad
	 * @return All files and folder
	 */
	@CrossOrigin
	@RequestMapping(value = "/getallfilesandfolders", method = RequestMethod.POST)
	public ResponseEntity<List<?>> getDropBoxFilesAndFolders(@RequestBody HashMap<String, String> payLoad) {
		ResponseEntity<List<?>> response =  integrationService.getAllFilesAndFolders(payLoad.get("auth"));
		return response;
	}
}
