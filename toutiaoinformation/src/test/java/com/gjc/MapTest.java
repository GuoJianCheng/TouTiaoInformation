package com.gjc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapTest {

    @Test
    public void mapTest(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("first","yes");
        System.out.println(map.get("msg"));
    }
    }
