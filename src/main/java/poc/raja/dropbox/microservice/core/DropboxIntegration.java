package poc.raja.dropbox.microservice.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class DropboxIntegration {
	private RestTemplate restTemplate = new RestTemplate();
	private static final Logger LOG = Logger.getLogger(DropboxIntegration.class);
	public ResponseEntity<List<?>> getFilesAndFolders(String authToken, String path) {

		try {
			
	        String payload = "{\"path\": \""+ path + "\",\"recursive\": false,\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";
	        JSONObject responseJson = getFilesOrFolders(authToken, payload);
			// The entries from the response will always be a list according the API, hence adding suppress warnings
			List<?> fileList =(List<?>) responseJson.get("entries");
	        return new ResponseEntity<>(fileList, HttpStatus.OK);

		} catch(Exception e) {
			System.out.println(e);
		}
		return null;

	}
	
	public ResponseEntity<List<?>> getAllFilesAndFolders(String authToken){
		
		try {
			String url = "https://api.dropboxapi.com/1/delta";
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.add("Authorization", "Bearer " + authToken);
	        headers.add("Accept-Charset", "UTF-8");
	        HttpEntity<?> request = new HttpEntity<Object>(headers);
			ResponseEntity<String> resultStr = restTemplate.postForEntity(url, request, String.class);
			JSONObject responseJson = response2Object(resultStr);
			// The entries from the response will always be a list according the API, hence adding suppress warnings
			List<?> fileList =(List<?>) responseJson.get("entries");
	        return new ResponseEntity<>(fileList, HttpStatus.OK);
			
		} catch(ParseException parseEx){
			LOG.error(parseEx);
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	private JSONObject getFilesOrFolders(String authToken, String payload) throws ParseException {
		String url = "https://api.dropboxapi.com/2/files" + "/list_folder";
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + authToken);//"yrfs5Sfq0zAAAAAAAAAADUjINoo3xmgwVx6kqkoJc-Zodypc-80adJo4uAnEowi6");
        headers.add("Accept-Charset", "UTF-8");
        HttpEntity<?> request = new HttpEntity<Object>(payload, headers);
		ResponseEntity<String> resultStr = restTemplate.postForEntity(url, request, String.class);
		JSONObject responseJson = response2Object(resultStr);
		return responseJson;
	}
	
	private JSONObject response2Object(ResponseEntity<String> response) throws ParseException {
			JSONParser parser = new JSONParser();
			JSONObject newJObject = (JSONObject) parser.parse(response.getBody());
			return newJObject;
	}
}
