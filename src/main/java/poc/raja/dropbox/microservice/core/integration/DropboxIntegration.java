package poc.raja.dropbox.microservice.core.integration;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import poc.raja.dropbox.microservice.core.ApplConfig;

/**
 * @author rg3
 *
 */
@Component
public class DropboxIntegration {
	@Autowired
	ApplConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
	private static final Logger LOG = Logger.getLogger(DropboxIntegration.class);

	public ResponseEntity<List<?>> getFilesAndFolders(String authToken, String path) {

		try {

			String payload = "{\"path\": \"" + path
					+ "\",\"recursive\": false,\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";
			JSONObject responseJson = getFilesOrFolders(authToken, payload);
			// The entries from the response will always be a list according the
			// API, hence adding suppress warnings
			List<?> fileList = (List<?>) responseJson.get("entries");
			return new ResponseEntity<>(fileList, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}

	/**
	 * @param authToken
	 * @return Files and Folders
	 */
	public ResponseEntity<List<?>> getAllFilesAndFolders(String authToken) {

		try {
			System.out.println(appConfig.getDropBoxUrl());
			String url = appConfig.getDropBoxUrl() + "/1/delta";
			HttpEntity<?> request = new HttpEntity<Object>(getHeaders(authToken));
			ResponseEntity<String> resultStr = restTemplate.postForEntity(url, request, String.class);
			JSONObject responseJson = response2Object(resultStr);
			// The entries from the response will always be a list according the
			// API, hence adding suppress warnings
			List<?> fileList = (List<?>) responseJson.get("entries");
			return new ResponseEntity<>(fileList, HttpStatus.OK);

		} catch (ParseException parseEx) {
			LOG.error(parseEx);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * @param authToken
	 * @param payload
	 * @return
	 * @throws ParseException
	 */
	private JSONObject getFilesOrFolders(String authToken, String payload) throws ParseException {
		String url = appConfig.getDropBoxUrl() + "/2/files/list_folder";
		HttpEntity<?> request = new HttpEntity<Object>(payload, getHeaders(authToken));
		ResponseEntity<String> resultStr = restTemplate.postForEntity(url, request, String.class);
		JSONObject responseJson = response2Object(resultStr);
		return responseJson;
	}

	/**
	 * @param authToken
	 * @return HttpHeaders
	 */
	private HttpHeaders getHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + authToken);// "yrfs5Sfq0zAAAAAAAAAADUjINoo3xmgwVx6kqkoJc-Zodypc-80adJo4uAnEowi6");
		headers.add("Accept-Charset", "UTF-8");
		return headers;
	}

	/**
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	private JSONObject response2Object(ResponseEntity<String> response) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject newJObject = (JSONObject) parser.parse(response.getBody());
		return newJObject;
	}
}
