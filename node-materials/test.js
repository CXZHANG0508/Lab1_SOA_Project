const axios = require("axios");

const BASE_URL = "http://localhost:8082/materials";

// 用来打印结果
function printResult(title, response) {
    console.log("==== " + title + " ====");
    console.log("Status:", response.status);
    console.log("Data:", JSON.stringify(response.data, null, 2));
    console.log("===============================\n");
}

async function runTests() {
    try {
        // 1. 获取全部物资
        let res = await axios.get(BASE_URL);
        printResult("GET /materials", res);

        // 2. 创建新物资
        res = await axios.post(BASE_URL, {
            name: "测试试管架",
            stock: 30,
            category: "工具",
            location: "A-202"
        });
        printResult("POST /materials", res);

        const createdId = res.data.material_id;

        // 3. 获取新建物资
        res = await axios.get(`${BASE_URL}/${createdId}`);
        printResult("GET /materials/:id", res);

        // 4. 更新物资
        res = await axios.put(`${BASE_URL}/${createdId}`, {
            name: "试管架（更新后）",
            stock: 50,
            category: "工具",
            location: "A-202"
        });
        printResult("PUT /materials/:id", res);

        // 5. 删除物资
        res = await axios.delete(`${BASE_URL}/${createdId}`);
        printResult("DELETE /materials/:id", res);
    } catch (err) {
        console.error("出错了:", err.response?.data || err.message);
    }
}

runTests();
