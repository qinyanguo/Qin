package test;

import com.ycmm.business.service.EmployeeService;
import com.ycmm.business.service.UserService;
import com.ycmm.model.BaseBizEmployee;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by jishubu on 2018/5/2.
 */
public class UserTest extends BaseTest{

    UserService userService;

    @Before
    public void testBefore() {

        userService = getBean(UserService.class);
    }

    @Test
    public void getUser() throws Exception {
        userService.testRedis();
    }
}
