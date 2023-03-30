package com.wonder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wonder.entity.TestTable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DelLevin
 * @since 2023-03-29
 */
public interface TestTableService extends IService<TestTable> {
    boolean deleteById(int id);
    boolean add(TestTable table);
    TestTable selectById(Integer id);
    Object getTestTableById(Integer id);
}
