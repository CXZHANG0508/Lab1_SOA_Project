const express = require("express");
const router = express.Router();
const db = require("../db");

// 获取全部物资
router.get("/", async (req, res) => {
    try {
        const [rows] = await db.query("SELECT * FROM materials");
        res.json(rows);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 根据 ID 查询
router.get("/:id", async (req, res) => {
    try {
        const [rows] = await db.query(
            "SELECT * FROM materials WHERE material_id = ?",
            [req.params.id]
        );
        if (rows.length === 0) return res.status(404).json({ error: "Not found" });
        res.json(rows[0]);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 新增物资
router.post("/", async (req, res) => {
    const { name, stock, category, location } = req.body;

    try {
        const [result] = await db.query(
            "INSERT INTO materials (name, stock, category, location) VALUES (?, ?, ?, ?)",
            [name, stock, category, location]
        );

        const [newData] = await db.query(
            "SELECT * FROM materials WHERE material_id = ?",
            [result.insertId]
        );

        res.status(201).json(newData[0]);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 更新物资
router.put("/:id", async (req, res) => {
    const { name, stock, category, location } = req.body;

    try {
        await db.query(
            "UPDATE materials SET name=?, stock=?, category=?, location=? WHERE material_id=?",
            [name, stock, category, location, req.params.id]
        );

        const [result] = await db.query(
            "SELECT * FROM materials WHERE material_id = ?",
            [req.params.id]
        );

        res.json(result[0]);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 删除物资
router.delete("/:id", async (req, res) => {
    try {
        await db.query("DELETE FROM materials WHERE material_id = ?", [
            req.params.id,
        ]);
        res.json({ message: "Material deleted" });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
