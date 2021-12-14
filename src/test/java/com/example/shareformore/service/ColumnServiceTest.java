package com.example.shareformore.service;


import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.User;

import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import com.example.shareformore.response.ResponseHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ColumnServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private ColumnRepository columnRepository;
    /*
    若要一起运行 需要保证zhangsan除了default之外已经新建一个专栏
    因为一起运行时newColumn在update和delete操作之前
     */

    @Test
    void newColumn() {
        ColumnService columnService=new ColumnService(userRepository,workRepository,columnRepository);

        //用于测试异常的出现
        try {
            columnService.newColumn(0L,"小说","我的小说");
            fail();
        } catch (Exception e) {
            assertEquals( "User with id: 0 doesn't exist",e.getMessage());
        }
        User user=userRepository.findByName("zhangsan");
        long num=  columnRepository.count();
        columnService.newColumn(user.getUserId(),"pict","my picts");
        assertEquals(num+1,columnRepository.count());

    }

    @Test
    void updateColumn() {
        ColumnService columnService=new ColumnService(userRepository,workRepository,columnRepository);

        //用于测试异常的出现
        try {
            columnService.updateColumn(0L,1L,"novel","my novel!!");
            fail();
        } catch (Exception e) {
            assertEquals( "User with id: 0 doesn't exist",e.getMessage());
        }

        User user=userRepository.findByName("zhangsan");
        try {
            columnService.updateColumn(user.getUserId(),0L,"novel","my novel!!");
            fail();
        } catch (Exception e) {
            assertEquals("com.example.shareformore.exception.column.ColumnNotFoundException",e.getClass().getName());

        }

        User userNo=userRepository.findByName("zhangsanx");
        SpecialColumn column=(SpecialColumn) userRepository.findByName("zhangsan").getColumnSet().toArray()[0];
        try {
            columnService.updateColumn(userNo.getUserId(),column.getColumnId(),"novel","my novel!!");
            fail();
        } catch (Exception e) {
            assertEquals("com.example.shareformore.exception.column.IllegalUpdateColumnException",e.getClass().getName());

        }



        columnService.updateColumn(user.getUserId(),column.getColumnId(),"newx","newwww!!");
        SpecialColumn column2=columnRepository.findByColumnId(column.getColumnId());
        assertNotEquals(column.getColumnName(),column2.getColumnName());
        assertNotEquals(column.getDescription(),column2.getDescription());
        assertNotEquals(column.getUpdateTime(),column2.getUpdateTime());
        assertEquals("newx",column2.getColumnName());
        assertEquals("newwww!!",column2.getDescription());


    }

    @Test
    void deleteColumn() {
        ColumnService columnService=new ColumnService(userRepository,workRepository,columnRepository);


        User user=userRepository.findByName("zhangsan");
        try {
            columnService.deleteColumn(0L);
            fail();
        } catch (Exception e) {
            assertEquals("com.example.shareformore.exception.column.ColumnNotFoundException",e.getClass().getName());

        }

        SpecialColumn column=(SpecialColumn) userRepository.findByName("zhangsan").getColumnSet().toArray()[0];
        assertNotNull(columnRepository.findByColumnId(column.getColumnId()));
        columnService.deleteColumn(column.getColumnId());
        assertNull(columnRepository.findByColumnId(column.getColumnId()));

    }

    @Test
    void listColumn() {
        ColumnService columnService=new ColumnService(userRepository,workRepository,columnRepository);


        User user=userRepository.findByName("zhangsan");
        //用于测试异常的出现
        try {
            columnService.listColumn(0L);
            fail();
        } catch (Exception e) {
            assertEquals( "User with id: 0 doesn't exist",e.getMessage());
        }

        ResponseHolder responseHolder=columnService.listColumn(user.getUserId());
        //System.out.println(responseHolder.getResponseEntity().getBody().toString());
        assertFalse(responseHolder.getResponseEntity().getBody().toString().contains("list=null"));





    }
}