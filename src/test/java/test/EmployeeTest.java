package test;

import com.ycmm.business.service.EmployeeService;
import com.ycmm.model.BaseBizEmployee;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by jishubu on 2018/5/2.
 */
public class EmployeeTest extends BaseTest{

    EmployeeService employService;

    @Before
    public void testBefore() {
        employService = getBean(EmployeeService.class);
    }

//    @Test
    public void getEmployee() throws Exception {
        BaseBizEmployee baseBizEmployee = new BaseBizEmployee();
        baseBizEmployee.setId(100000);
        List<Map<String, Object>> maps = employService.queryEmployeeExcel(baseBizEmployee);
        System.out.println(maps);
    }
}
