package com.ptit.hackerthonservice.ws.client;


import com.ptit.hackerthonservice.ws.dto.Request;
import com.ptit.hackerthonservice.ws.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "RemoteCodeCompiler", url = "http://103.179.191.78:8080")
public interface RemoteCompilerService {

	@PostMapping("/api/compile/json")
	public Response compile(@RequestHeader("API_KEY") String apiKey, @RequestBody Request request);
}
