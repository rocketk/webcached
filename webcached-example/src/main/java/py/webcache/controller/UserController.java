package py.webcache.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import py.webcache.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pengyu on 2016/6/14.
 */
@Controller
public class UserController {

    private Map<Integer, User> userMap = new TreeMap<Integer, User>();

    public static final String JANE = "jane";

    {
        userMap.put(1, new User(1, "JAMES"));
        userMap.put(2, new User(2, "JOHNe"));
        userMap.put(3, new User(3, "ROBERT"));
        userMap.put(4, new User(4, "MICHAEL"));
        userMap.put(5, new User(5, "WILLIAM"));
        userMap.put(6, new User(6, "DAVID"));
        userMap.put(7, new User(7, "RICHARD"));
        userMap.put(8, new User(8, "CHARLES"));
        userMap.put(9, new User(9, "JOSEPH"));
        userMap.put(10, new User(10, "THOMAS"));
        userMap.put(11, new User(11, "CHRISTOPHER"));
        userMap.put(12, new User(12, "DANIEL"));
        userMap.put(13, new User(13, "PAUL"));
        userMap.put(14, new User(14, "MARK"));
        userMap.put(15, new User(15, "DONALD"));
        userMap.put(16, new User(16, "GEORGE"));
        userMap.put(17, new User(17, "KENNETH"));
        userMap.put(18, new User(18, "STEVEN"));
        userMap.put(19, new User(19, "EDWARD"));
        userMap.put(20, new User(20, "BRIAN"));
    }

    @ResponseBody
    @RequestMapping("userInfo")
    public User userInfo(String userId) {
        return userMap.get(userId);
    }

    @ResponseBody
    @RequestMapping("saveUser")
    public String saveUser(User user) {
        userMap.put(user.getUserId(), user);
        return "successful";
    }

    @ResponseBody
    @RequestMapping("userList")
    public List<User> userList(int pageNo) {
        int pageSize = 5;
        int start = (pageNo - 1) * 5 + 1;
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < pageSize; i++) {
            list.add(userMap.get(start + i));
        }
        return list;
    }
}
