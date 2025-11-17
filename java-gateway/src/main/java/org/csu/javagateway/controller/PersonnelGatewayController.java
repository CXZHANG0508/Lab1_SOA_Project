package org.csu.javagateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/personnel") //
public class PersonnelGatewayController {

    //注入配置好的 RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    //定义Personnel内部服务的地址（区别于外部的8080）
    private final String PERSONNEL_SERVICE_URL = "http://localhost:8083/personnel";
    /**
     * 客户端请求:  localhost:8080/api/personnel
     * 网关转发到:  localhost:8083/personnel
     */
    //查：获取所有人员
    @GetMapping
    public ResponseEntity<?> getAllPersonnel() {
        // 调用内部服务，并获取响应
        String response = restTemplate.getForObject(PERSONNEL_SERVICE_URL, String.class);
        // 接收响应后直接转发
        return ResponseEntity.ok(response);
    }

    //查：获取特定 ID 的人员
    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonnelById(@PathVariable String id) {
        //拼接 {id} 变量
        String response = restTemplate.getForObject(PERSONNEL_SERVICE_URL + "/" + id, String.class);
        return ResponseEntity.ok(response);
    }

    //增：创建新的人员
    @PostMapping
    public ResponseEntity<?> createPersonnel(@RequestBody String requestBody) {
        // @RequestBody会捕获客户端发来的 JSON
        //转发给内部服务，设置内容格式为json，并接收内部服务返回的响应
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject(PERSONNEL_SERVICE_URL, requestEntity, String.class);
        return ResponseEntity.ok(response);
    }

    //改：更新特定人员的ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonnel(@PathVariable String id, @RequestBody String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(PERSONNEL_SERVICE_URL + "/" + id, HttpMethod.PUT, requestEntity, Void.class);
        return ResponseEntity.ok("Personnel updated successfully");
    }

    //删：删除特定人员
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonnel(@PathVariable String id) {
        String url = PERSONNEL_SERVICE_URL + "/" + id;
        //同上put方法。
        restTemplate.delete(url);
        // 如果上面没有抛出异常，说明删除成功
        return ResponseEntity.ok("Personnel deleted successfully");
    }
}
