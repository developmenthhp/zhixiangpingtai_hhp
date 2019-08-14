package com.zhixiangmain.service.base;

import java.util.List;

public interface BaseService<T> {
    T save(T t);

    T getById(Long id);

    List<T> getAll();

    void delById(Long id);
}
