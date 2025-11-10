package org.csu.javagateway.controller;

import org.csu.javagateway.dto.BorrowRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrchestrationGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    // 定义需要的内部服务地址
    private final String BORROW_SERVICE_URL = "http://localhost:8081/borrows";
    private final String MATERIAL_SERVICE_URL = "http://localhost:8082/materials";

    /**
     * 客户端请求: POST localhost:8080/api/borrow-material
     * 请求体 Body: { "personnelId": "p001", "materialId": "m002" }
     */

    @PostMapping("/api/borrow-material")
    public ResponseEntity<?> borrowMaterial(@RequestBody BorrowRequestDTO borrowRequest) {

        // 调用 Python 服务 (8081)，创建借用记录,构建它需要的 JSON 请求体
        Map<String, String> borrowRequestBody = new HashMap<>();
        borrowRequestBody.put("personnelId", borrowRequest.getPersonnelId());
        borrowRequestBody.put("materialId", borrowRequest.getMaterialId());
        borrowRequestBody.put("borrowDate", Instant.now().toString());

        //  Python 服务返回的 JSON 字符串
        String newBorrowRecord = restTemplate.postForObject(
                BORROW_SERVICE_URL,
                borrowRequestBody,
                String.class
        );

        //调用 Node.js 服务 (8082)，减少物资库存,构建它需要的 JSON 请求体
        // 目前假设 Node.js 能理解这个格式：{ "action": "decrease_stock", "quantity": 1 })
        Map<String, Object> materialUpdateBody = new HashMap<>();
        materialUpdateBody.put("action", "decrease_stock");
        materialUpdateBody.put("quantity", 1);

        // 构造物资服务的 URL (目前假设为: .../materials/m002)
        String materialUrl = MATERIAL_SERVICE_URL + "/" + borrowRequest.getMaterialId();

        //发送 PUT 请求来更新库存
        restTemplate.put(materialUrl, materialUpdateBody);

        //两个调用都成功，则返回响应
        //把从 Python 服务拿到的借用记录返回给客户端
        return ResponseEntity.ok(newBorrowRecord);
    }
}