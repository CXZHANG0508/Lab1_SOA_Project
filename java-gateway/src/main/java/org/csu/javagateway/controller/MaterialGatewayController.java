package org.csu.javagateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/materials") // 1. 监听 /api/materials
public class MaterialGatewayController {

    //注入配置好的 RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    //定义Material内部服务的地址（区别于外部的8080）
    private final String MATERIAL_SERVICE_URL = "http://localhost:8082/materials"; // 2. 端口 8082, 路径 /materials

    /**
     * 客户端请求:  localhost:8080/api/materials
     * 网关转发到:  localhost:8082/materials
     */
    //查：获取所有物资
    @GetMapping
    public ResponseEntity<?> getAllMaterials() {
        String response = restTemplate.getForObject(MATERIAL_SERVICE_URL, String.class);
        return ResponseEntity.ok(response);
    }

    //查：获取特定 ID 的物资
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable String id) {
        String response = restTemplate.getForObject(MATERIAL_SERVICE_URL + "/" + id, String.class);
        return ResponseEntity.ok(response);
    }

    //增：创建新的物资
    @PostMapping
    public ResponseEntity<?> createMaterial(@RequestBody String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject(MATERIAL_SERVICE_URL, requestEntity, String.class);
        return ResponseEntity.ok(response);
    }

    //改：更新特定物资的ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable String id, @RequestBody String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                MATERIAL_SERVICE_URL + "/" + id,
                HttpMethod.PUT,
                request,
                String.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    //删：删除特定物资
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable String id) {
        String url = MATERIAL_SERVICE_URL + "/" + id;
        restTemplate.delete(url);
        return ResponseEntity.ok("Material deleted successfully");
    }
}