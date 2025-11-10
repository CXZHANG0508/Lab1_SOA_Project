package org.csu.javagateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/borrows") // 1. 监听 /api/borrows
public class BorrowGatewayController {

    //注入配置好的 RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    //定义Borrow内部服务的地址（区别于外部的8080）
    private final String BORROW_SERVICE_URL = "http://localhost:8081/borrows"; // 2. 端口 8081, 路径 /borrows

    /**
     * 客户端请求:  localhost:8080/api/borrows
     * 网关转发到:  localhost:8081/borrows
     */
    //查：获取所有借用记录
    @GetMapping
    public ResponseEntity<?> getAllBorrows() {
        String response = restTemplate.getForObject(BORROW_SERVICE_URL, String.class);
        return ResponseEntity.ok(response);
    }

    //查：获取特定 ID 的借用记录
    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowById(@PathVariable String id) {
        String response = restTemplate.getForObject(BORROW_SERVICE_URL + "/" + id, String.class);
        return ResponseEntity.ok(response);
    }

    //增：创建新的借用记录
    @PostMapping
    public ResponseEntity<?> createBorrow(@RequestBody String requestBody) {
        String response = restTemplate.postForObject(BORROW_SERVICE_URL, requestBody, String.class);
        return ResponseEntity.ok(response);
    }

    //改：更新特定借用记录的ID
    // (注意: 我们的 API 设计中 '修改' 借用记录不常见，但 '归还' 是 DELETE，我们保持模板一致性)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrow(@PathVariable String id, @RequestBody String requestBody) {
        String url = BORROW_SERVICE_URL + "/" + id;
        restTemplate.put(url, requestBody);
        return ResponseEntity.ok("Borrow record updated successfully");
    }

    //删：删除特定借用记录 (即“归还”)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrow(@PathVariable String id) {
        String url = BORROW_SERVICE_URL + "/" + id;
        restTemplate.delete(url);
        return ResponseEntity.ok("Borrow record deleted successfully (returned)");
    }
}