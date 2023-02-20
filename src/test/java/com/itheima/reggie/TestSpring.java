package com.itheima.reggie;

import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestSpring {

    @Test
    public void test11() {
        Dish d = new Dish();
        DishDTO dt = new DishDTO();
        List<Dish> l1 = new ArrayList<>();
        List<DishDTO> l2 = new ArrayList<>();
        l1.add(d);
        l2.add(dt);
        d.setCode("123");
        BeanUtils.copyProperties(l1, l2);
        System.out.println(l2.get(0).getCode());
    }
}
