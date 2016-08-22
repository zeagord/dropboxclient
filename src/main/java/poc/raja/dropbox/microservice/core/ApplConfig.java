package poc.raja.dropbox.microservice.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Component
public class ApplConfig {
	
	@Value("${dropboxurl}")
	private String url;
	
    @Autowired
    public ApplConfig(@Value("${dropboxurl}") String url) {
        this.url = url;
    }
    
	public String getDropBoxUrl() {
		return url;
	}
}
