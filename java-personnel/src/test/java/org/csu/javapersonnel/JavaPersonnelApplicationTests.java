package org.csu.javapersonnel;

import org.csu.javapersonnel.entity.Personnel;
import org.csu.javapersonnel.service.PersonnelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class JavaPersonnelApplicationTests {

    @Autowired
    private PersonnelService service;

    @Test
    void testCreate() {
        Personnel p = new Personnel();
        p.setName("测试用户");
        p.setDepartment("测试学院");
        p.setRole("本科生");
        p.setContact("123456789");

        service.save(p);

        System.out.println("新增成功: " + p);
    }

    @Test
    void testReadAll() {
        List<Personnel> list = service.list();
        System.out.println("查询所有人员: ");
        list.forEach(System.out::println);
    }

    @Test
    void testReadOne() {
        Personnel p = service.getById(1);
        System.out.println("查询 ID=1: " + p);
    }

    @Test
    void testUpdate() {
        Personnel p = service.getById(1);
        if (p != null) {
            p.setDepartment("更新后的学院");
            service.updateById(p);
            System.out.println("更新成功: " + p);
        }
    }

    @Test
    void testDelete() {
        boolean result = service.removeById(1);
        System.out.println("删除结果: " + result);
    }
}
