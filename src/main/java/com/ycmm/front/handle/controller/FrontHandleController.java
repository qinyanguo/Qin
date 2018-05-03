package com.ycmm.front.handle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.ycmm.base.QueryObject;
import com.ycmm.business.service.WmsStockService;
import com.ycmm.model.Stock;
import com.ycmm.utils.ExportExcelUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycmm.base.FrontParamBean;
import com.ycmm.base.ResultBean;
import com.ycmm.front.handle.service.FrontHandleService;
import com.ycmm.utils.WebUtils;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("yan")
public class FrontHandleController {


    @Autowired
    FrontHandleService frontHandleService;

    @Autowired
    WmsStockService wmsStockService;

    /**
     * value:  指定请求的实际地址， 比如 /action/info之类。
     * method：  指定请求的method类型， GET、POST、PUT、DELETE等
     * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回
     * params： 指定request中必须包含某些参数值是，才让该方法处理
     * headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求
     *
     * 方法仅处理request Content-Type为“application/json”类型的请求.
     * produces标识==>处理request请求中Accept头中包含了"application/json"的请求，同时暗示了返回的内容类型为application/json
     *
     */
    @RequestMapping(value = "/control", method = RequestMethod.POST, consumes = "multipart/form-data")
    @ResponseBody
    public ResultBean execute(HttpServletRequest request, @RequestBody() FrontParamBean frontParamBean) throws Exception{
        /**
         * 1.未禁用cookies ：
         * request.getRequestedSessionId()方法获取的值是Cookie中的值，即使URL中有jsessionid的值。
         * 2.禁用cookies：
         * request.getRequestedSessionId()方法获取的是值就是URL中的jessionid的值，如果没有，则为NULL
         */
        frontParamBean.setSID(request.getRequestedSessionId());
        //获取访问IP地址
        String ip = WebUtils.getNginxAddress(request);
        //获取浏览器类型
        String model = WebUtils.getModel(request);

        request.setAttribute("frontParam", frontParamBean);
        ResultBean resultBean = frontHandleService.frontHandle(frontParamBean, ip, model);

        return resultBean;
    }

    @ResponseBody
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, consumes = "application/json")
    public JSONObject exportExcel1(HttpServletRequest request, Stock stock) throws
            Exception {
        System.err.println("3333333333333333333333333333333333333333333333333333");
        String fileName = "ku_" + System.currentTimeMillis() + ".xls";
        String[] headers = {"编号", "联系人姓名", "；联系电话", "规格"};
        String[] cellName = {"id", "contactName", "contactPhone", "specAttribute"};
        List<Map<String, Object>> stockList = wmsStockService.queryStockList(stock);
        ExportExcelUtils.exportExcel2("库存", fileName, headers, cellName, stockList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", fileName);
        jsonObject.put("code", "1c01");
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/exportExcel222", method = RequestMethod.POST)
    public void exportExcel(HttpServletResponse response, Stock stock) throws Exception {

        System.err.println("===================================================");
        // 导出订单表格
        String fileName = "ku_" + System.currentTimeMillis() + ".xls";
        String[] headers = {"编号", "联系人姓名", "；联系电话", "规格"};
        String[] cellName = {"id", "contactName", "contactPhone", "specAttribute"};
        List<Map<String, Object>> stockList = wmsStockService.queryStockList(stock);
        exportExcel(fileName, headers, cellName, stockList, response);
    }

    public void exportExcel(String fileName, String[] headers, String[] cellName, List<Map<String, Object>> dataList, HttpServletResponse response) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
//          //response.setHeader("Content-disposition", "attachment;");// 设定输出文件头
            response.setContentType("application/vnd.ms-excel");// 定义输出类型
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers","Content-Type,Content-Length, Authorization, Accept," +
                    "X-Requested-With");
            response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
            response.setHeader("X-Powered-By","Jetty");
            ExportExcelUtils.exportExcel("导出信息", fileName, headers, cellName, dataList, os);

            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




















