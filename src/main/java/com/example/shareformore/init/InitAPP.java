package com.example.shareformore.init;

import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component
public class InitAPP implements CommandLineRunner {
    UserRepository userRepository;
    ColumnRepository columnRepository;
    TagRepository tagRepository;
    WorkRepository workRepository;

    @Autowired
    public InitAPP(UserRepository userRepository, ColumnRepository columnRepository, TagRepository tagRepository, WorkRepository workRepository) {
        this.userRepository = userRepository;
        this.columnRepository = columnRepository;
        this.tagRepository = tagRepository;
        this.workRepository = workRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByName("honminden") == null) {
            List<User> userList = initUser();
            List<Tag> tagList = initTag();
            List<SpecialColumn> columnList = initColumn(userList);
            initWork(userList, columnList, tagList);
            System.out.print("Initiation finished");
        }
    }

    public List<User> initUser() {
        List<User> list = new ArrayList<>();
        list.add(new User("Administrator", "share_for_more", null));
        list.add(new User("honminden", "123456", null));
        list.add(new User("hauling", "school_idol", null));
        list.add(new User("搬运工", "share_for_more", null));
        list.add(new User("testman", "123456", null));
        for (User user : list) {
            userRepository.save(user);
        }
        return list;
    }

    public List<SpecialColumn> initColumn(List<User> userList) {
        List<SpecialColumn> list = new ArrayList<>();
        list.add(new SpecialColumn(userList.get(1)));   // 红明灯的默认专栏
        list.add(new SpecialColumn(userList.get(2), "Love Live", "School Idol"));   // hauling的专栏
        list.add(new SpecialColumn(userList.get(3)));   // 搬运工的默认专栏
        list.add(new SpecialColumn(userList.get(3), "搬运作品", "侵权删"));   // 搬运工的搬运专栏
        for (SpecialColumn column : list) {
            columnRepository.save(column);
        }
        return list;
    }

    public List<Tag> initTag() {
        List<Tag> list = new ArrayList<>();
        list.add(new Tag("风景"));
        list.add(new Tag("建筑"));
        list.add(new Tag("人物"));
        list.add(new Tag("原创"));
        list.add(new Tag("ACG"));
        for(Tag tag : list) {
            tagRepository.save(tag);
        }
        return list;
    }

    public void initWork(List<User> userList, List<SpecialColumn> columnList, List<Tag> tagList) {
        String pre = "http://1.117.246.214/8080/images/"; // 记得修改
        User hmd = userList.get(1);
        User idol = userList.get(2);
        User porter = userList.get(3);
        List<Work> list = new ArrayList<>();
        list.add(new Work(hmd, null, "乐", "乐.jpg", "", pre + "1000000.png", 0, null));
        list.add(new Work(hmd, columnList.get(0), "title", "description", "", pre +  "1000001.png", 0, new HashSet<>(){{add(tagList.get(4));}}));
        list.add(new Work(idol, columnList.get(1), "xll", "xll天下第一", "", pre +  "2000000.png", 10, new HashSet<>(){{add(tagList.get(2));}}));
        list.add(new Work(idol, columnList.get(1), "果皇", "cryin来点作用啊", "", pre +  "2000001.png", 10, new HashSet<>(){{add(tagList.get(2));}}));
        list.add(new Work(idol, columnList.get(1), "南小鸟", "怎么又是二次元", "", pre +  "2000002.png", 10, new HashSet<>(){{add(tagList.get(2)); add(tagList.get(1));}}));
        list.add(new Work(porter, columnList.get(2), "Cross-layer Cooperation: Control", "CSE", "One possibility is that it just drives forward, retransmitting the lost packet and continuing to send more data as rapidly as its application supplies it.", "", 20, new HashSet<>(){{add(tagList.get(3));}}));
        list.add(new Work(porter, columnList.get(2), "Cross-layer Cooperation: Feedback", "CSE", "A second feedback idea is for a packet forwarder that is experiencing congestion to set a flag on each forwarded packet.", "", 20, new HashSet<>(){{add(tagList.get(3));}}));
        list.add(new Work(porter, columnList.get(3), "picture", "description", "", pre +  "4000000.png", 5, new HashSet<>(){{add(tagList.get(0));add(tagList.get(2));}}));
        list.add(new Work(porter, columnList.get(3), "scene", "description", "", pre +  "4000001.png", 5, new HashSet<>(){{add(tagList.get(1));add(tagList.get(4));}}));
        list.add(new Work(porter, columnList.get(3), "building", "description", "", pre +  "4000002.png", 0, new HashSet<>(){{add(tagList.get(1));}}));
        for (Work work : list) {
            workRepository.save(work);
        }
    }
}
